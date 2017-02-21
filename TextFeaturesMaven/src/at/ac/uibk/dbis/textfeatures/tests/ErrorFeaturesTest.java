package at.ac.uibk.dbis.textfeatures.tests;

import static org.junit.Assert.assertEquals;

import java.util.Map.Entry;

import org.junit.BeforeClass;
import org.junit.Test;
import org.languagetool.rules.Rule;

import at.ac.uibk.dbis.textfeatures.features.ErrorFeatures;
import at.ac.uibk.dbis.textfeatures.utilities.TextContainer;
import at.ac.uibk.dbis.textfeatures.utilities.Utility;

/**
 * @author Balthasar Huber
 * @version 1.0
 *
 */
public class ErrorFeaturesTest {
	
	static Utility ut;
	static TextContainer tc;
	static ErrorFeatures ef;
	static TextContainer tcShort;
	static ErrorFeatures efShort;
	static TextContainer tcError;
	static ErrorFeatures efError;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ut = new Utility();
		tc = new TextContainer(ut.readInRersource("resources/dalechallexample_5-6.txt"), false);
		tcShort = new TextContainer(ut.readInRersource("resources/textShort.txt"), false);
		tcError = new TextContainer(ut.readInRersource("resources/errortext.txt"), false);
		ef = new ErrorFeatures(tc, true);
		ef.calculateErrors();
		efShort = new ErrorFeatures(tcShort, true);
		efShort.calculateErrors();
		efError = new ErrorFeatures(tcError, true);
		efError.calculateErrors();
		
	
	
	}

	@Test
	public void testFrequencyOfErrorFeatures() {
		
		double counter = 0;
		for(Entry<Rule, Double> entry : efError.getFrequencyOfRules(true).entrySet()){
			counter+= entry.getValue();
		}
		assertEquals(1.0, counter, 0.0001);
	}
	
	@Test
	public void testFrequencyOfSuggestions() {
		
		double counter = 0;
		for(Entry<String, Double> entry : efError.getFrequencyOfSuggestions(true).entrySet()){
			counter+= entry.getValue();
		}
		assertEquals(1.0, counter, 0.0001);
	}
	
	

	@Test
	public void testGetFrequencyOfRule(){
		
		
		
		assertEquals(1.0, efError.getMultipleWhitespaceRule(false), 0.00001);
		assertEquals(1.0, efError.getFrequencyOfRule("MultipleWhitespace", false), 0.00001);
		assertEquals(1.0, efError.getFrequencyOfRule("UppercaseSentenceStartRule", false), 0.00001);
		assertEquals(0.0, efError.getAvsAnRule(true), 0.000000001);
		assertEquals(0.0, efError.getCompoundRule(false), 0.000000001);
		
	}
	


}
