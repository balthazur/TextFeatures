package at.ac.uibk.dbis.textfeatures.features;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.languagetool.JLanguageTool;

import org.languagetool.language.AmericanEnglish;
import org.languagetool.language.BritishEnglish;

import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;

import at.ac.uibk.dbis.textfeatures.utilities.TextContainer;
import at.ac.uibk.dbis.textfeatures.utilities.Utility;

/**
 * @author Balthasar Huber
 * @version 1.0
 *
 */
public class ErrorFeatures {
	private Utility ut;
	private boolean british;
	private TextContainer textContainer;
	private Map<Rule, Double> frequencyOfRules;
	private Map<String, Double> frequencyOfSuggestions;

	/**
	 * ErrorFeatures-Object allows you to calculate diverse error features.<br>
	 * <b>To use Functions, you have to make a new ErrorFeature Object <br>
	 * and then call calculateErrors() on it, so all Errors are loaded.<br>
	 * Otherwise, you will get NullPointerExceptions.</b><br>
	 * Error Calculation is done with
	 * <a href="https://languagetool.org/development/api/">LanguageTool Java
	 * Api</a>.<br>
	 * To find out what each Rule stands for, also visit this link.
	 * 
	 * @param TextContainer
	 *            - contains different representations of the text.
	 * @param british
	 *            = true, use British English
	 * @param british
	 *            = false, use American English
	 */
	public ErrorFeatures(TextContainer tc, boolean british) {

		// need my utilities;
		this.ut = new Utility();
		this.textContainer = tc;
		this.british = british;

	}

	public void calculateErrors() {

		frequencyOfRules(british);

	}

