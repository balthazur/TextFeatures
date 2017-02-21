package at.ac.uibk.dbis.textfeatures.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.uibk.dbis.textfeatures.features.SyntacticalFeatures;
import at.ac.uibk.dbis.textfeatures.utilities.TextContainer;
import at.ac.uibk.dbis.textfeatures.utilities.Utility;
/**
 * @author Balthasar Huber
 * @version 1.0
 *
 */
public class SyntacticalFeaturesTest {
	static Utility ut;
	static TextContainer tc;
	static SyntacticalFeatures sf;
	static TextContainer tcShort;
	static SyntacticalFeatures sfShort;

	@BeforeClass
	public static void setUp() throws Exception {
		
		ut = new Utility();
		tc =   new TextContainer(ut.readInRersource("resources/dalechallexample_5-6.txt"),false);
		tcShort =   new TextContainer(ut.readInRersource("resources/textShort.txt"),false);
		sf = new SyntacticalFeatures(tc);
		sfShort = new SyntacticalFeatures(tcShort);

	}

	@Test
	public void testFrequencyOfTags() {

		double before = 10000000000.0;
		for (Entry<String, Double> entry : sf.frequencyOfTags(false).entrySet()) {
			assertTrue(before >= entry.getValue());
			before = entry.getValue();
		}

		before = 10000000000.0;
		for (Entry<String, Double> entry : sfShort.frequencyOfTags(false).entrySet()) {
			assertTrue(before >= entry.getValue());
			before = entry.getValue();
		}

		double counter = 0;
		for (Entry<String, Double> entry : sf.frequencyOfTags(true).entrySet()) {
			counter += entry.getValue();
		}

		assertEquals(1.0, counter, 0.05);

		counter = 0;
		for (Entry<String, Double> entry : sfShort.frequencyOfTags(true).entrySet()) {
			counter += entry.getValue();
		}
		assertEquals(1.0, counter, 0.05);

	}

	@Test
	public void testFrequencyOfNGrams() {

		double counter = 0;
		for (Entry<ArrayList<String>, Double> entry : sf.nGramOfTags(3, true).entrySet()) {
			assertEquals(3.0, entry.getKey().size(), 0.001);
			counter += entry.getValue();

		}
		assertEquals(1.0, counter, 0.005);

		double before = 10000000000.0;
		for (Entry<ArrayList<String>, Double> entry : sf.nGramOfTags(3, false).entrySet()) {
			assertTrue(before >= entry.getValue());
			before = entry.getValue();
		}

		assertEquals(0, sfShort.nGramOfTags(8, false).size(), 0);
		assertEquals(1.0, sfShort.nGramOfTags(7, false).size(), 0);

	}
	@Test(expected = IllegalArgumentException.class)
	public void testNGramException(){
		sf.nGramOfTags(1, false);
	}



	@Test
	public void testFrequencyOfFunctionWords(){
		double before = 10000000000.0;
		for (Entry<String, Double> entry : sf.frequencyOfFuntionWords(false).entrySet()) {
			assertTrue(before >= entry.getValue());
			before = entry.getValue();
		}

		before = 10000000000.0;
		for (Entry<String, Double> entry : sfShort.frequencyOfFuntionWords(false).entrySet()) {
			assertTrue(before >= entry.getValue());
			before = entry.getValue();
		}

		double counter = 0;
		for (Entry<String, Double> entry : sf.frequencyOfFuntionWords(true).entrySet()) {
			counter += entry.getValue();
		}

		assertEquals(1.0, counter, 0.05);

		counter = 0;
		for (Entry<String, Double> entry : sfShort.frequencyOfFuntionWords(true).entrySet()) {
			counter += entry.getValue();
		}
		assertEquals(1.0, counter, 0.05);
		
		
	}
	
	@Test
	public void testAvgFunctionWordsPerSentence(){
		
		assertEquals(1.0,sfShort.avgFunctionWordsPerSentence(), 0.0001);
		assertEquals(5.5,sf.avgFunctionWordsPerSentence(), 0.001);
	
	}
	
	@Test
	public void testPunctuationWordRatio(){
		assertEquals(0.25, sfShort.punctuationWordRatio(), 0.001);
		assertEquals(0.08823, sf.punctuationWordRatio(), 0.005);
		
	}



}
