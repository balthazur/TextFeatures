package at.ac.uibk.dbis.textfeatures.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.ling.TaggedWord;
/**
 * @author Balthasar Huber
 * @version 1.0
 *
 */
public class TextContainer {

	private String filePath;
	private int textLengthInWords;
	private int textLengthInChars;
	private int textLengthInTokens;
	private int numberOfDifferentWords;
	
	private ArrayList<String> tokenizedChars;
	private ArrayList<String> tokenizedWords;
	private SentenceSplitContainer sentSplitCont;
	private ArrayList<List<TaggedWord>> taggedSentences;
	private Map<String,Double> frequencyOfwords;
	private Map<String, Double> hapaxLegomenon;
	private ArrayList<String> onlyWords;
	private Map<String, Double> dislegomenon;
	private String textAsString;
	

	private Utility ut;
	
	
//	public static TextContainer isPath(String path){
//		return new TextContainer(path, true);
//	}
//	
//	public static TextContainer isString(String text){
//		return new TextContainer(text, false);
//	}
	

	
	/**
	 * TextContainer-Object contains different representations of a text.
	 * 
	 * @param filePath - the filepath/the string
	 * @param isFilePath = true - then the String is seen as relative filepath.
	 * @param isFilePath = false - then the String is seen as the text.
	 * 
	 */
	public TextContainer(String filePath, boolean isFilepath) {
		ut = new Utility();
		if(isFilepath){
			this.filePath = filePath;

			this.tokenizedWords = ut.tokenize(filePath);
			this.onlyWords = ut.onlyWords(this.tokenizedWords); 
			this.sentSplitCont = ut.sentDetect(filePath);
			
			this.taggedSentences = ut.posTagging(sentSplitCont.getHasWordSplit());
			this.frequencyOfwords = ut.frequencyOfWords(this.onlyWords);
			
			this.hapaxLegomenon = ut.hapaxLegomenon(this.onlyWords, 1);
			this.dislegomenon = ut.hapaxLegomenon(this.onlyWords, 2);//eigentlich dislegomena
			try {
				this.textAsString = ut.readFile(filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.tokenizedChars = ut.tokenizeChars(this.textAsString);
			this.textLengthInChars = tokenizedChars.size();
			this.textLengthInWords = onlyWords.size();
			this.textLengthInTokens = tokenizedWords.size();
			this.numberOfDifferentWords = frequencyOfwords.size();
		}else{
			this.filePath = filePath;

			this.tokenizedWords = ut.tokenizeString(filePath);
			this.onlyWords = ut.onlyWords(this.tokenizedWords); 
			this.sentSplitCont = ut.sentDetectString(filePath);
			
			this.taggedSentences = ut.posTagging(sentSplitCont.getHasWordSplit());
			this.frequencyOfwords = ut.frequencyOfWords(this.onlyWords);
			
			this.hapaxLegomenon = ut.hapaxLegomenon(this.onlyWords, 1);
			this.dislegomenon = ut.hapaxLegomenon(this.onlyWords, 2);//eigentlich dislegomena
			
				this.textAsString = filePath;
			
			
			
			this.tokenizedChars = ut.tokenizeChars(this.textAsString);
			this.textLengthInChars = tokenizedChars.size();
			this.textLengthInWords = onlyWords.size();
			this.textLengthInTokens = tokenizedWords.size();
			this.numberOfDifferentWords = frequencyOfwords.size();
		}
		
		
		
	}

	/**
	 * @return the Text Length In Words
	 */
	public int getTextLengthInWords() {
		return textLengthInWords;
	}

	/**
	 * @return the Text Length In Chars
	 */
	public int getTextLengthInChars() {
		return textLengthInChars;
	}

	/**
	 * @return the Text Length InTokens
	 */
	public int getTextLengthInTokens() {
		return textLengthInTokens;
	}

	/**
	 * @return the Number Of Different Words In The Text
	 */
	public int getNumberOfDifferentWords() {
		return numberOfDifferentWords;
	}

	/**
	 * @return the Text As String<br>
	 * whitespaces : yes <br>
	 *  all lower case : no<br>
	 *  contains punctuation : yes<br>
	 */
	public String getTextAsString() {
		return textAsString;
	}

	/**
	 * @return a container which contains of 2 types of sentence splits
	 */
	public SentenceSplitContainer getSentSplitCont() {
		return sentSplitCont;
	}

	/**
	 * @return all hapax dislegomenon = all words which occur exactly 2 times in text<br>
	 * whitespaces : no<br>
	 *  all lower case : yes<br>
	 *  contains punctuation : no<br>
	 */
	public Map<String, Double> getDislegomenon() {
		return dislegomenon;
	}

	

	/**
	 * @return list of words<br>
	 *	 whitespaces : no<br>
	 *  all lower case : yes<br>
	 *  contains punctuation : no<br>
	 */
	public ArrayList<String> getOnlyWords() {
		return onlyWords;
	}

	/**
	 * @return a map with the word as Key and its frequency in the text as Value<br>
	 * whitespaces : no<br>
	 *  all lower case : yes<br>
	 *  contains punctuation : no<br>
	 */
	public Map<String, Double> getFrequencyOfwords() {
		return frequencyOfwords;
	}

	/**
	 * @return the hapaxLegomenon<br>
	 * whitespaces : no<br>
	 *  all lower case : yes<br>
	 *  contains punctuation : no<br>
	 */
	public Map<String, Double> getHapaxLegomenon() {
		return hapaxLegomenon;
	}

	/**
	 * @return List with tokenized Chars<br>
	 * whitespaces : no<br>
	 *  all lower case : no<br>
	 *  contains punctuation : yes<br>
	 */
	public ArrayList<String> getTokenizedChars() {
		return tokenizedChars;
	}

	/**
	 * @return the file name as String,<br>
	 * or the entered String, if "isFilepath" was false.
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @return a list with tokens<br>
	 *  whitespaces : no<br>
	 *  all lower case : no  <br>
	 *  contains punctuation : yes<br>
	 */
	public ArrayList<String> getTokenizedWords() {
		return tokenizedWords;
	}

	
	
	/**
	 * @return Lists in an ArrayList, where every List represents a Sentence, and the ArrayList the whole text<br>
	 * whitespaces : no<br>
	 *  all lower case : no<br>
	 *  contains punctuation : yes<br>
	 */
	public ArrayList<List<TaggedWord>> getTaggedSentences() {
		return taggedSentences;
	}

	
	

}
