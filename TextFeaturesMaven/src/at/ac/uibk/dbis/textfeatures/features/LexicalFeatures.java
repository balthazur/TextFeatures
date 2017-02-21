package at.ac.uibk.dbis.textfeatures.features;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.util.Map.Entry;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang3.time.StopWatch;

import com.org.watsonwrite.lawrence.Lawrence;

import at.ac.uibk.dbis.textfeatures.utilities.TextContainer;
import at.ac.uibk.dbis.textfeatures.utilities.Utility;
/**
 * @author Balthasar Huber
 * @version 1.0
 *
 */
public class LexicalFeatures {
	private Utility ut;
	private TextContainer textContainer;
	// for Syllable count
	private Lawrence lawrence;

	/**
	 * LexicalFeatures-Object allows you to calculate diverse lexical features.
	 * 
	 * @param tc
	 *            - contains different representations of the text.
	 * 
	 */
	public LexicalFeatures(TextContainer tc) {

		// need my utilities;
		ut = new Utility();
		// the text
		this.textContainer = tc;

		// need this for syllable count
		try {
			lawrence = new Lawrence();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Counts the frequency of all characters in the text, including white
	 * spaces and special chars
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the characters
	 *            proportional to 100%( 1.0 = 100%).<br>
	 *            = false - returns raw values;
	 * @param allLowerCase
	 *            = true - all characters will be transformed into lower case.
	 * @return a sorted map with all characters(K) and their frequency(V).
	 */

	public Map<String, Double> frequencyOfChars(boolean normalized, boolean allLowerCase) {
		ArrayList<String> text = textContainer.getTokenizedChars();

		HashMap<String, Double> map = new HashMap<String, Double>();
		for (String s : text) {
			if (allLowerCase) {
				s = s.toLowerCase();
			}
			if (!map.containsKey(s)) {
				map.put(s, 1.0);
			} else {
				map.put(s, map.get(s) + 1);
			}
		}
		if (normalized) {
			return ut.sortedByValuesDesc(ut.normalizeFrequency(map));
		}
		return ut.sortedByValuesDesc((map));

	}

	/**
	 * Counts the frequency of all special characters in the text.
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the characters
	 *            proportional to 100%( 1.0 = 100%).<br>
	 *            = false - returns raw values;
	 * @return a sorted map with all special characters(K) and their
	 *         frequency(V).
	 */
	public Map<String, Double> frequencyOfSpecialChars(boolean normalized) {
		ArrayList<String> text = textContainer.getTokenizedWords();

		HashMap<String, Double> map = new HashMap<String, Double>();

		for (String word : text) {
			if (word.matches("[^A-Za-z0-9]")) {
				if (!map.containsKey(word)) {
					map.put(word, 1.0);
				} else {
					map.put(word, map.get(word) + 1);
				}

			}
		}
		if (normalized) {
			return ut.sortedByValuesDesc(ut.normalizeFrequency(map));
		}
		return ut.sortedByValuesDesc((map));

	}

	/**
	 * Counts the frequency of all words in the text.(Words are transformed into
	 * lower case).
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the words proportional
	 *            to 100%( 1.0 = 100%).<br>
	 *            = false - returns raw values;
	 * @return a sorted map with all words(K) and their frequency(V)
	 */

	public Map<String, Double> frequencyOfWords(boolean normalized) {
		if (normalized) {
			return ut.sortedByValuesDesc(ut.normalizeFrequency(textContainer.getFrequencyOfwords()));
		}
		return ut.sortedByValuesDesc(textContainer.getFrequencyOfwords());
	}

	/**
	 * Calculates the average word length.<br>
	 * <b>Formula:<br>
	 * a = sum of all word lenghts<br>
	 * b = textlength in words<br>
	 * a / b</b>
	 * 
	 * @return the average word length
	 */

	public double avgWordLength() {
		ArrayList<String> text = textContainer.getOnlyWords();

		double avg = 0;
		for (String s : text) {
			avg += s.length();
		}

		// System.out.println(avg + " " + text.size());
		return avg / text.size();

	}

	/**
	 * Calculates the average number of words per sentence<br>
	 * <b>Formula:<br>
	 * a = textlength in words<br>
	 * b = textlength in sentences<br>
	 * a / b</b>
	 * 
	 * @return average number of words per sentence
	 */
	public double avgWordsPerSentence() {

		ArrayList<ArrayList<String>> text = textContainer.getSentSplitCont().getStringSplit();

		double words = textContainer.getTextLengthInWords();
		double sentences = text.size();

		return words / sentences;

	}

	/**
	 * Calculates the average number of syllables per sentence. <br>
	 * <b>Formula:<br>
	 * a = number of syllables in text<br>
	 * b = textlength in sentences<br>
	 * a / b</b> Syllable Count realized with
	 * <a href="https://github.com/troywatson/Lawrence-Style-Checker">https://
	 * github.com/troywatson/Lawrence-Style-Checker</a>
	 * 
	 * @return average number of syllables per sentence
	 */
	public double avgSyllablesPerSentence() {

		ArrayList<ArrayList<String>> text = textContainer.getSentSplitCont().getStringSplit();

		double syllables = 0;

		for (String s : textContainer.getOnlyWords()) {
			syllables += lawrence.countSyllablesLight(s);

		}

		return syllables / text.size();

	}

	/**
	 * @param normalized
	 *            = true - will return the frequency of the words proportional
	 *            to 100%( 1.0 = 100%).<br>
	 *            = false - returns raw values;
	 * @param nGram
	 *            - length of the N Grams (must be >= 2)
	 * @return a sorted map with all n-grams of words(K) and their quantity(V)
	 * @throws IllegalArgumentException
	 *             if nGram < 2
	 */
	public Map<ArrayList<String>, Double> wordNGrams(boolean normalized, int nGram) {

		if (nGram < 2) {
			throw new IllegalArgumentException("Only N-Grams => 2");
		}

		ArrayList<String> text = textContainer.getOnlyWords();

		HashMap<ArrayList<String>, Double> map = new HashMap<ArrayList<String>, Double>();
		for (int i = 0; i < text.size() - nGram + 1; i++) {

			ArrayList<String> tmp = new ArrayList<String>();

			for (int j = 0; j < nGram; j++) {

				tmp.add(text.get(i + j));
			}

			if (map.containsKey(tmp)) {
				map.put(tmp, map.get(tmp) + 1);
			} else {
				map.put(tmp, 1.0);
			}

		}

		if (normalized) {
			return ut.sortedByValuesDesc(ut.normalizeFrequency2(map));
		}
		return ut.sortedByValuesDesc((map));

	}

	/**
	 * @param normalized
	 *            = true - will return the frequency of the words proportional
	 *            to 100%( 1.0 = 100%).<br>
	 *            = false - returns raw values;
	 * @param nGram
	 *            - length of the N Grams (must be >= 2)
	 * @return a sorted map with all n-grams of characters(K) and their
	 *         quantity(V)
	 * @throws IllegalArgumentException
	 *             if nGram < 2
	 */
	public Map<ArrayList<String>, Double> charsNGrams(boolean normalized, int nGram) {

		if (nGram < 2) {
			throw new IllegalArgumentException("Only N-Grams => 2");
		}

		ArrayList<String> text = textContainer.getTokenizedChars();

		HashMap<ArrayList<String>, Double> map = new HashMap<ArrayList<String>, Double>();
		for (int i = 0; i < text.size() - nGram + 1; i++) {

			ArrayList<String> tmp = new ArrayList<String>();

			for (int j = 0; j < nGram; j++) {

				tmp.add(text.get(i + j));
			}

			if (map.containsKey(tmp)) {
				map.put(tmp, map.get(tmp) + 1);
			} else {
				map.put(tmp, 1.0);
			}

		}

		if (normalized) {
			return ut.sortedByValuesDesc(ut.normalizeFrequency2(map));
		}
		return ut.sortedByValuesDesc((map));
	}

	/**
	 * Calculates the number of Hapax legomena in the text(words which occur
	 * exactly one time)
	 * 
	 * @return Number of Hapax Legomena in Text
	 */
	public double numberOfHapaxLegomena() {
		return textContainer.getHapaxLegomenon().size();

	}

	/**
	 * Calculates the number of Hapax Dislegomena in the text(words which occur
	 * exactly two times)
	 * 
	 * @return Number of Hapax Dislegomena in Text
	 */
	public double numberOfHapaxDislegomena() {
		return textContainer.getDislegomenon().size();

	}

	/**
	 * Calculates the index of diversity, that is the proportion of hapax
	 * legomena and the text lenght in words<br>
	 * <b>Formula:<br>
	 * a = frequency of hapax legomena<br>
	 * b = textlength in words<br>
	 * a / b</b>
	 */
	public double indexOfDiversity() {

		Map<String, Double> hpWords = textContainer.getHapaxLegomenon();
		double x = textContainer.getTextLengthInWords();
		double y = hpWords.size();
		return y / x;

	}

	/**
	 * Calculates Honore's Measure<br>
	 * <b>Formula:<br>
	 * N = text length in words<br>
	 * V = number of different words in text<br>
	 * V1 = number of hapax legomena(occur once)<br>
	 * 100*log(N/(1-(V1/V)))</b>
	 * 
	 * @return Honore's Measure
	 * @throws IllegalArgumentException
	 *             if V == V1 (happens when text is very short)
	 */
	public double honoreMeasure() {

		// # of words in Text
		double N = textContainer.getTextLengthInWords();
		// # of different Words in text
		double V = textContainer.getNumberOfDifferentWords();
		// # of words which appear exactly 1 time in text
		double V1 = textContainer.getHapaxLegomenon().size();

		if (V == V1) {
			throw new IllegalArgumentException("Can't divide by 0. Text is probably too short");
		}
		double R = 100 * Math.log(N / (1 - (V1 / V)));

		return R;

	}

	/**
	 ** Calculates Sichel's Measure<br>
	 * <b>Formula:<br>
	 * V = number of different words in text<br>
	 * V2 = number of hapax dislegomena(occur twice)<br>
	 * V2/V</b>
	 * 
	 * @return Honore's Measure
	 * @throws IllegalArgumentException
	 *             if V == 0(Text empty or all words in text the same)
	 */
	public double sichelMeasure() {
		// # of different Words in text
		double V = textContainer.getNumberOfDifferentWords();
		if (V == 0) {
			throw new IllegalArgumentException("Text empty or all words in text the same");
		}
		// # of words which appear exactly 2 times in text
		double V2 = textContainer.getDislegomenon().size();
		return V2 / V;

	}

	/**
	 * @deprecated This Feature is implemented as in Literature said<br>
	 *             but with texts longer then 15 words the result will be
	 *             infinity
	 */
	@Deprecated
	public double brunetMeasure() {
		// # of words in Text
		double N = textContainer.getOnlyWords().size();

		// # of different Words in text
		double V = textContainer.getFrequencyOfwords().size();
		// a = constant
		double a = 0.17;

		double W = Math.pow(N, V - a);
		return W;

	}

	/**
	 * Calculates Flesch-Kincaid Grade<br>
	 * Syllable Count realized with <br>
	 * <a href="https://github.com/troywatson/Lawrence-Style-Checker">https://
	 * github.com/troywatson/Lawrence-Style-Checker</a><br>
	 * <b>Formula:<br>
	 * W = text length in words<br>
	 * S = text length in sentences<br>
	 * T= total syllables in text <br>
	 * 206.835-1.015(W/S)-84.6(T/W)</b>
	 * 
	 * @return Flesch-Kincaid Grade
	 */
	public double fleschKincaidGrade() {
		double totalWords = textContainer.getTextLengthInWords();
		double totalSentences = textContainer.getSentSplitCont().getStringSplit().size();

		double totalSyllables = 0;
		// count all Syllables for every Word
		for (String word : textContainer.getOnlyWords()) {
			totalSyllables += lawrence.getSyllable(word);
		}

		// calculate FK with formula
		double result = 206.835 - (1.015 * (totalWords / totalSentences)) - (84.6 * (totalSyllables / totalWords));
		return result;

	}

	/**
	 * Calculates Gunning-Fog Index<br>
	 * Syllable Count realized with <br>
	 * <a href="https://github.com/troywatson/Lawrence-Style-Checker">https://
	 * github.com/troywatson/Lawrence-Style-Checker</a><br>
	 * <b>Formula:<br>
	 * W = text length in words<br>
	 * S = text length in sentences<br>
	 * C=Complex words(3 or more syllables) <br>
	 * 0.4((W/S)+100(C/W))</b>
	 * 
	 * @return Gunning-Fog Index
	 */
	public double gunningFogIndex() {
		double totalWords = textContainer.getTextLengthInWords();
		double totalSentences = textContainer.getSentSplitCont().getStringSplit().size();

		double complexWords = 0;
		// count all complex words( 3 or more syllables)
		for (String word : textContainer.getOnlyWords()) {
			if (lawrence.getSyllable(word) >= 3) {
				complexWords += 1;
			}

		}

		// calculate GF with formula
		double result = 0.4 * ((totalWords / totalSentences) + (100 * (complexWords / totalWords)));
		return result;

	}

	/**
	 * Calculates Yule's K Measure<br>
	 * 
	 * <b>Formula:<br>
	 * N = number of different words in text<br>
	 * Entry<String, Double> --> String = words , Double = how often it occurs
	 * in text M =sum of (all "Doubles"(from Entry)^2) <br>
	 * 
	 * 1000((M-N)/N^2)</b>
	 * 
	 * @return Yule's K Measure
	 * @throws IllegalArgumentException
	 *             - Result will be 0, as M and N are equal. Probably text is
	 *             too short.
	 */
	public double yuleKMeasure() {
		double N = textContainer.getNumberOfDifferentWords();
		double M = 0;
		for (Entry<String, Double> entry : textContainer.getFrequencyOfwords().entrySet()) {
			M += Math.pow(entry.getValue(), 2);
		}
		if (M == N) {
			throw new IllegalArgumentException("Result will be 0, as M and N are equal. Probably text is too short");
		}
		double result = 10000 * ((M - N) / Math.pow(N, 2));

		return result;

	}

	/**
	 * Calculates Dale-Chall Index<br>
	 * 
	 * <b>Formula:<br>
	 * Raw Score = 0.1579 * (PDW) + 0.0496 * ASL <br>
	 * 
	 * Raw Score = Reading Grade of a reader who can comprehend your text at 3rd
	 * <br>
	 * grade or below.<br>
	 * 
	 * PDW = Percentage of Difficult Words(Words not in List(3000 words))<br>
	 * 
	 * ASL = Average Sentence Length in words<br>
	 * 
	 * If (PDW) is greater than 5%, then:<br>
	 * 
	 * Adjusted Score = Raw Score + 3.6365, otherwise Adjusted Score = Raw<br>
	 * Score<br>
	 * </b>
	 * 
	 */
	public double daleChallIndex() {
		// Load List with all words in lower case but without punctuation
		ArrayList<String> text = textContainer.getOnlyWords();

		// Load all "Dale-Chall words"
		ArrayList<String> daleChallWords = ut.tokenizeString(ut.readInRersource("resources/dale-chall.txt"));
		
		// # ofsSentences in text
		double sentenceCounter = textContainer.getSentSplitCont().getStringSplit().size();

		// # of words in text
		double wordCounter = text.size();

		// create difficult word list --> every word that is not in
		// dale-chall-word-list
		ArrayList<String> difficultWordList = new ArrayList<String>();
		for (String word : text) {
			if (!daleChallWords.contains(word)) {

				difficultWordList.add(word);
			}

		}

		// finally, # of difficult words
		double difficultWords = difficultWordList.size();

		// calculate result with formula
		double result = (0.1579 * ((difficultWords / wordCounter) * 100)) + (0.0496 * (wordCounter / sentenceCounter));

		if (((wordCounter * 0.05) <= difficultWords)) {
			result += 3.6365;
		}
		return result;
	}

	/**
	 * Calculates Type-Token Ratio<br>
	 * 
	 * <b>Formula:<br>
	 * Token = text length in words<br>
	 * Type = number of different words <br>
	 * 
	 * result = Type/Token </b>
	 * 
	 * @return Type-Token Ratio
	 * 
	 */
	public double typeTokenRatio() {
		double tokens = textContainer.getTextLengthInWords();
		double types = textContainer.getNumberOfDifferentWords();

		double result = types / tokens;
		return result;

	}

	/**
	 * Calculates Average Word Frequency Class<br>
	 * 
	 * <b>Formula:<br>
	 * "Let C be a text corpus, and let |C| be the number of words in C.<br>
	 * Moreover, let f(w) denote the frequency of a word w ∈ C, and let r(w)<br>
	 * denote the rank of w in a word list of C, which is sorted by decreasing
	 * <br>
	 * frequency. In accordance with [17] we define the word frequency class<br>
	 * c(w) of a word w ∈ C as log2 (f(w ∗ )/f(w)), where w ∗ denotes the most
	 * <br>
	 * frequently used word in C"-.<br>
	 * </b> Plagiarism Detection without Reference Collections<br>
	 * -<br>
	 * Sven Meyer zuEissen, Benno Stein, and Marion Kulig
	 * 
	 * @return Average Word Frequency Class
	 * 
	 */
	public double avgWordFrequencyClass() {
		Map<String, Double> words = textContainer.getFrequencyOfwords();
		Entry<String, Double> entry = words.entrySet().iterator().next();
		double mostFrequent = entry.getValue();
		double avg = 0;
		for (Entry<String, Double> word : words.entrySet()) {
			avg += ut.LogBaseX((mostFrequent / word.getValue()), 2);

		}
		if (avg == 0) {
			throw new IllegalArgumentException("Result will be 0, text propably too short");
		}
		avg = avg / words.size();

		return avg;
	}

	/**
	 * Measures the Compression Rate(1.0 = 100%) Compression Tool = "ZIP" with
	 * Level 9 (Highest)<br>
	 * 
	 * NOT WORKING CORRECTLY WITH A STRING ENTERED.<br>
	 * WORKS FINE WITH A FILEPATH.
	 * 
	 * @return the compression rate e.g. 10 MB compresses into 9MB<br>
	 *         then 0.1(10%) will be returned
	 * @throws IOException
	 */
	public double compressionRate() throws IOException {
		String text = textContainer.getTextAsString();

		final File f = new File("test.zip");
		final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
		out.setLevel(9);
		ZipEntry e = new ZipEntry("mytext.txt");
		out.putNextEntry(e);
		byte[] data = text.getBytes();
		double dataLength = data.length;
		out.write(data, 0, data.length);

		out.closeEntry();
		out.close();
		double compressed = f.length();
		f.delete();
		
		return 1 - (compressed / dataLength);
	}

	
	
	
	
	/**
	 * Calculates the compression in MegaBytes/Sec
	 * @param rounds - number of times the algorithm runs<br>more rounds -> better result
	 * @return how many MegaBytes/Sec compress
	 * @throws Exception - When File is too small, the time to compress cannot be measured
	 */
	public double compressionTime(int rounds) throws Exception {
		String text = textContainer.getTextAsString();

		final File f = new File("test.zip");
		final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
		out.setLevel(9);
		ZipEntry e = new ZipEntry("mytext.txt");
		out.putNextEntry(e);

		byte[] data = text.getBytes();
		double dataLength = data.length;
		double time = 0;
		for (int i = 0; i < rounds; i++) {
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			out.write(data, 0, data.length);
			stopWatch.stop();
			time += stopWatch.getTime();
			if (i == rounds / 4) {
				System.out.println("25% of calculating compression-time done");
			}
			if (i == rounds / 2) {
				System.out.println("50% of calculating compression-time done");
			}
			if (i == rounds - (rounds / 4)) {
				System.out.println("75% of calculating compression-time done");
			}
		}
		time = time / rounds;

		out.closeEntry();
		out.close();

		time = time / 1000; // time in seconds now
		if (time == 0) {
			throw new Exception("Divide by 0 Error / time was too short to be measured");
		}
		return (dataLength / time) / 1000000; // megabytes pro sekunde

	}

}