	/**
	 * Counts the frequency of all Error Rules as well as all primary
	 * suggestions found in the text.
	 * 
	 * @param british
	 *            = true, use British English
	 * @param british
	 *            = false, use American English
	 * 
	 * @return a sorted map with all Rules(K) and their frequency(V).
	 */
	private void frequencyOfRules(boolean british) {
		JLanguageTool langTool = null;
		if (british) {
			langTool = new JLanguageTool(new BritishEnglish());
		} else {
			langTool = new JLanguageTool(new AmericanEnglish());
		}

		// langTool.activateDefaultPatternRules(); -- only needed for LT 2.8 or
		// earlier
		List<RuleMatch> matches = null;
		try {
			matches = langTool.check(textContainer.getTextAsString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<Rule, Double> map = new HashMap<Rule, Double>();
		HashMap<String, Double> map2 = new HashMap<String, Double>();

		for (RuleMatch match : matches) {
			if (!map.containsKey(match.getRule())) {
				map.put(match.getRule(), 1.0);
			} else {
				map.put(match.getRule(), map.get(match.getRule()) + 1);
			}
			try{
			if (!map2.containsKey(match.getSuggestedReplacements().get(0))) {
				map2.put(match.getSuggestedReplacements().get(0), 1.0);
			} else {
				map2.put(match.getSuggestedReplacements().get(0), map.get(match.getRule()) + 1);
			}
			
			}catch(IndexOutOfBoundsException e){
				
			}
		}

		this.frequencyOfRules = ut.sortedByValuesDesc(map);
		this.frequencyOfSuggestions = ut.sortedByValuesDesc(map2);

	}

	/**
	 * Counts the frequency of all Error Rules found in the text.
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw values;
	 * 
	 * @return a sorted map with all Rules(K) and their frequency(V).
	 */
	public Map<Rule, Double> getFrequencyOfRules(boolean normalized) {
		if (normalized) {
			return ut.sortedByValuesDesc(ut.normalizeFrequency3(this.frequencyOfRules));
		} else {
			return frequencyOfRules;
		}
	}

	/**
	 * Counts the frequency of all Suggestions(on position 1) found in the text.
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw values;
	 * 
	 * @return a sorted map with all Rules(K) and their frequency(V).
	 */
	public Map<String, Double> getFrequencyOfSuggestions(boolean normalized) {
		if (normalized) {
			return ut.sortedByValuesDesc(ut.normalizeFrequency(this.frequencyOfSuggestions));
		} else {
			return frequencyOfSuggestions;
		}
	}

	/**
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw value;
	 * @return the frequency of "SentenceWhitespaceRule" in the text
	 */
	public double getSentenceWhitespaceRule(boolean normalized) {
		return getFrequencyOfRule("org.languagetool.rules.SentenceWhitespaceRule", normalized);
	}

	/**
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw value;
	 * @return the frequency of "CommaWhitespaceRule" in the text
	 */
	public double getCommaWhitespaceRule(boolean normalized) {
		return getFrequencyOfRule("org.languagetool.rules.CommaWhitespaceRule", normalized);
	}

	/**
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw value;
	 * @return the frequency of "DoublePunctuationRule" in the text
	 */
	public double getDoublePunctuationRule(boolean normalized) {
		return getFrequencyOfRule("org.languagetool.rules.DoublePunctuationRule", normalized);
	}

	/**
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw value;
	 * @return the frequency of "UppercaseSentenceStartRule" in the text
	 */
	public double getUppercaseSentenceStartRule(boolean normalized) {
		return getFrequencyOfRule("org.languagetool.rules.UppercaseSentenceStartRule", normalized);
	}

	/**
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw value;
	 * @return the frequency of "MultipleWhitespaceRule" in the text
	 */
	public double getMultipleWhitespaceRule(boolean normalized) {
		return getFrequencyOfRule("org.languagetool.rules.MultipleWhitespaceRule", normalized);
	}

	/**
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw value;
	 * @return the frequency of "EnglishUnpairedBracketsRule" in the text
	 */
	public double getEnglishUnpairedBracketsRule(boolean normalized) {
		return getFrequencyOfRule("org.languagetool.rules.en.EnglishUnpairedBracketsRule", normalized);
	}

	/**
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw value;
	 * @return the frequency of "EnglishUnpairedBracketsRule" in the text
	 */
	public double getEnglishWordRepeatRule(boolean normalized) {
		return getFrequencyOfRule("org.languagetool.rules.en.EnglishUnpairedBracketsRule", normalized);
	}

	/**
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw value;
	 * @return the frequency of "AvsAnRule" in the text
	 */
	public double getAvsAnRule(boolean normalized) {
		return getFrequencyOfRule("org.languagetool.rules.en.AvsAnRule", normalized);
	}

	/**
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw value;
	 * @return the frequency of "EnglishWordRepeatBeginningRule" in the text
	 */
	public double getEnglishWordRepeatBeginningRule(boolean normalized) {
		return getFrequencyOfRule("org.languagetool.rules.en.EnglishWordRepeatBeginningRule", normalized);
	}

	/**
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw value;
	 * @return the frequency of "CompoundRule" in the text
	 */
	public double getCompoundRule(boolean normalized) {
		return getFrequencyOfRule("org.languagetool.rules.en.CompoundRule@482f8f11", normalized);
	}

	/**
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw value;
	 * @return the frequency of "ContractionSpellingRul" in the text
	 */
	public double getContractionSpellingRule(boolean normalized) {
		return getFrequencyOfRule("org.languagetool.rules.en.ContractionSpellingRule", normalized);
	}

	/**
	 * 
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw value;
	 * @return the frequency of "MorfologikAmericanSpellerRule" in the text
	 */
	public double getMorfologikAmericanSpellerRules(boolean normalized) {
		return getFrequencyOfRule("org.languagetool.rules.en.MorfologikAmericanSpellerRule", normalized);
	}

	/**
	 * 
	 * @param ruleDescription
	 *            - first Rule that contains the String ruleDescription will be
	 *            returned <br>
	 *            Note that every Rule has an unique name.
	 * @param normalized
	 *            = true - will return the frequency of the Rule proportional to
	 *            100%( 1.0 = 100%).<br>
	 *            = false - returns raw values;
	 * @return
	 */
	public double getFrequencyOfRule(String ruleDescription, boolean normalized) {

		double counter = 0;
		double allRules = 0;
		boolean found = false;
		for (Entry<Rule, Double> rule : frequencyOfRules.entrySet()) {
			allRules += rule.getValue();
			if (!found) {
				if (rule.getKey().toString().contains(ruleDescription)) {
					counter = rule.getValue();
					found = true;
				}
			}

		}
		if (normalized) {
			return counter / allRules;
		} else {
			return counter;
		}

	}
}
