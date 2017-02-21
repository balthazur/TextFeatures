package at.ac.uibk.dbis.textfeatures.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.uibk.dbis.textfeatures.utilities.TextContainer;
import at.ac.uibk.dbis.textfeatures.utilities.Utility;
import edu.stanford.nlp.ling.TaggedWord;

/**
 * @author Balthasar Huber
 * @version 1.0
 *
 */
public class SyntacticalFeatures {

	private TextContainer textContainer;
	private Utility ut;
	
	
	/**
	 * SyntacticalFeatures-Object allows you to calculate diverse syntactical features.
	 * 
	 * @param TextContainer
	 *            - contains different representations of the text.
	 * 
	 */
	public SyntacticalFeatures(TextContainer tc) {
		this.textContainer = tc;
		ut = new Utility();
	}

	
	/**
	 * Counts the frequency of all POS-Tags in the text.
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the characters
	 *            proportional to 100%( 1.0 = 100%).<br>
	 *            = false - returns raw values;
	 
	 * @return a sorted map with all POS-Tags(K) and their frequency(V).
	 */
	public Map<String, Double> frequencyOfTags(boolean normalized) {

		ArrayList<List<TaggedWord>> taggedText = textContainer.getTaggedSentences();
		ArrayList<String> tags = new ArrayList<String>();
		for (List<TaggedWord> sentence : taggedText) {
			for (TaggedWord word : sentence) {
				tags.add(word.tag());
			}
		}

		HashMap<String, Double> map = new HashMap<String, Double>();
		for (String s : tags) {
			if (!map.containsKey(s)) {
				map.put(s, 1.0);
			} else {
				map.put(s, map.get(s) + 1);
			}
		}
		if (normalized) {
			return ut.sortedByValuesDesc(ut.normalizeFrequency(map));

		}else{
			return ut.sortedByValuesDesc(map);
		}
		

	}
	/**
	 * @param normalized
	 *            = true - will return the frequency of the words proportional
	 *            to 100%( 1.0 = 100%).<br>
	 *            = false - returns raw values;
	 * @param nGram
	 *            - length of the N Grams (must be >= 2)
	 * @return a sorted map with all n-grams of POS-Tags(K) and their
	 *         quantity(V)
	 * @throws IllegalArgumentException
	 *             if nGram < 2
	 */
	public Map<ArrayList<String>, Double> nGramOfTags(int nGram, boolean normalized) {

		if(nGram < 2){
			throw new IllegalArgumentException("Only N-Grams => 2");
		}
		
		
		ArrayList<List<TaggedWord>> taggedText = textContainer.getTaggedSentences();
		ArrayList<String> tags = new ArrayList<String>();

		for (List<TaggedWord> sent : taggedText) {
			for (TaggedWord tag : sent) {
				tags.add(tag.tag());
			}
		}

		HashMap<ArrayList<String>, Double> map = new HashMap<ArrayList<String>, Double>();
		for (int i = 0; i < tags.size() - nGram + 1; i++) {

			ArrayList<String> tmp = new ArrayList<String>();

			for (int j = 0; j < nGram; j++) {

				tmp.add(tags.get(i + j));
			}

			if (map.containsKey(tmp)) {
				map.put(tmp, map.get(tmp) + 1);
			} else {
				map.put(tmp, 1.0);
			}

		}

		if (normalized) {
			return ut.sortedByValuesDesc(ut.normalizeFrequency2(map));

		}else{
			return ut.sortedByValuesDesc(map);
		}

	}


	
	
	/**
	 * Counts the frequency of all function words in the text.
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the characters
	 *            proportional to 100%( 1.0 = 100%).<br>
	 *            = false - returns raw values;
	 
	 * @return a sorted map with all found function words(K) and their frequency(V).
	 */
	public Map<String, Double> frequencyOfFuntionWords(boolean normalized) {

		ArrayList<String> text = textContainer.getOnlyWords();

		// read in function words
		ArrayList<String> functionWords1 = ut.tokenizeString(ut.readInRersource("resources/functionwords.txt"));
		
		// SentenceSplitContainer spc = ut.sentDetect("functionwords2.txt");
		// ArrayList<ArrayList<String>> functionWords2 = spc.getStringSplit();

		HashMap<String, Double> map = new HashMap<String, Double>();

		for (String w : text)
			for (String fw : functionWords1) {
				if (w.equals(fw)) {
					if (!map.containsKey(w)) {
						map.put(w, 1.0);
					} else {
						map.put(w, map.get(w) + 1);
					}
				}

			}
	

		if (normalized) {
			return ut.sortedByValuesDesc(ut.normalizeFrequency(map));

		}else{
			return ut.sortedByValuesDesc(map);
		}
	}

	
	
	
	/**
	 * Calculates average function words per sentence
	 * <b>Function words are e.g<br>  prepositions, pronouns, auxiliary verbs, conjunctions, grammatical articles or particles.</b>
	 * 
	 * @return total function words in text / total sentences
	 */
	public double avgFunctionWordsPerSentence() {

		ArrayList<String> text = textContainer.getOnlyWords();

		// read in function words
		ArrayList<String> functionWords1 = ut.tokenizeString(ut.readInRersource("resources/functionwords.txt"));

		// SentenceSplitContainer spc = ut.sentDetect("functionwords2.txt");
		// ArrayList<ArrayList<String>> functionWords2 = spc.getStringSplit();

		double counter = 0;
		for (String w : text)
			for (String fw : functionWords1) {
				if (w.equals(fw)) {
					counter++;
				}

			}
		return counter / textContainer.getSentSplitCont().getStringSplit().size();
	}
	/**
	 * Calculates the punctuation - word ratio
	 * @return total punctuations / text length in words
	 */
	public double punctuationWordRatio() {
		ArrayList<String> text = textContainer.getTokenizedWords();
		double counter = 0;
		for (String word : text) {
			if (word.matches("[^A-Za-z0-9]")) {
				counter += 1;
			}
		}
		return counter / textContainer.getTextLengthInWords();
	}
}
