package at.ac.uibk.dbis.textfeatures.utilities;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
/**
 * @author Balthasar Huber
 * @version 1.0
 *
 */
public class PosTagger {

	private static MaxentTagger tagger;
	
	private PosTagger(){
	
	}

	public static MaxentTagger getInstance(){
		
		
		if(tagger == null){
			tagger = new MaxentTagger(
					"edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
		}
		return PosTagger.tagger;
	}


}
