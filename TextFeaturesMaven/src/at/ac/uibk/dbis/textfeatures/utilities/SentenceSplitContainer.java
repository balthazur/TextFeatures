package at.ac.uibk.dbis.textfeatures.utilities;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
/**
 * @author Balthasar Huber
 * @version 1.0
 *
 */
public class SentenceSplitContainer {

	private ArrayList<ArrayList<String>> stringSplit;
	private ArrayList<List<HasWord>> hasWordSplit;
	
	
	public SentenceSplitContainer(ArrayList<ArrayList<String>> stringSplit, ArrayList<List<HasWord>> hasWordSplit) {
		super();
		this.stringSplit = stringSplit;
		this.hasWordSplit = hasWordSplit;
	}
	/**
	 * @return the stringSplit
	 */
	public ArrayList<ArrayList<String>> getStringSplit() {
		return stringSplit;
	}
	/**
	 * @return the hasWordSplit
	 */
	public ArrayList<List<HasWord>> getHasWordSplit() {
		return hasWordSplit;
	}
	
	

}
