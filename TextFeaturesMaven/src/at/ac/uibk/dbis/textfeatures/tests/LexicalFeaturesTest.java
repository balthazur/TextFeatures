package at.ac.uibk.dbis.textfeatures.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.uibk.dbis.textfeatures.features.LexicalFeatures;
import at.ac.uibk.dbis.textfeatures.utilities.TextContainer;
import at.ac.uibk.dbis.textfeatures.utilities.Utility;
/**
 * @author Balthasar Huber
 * @version 1.0
 *
 */
public class LexicalFeaturesTest {
	static Utility ut;
	static TextContainer tc;
	static LexicalFeatures lf;
	static TextContainer tcShort;
	static LexicalFeatures lfShort;
	static TextContainer tcLong;
	static LexicalFeatures lfLong;
	
	

	@BeforeClass
	public static void setUp() throws Exception {
		
		ut = new Utility();
		tc =  new TextContainer(ut.readInRersource("resources/dalechallexample_5-6.txt"),false);
		tcShort =   new TextContainer(ut.readInRersource("resources/textShort.txt"),false);
		tcLong =   new TextContainer(ut.readInRersource("resources/textLong.txt"),false);
		lf = new LexicalFeatures(tc);
		lfShort = new LexicalFeatures(tcShort);
		lfLong = new LexicalFeatures(tcLong);
		

	}

	@Test
	public void testFrequencyOfChars() {

		assertEquals(58.0, lf.frequencyOfChars(false, false).get(" "), 0);
		assertEquals(4.0, lf.frequencyOfChars(false, false).get("S"), 0);
		assertEquals(23.0, lf.frequencyOfChars(false, true).get("s"), 0);

		double counter = 0;
		for (Entry<String, Double> entry : lf.frequencyOfChars(true, true).entrySet()) {
			counter += entry.getValue();
		}
		assertEquals(1.0, counter, 0.1);

	}

	@Test(expected = java.lang.NullPointerException.class)
	public void testFrequencyOfCharsException() {
		assertEquals(0, lf.frequencyOfChars(false, true).get("S"), 0);
	}

	@Test
	public void testFrequencyOfSpecialChars() {

		Map<String, Double> map = lf.frequencyOfSpecialChars(false);

		assertEquals(4.0, map.get("."), 0);
		assertEquals(1.0, map.get("'"), 0);
		assertEquals(1.0, map.get(","), 0);

		double counter = 0;
		for (Entry<String, Double> entry : lf.frequencyOfSpecialChars(true).entrySet()) {
			counter += entry.getValue();
		}

		assertEquals(1.0, counter, 0.1);

	}

	@Test
	public void testFrequencyOfWords() {
		Map<String, Double> map = lf.frequencyOfWords(false);
		assertEquals(6.0, map.get("the"), 0);
		assertEquals(1.0, map.get("80-footlong"), 0);
		assertEquals(3.0, map.get("eskimos"), 0);

		double counter = 0;
		for (Entry<String, Double> entry : lf.frequencyOfWords(true).entrySet()) {
			counter += entry.getValue();
		}

		assertEquals(1.0, counter, 0.1);
	}

	@Test
	public void testAvgWordLength() {

		assertEquals(4.661764, lf.avgWordLength(), 0.1);
	}

	@Test
	public void testAvgWordsPerSentence() {
		assertEquals(17.0, lf.avgWordsPerSentence(), 0);
	}

	@Test
	public void testAvgSyllablesPerSentence() {
		assertEquals(27.5, lf.avgSyllablesPerSentence(), 5);
	}

	@Test
	public void testWordNGrams() {
		assertEquals(2.0, lfShort.wordNGrams(false, 3).size(), 0);
		assertEquals(3.0, lfShort.wordNGrams(false, 2).size(), 0);

		double counter = 0;
		for (Entry<ArrayList<String>, Double> entry : lfShort.wordNGrams(true, 3).entrySet()) {
			counter += entry.getValue();
		}
		assertEquals(1.0, counter, 0.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentOnWordNGrams() {
		lfShort.wordNGrams(false, 1);
	}

	@Test
	public void testCharNGrams() {
		double counter = 0;
		for (Entry<ArrayList<String>, Double> entry : lfShort.charsNGrams(true, 3).entrySet()) {
			counter += entry.getValue();
		}
		assertEquals(1.0, counter, 0.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentOnCharNGrams() {
		lfShort.charsNGrams(false, 1);
	}

	@Test
	public void testHapaxLegomenon() {

		assertEquals(4.0, lfShort.numberOfHapaxLegomena(), 0);
		assertEquals(37, lf.numberOfHapaxLegomena(), 0);

	}

	@Test
	public void testDislegomenon() {

		assertEquals(8, lf.numberOfHapaxDislegomena(), 0);
		assertEquals(0, lfShort.numberOfHapaxDislegomena(), 0);

	}

	
	@Test
	public void testIndexOfDiversity(){
		assertEquals(0.544117647, lf.indexOfDiversity(), 0.001);
		assertEquals(1.0,lfShort.indexOfDiversity(),0);
	}
	
	@Test
	public void testHonoreMeasure(){
		assertEquals(562.642, lf.honoreMeasure(), 0.05);
		
	}
	@Test(expected = IllegalArgumentException.class)
	public void testHonoreMeasureException(){
		lfShort.honoreMeasure();
	}
	
	@Test
	public void testSichelMeasure(){
	
		assertEquals(0.1632, lf.sichelMeasure(), 0.005);
		assertEquals(0,lfShort.sichelMeasure(), 0);
	}
	@Test
	public void testFleschKincaid(){
		
		assertEquals(60.1917, lf.fleschKincaidGrade(), 0.005);
		assertEquals(54.725, lfShort.fleschKincaidGrade(), 0.005);
	}
	
	@Test
	public void testGunningFog(){
		
		assertEquals(13.8588, lf.gunningFogIndex(),0.005);
		assertEquals(11.6, lfShort.gunningFogIndex(),0.005);
	}
	
	@Test
	public void testYuleK(){
		assertEquals(345.689, lf.yuleKMeasure(), 0.05);
		
		
		}
	@Test(expected= IllegalArgumentException.class)
	public void testYuleKException(){
		lfShort.yuleKMeasure();
	}
	
	@Test
	public void testDaleChall(){
		assertEquals(9.5, lf.daleChallIndex(), 1);
		assertEquals(7.7824, lfShort.daleChallIndex(), 0.1);
	}
	
	@Test
	public void testTypeTokenRatio(){
		assertEquals(0.7205, lf.typeTokenRatio(), 0.005);
		assertEquals(1.0, lfShort.typeTokenRatio(), 0.00001);
	}
	
	@Test public void testWordFrequencyClass(){
		assertEquals(2.2719, lf.avgWordFrequencyClass(), 0.005);
		
	}
	@Test(expected = IllegalArgumentException.class)
	public void testWordFrequencyClassException() {
	
	lfShort.avgWordFrequencyClass();
	
	}
	
	@Test
	public void testCompressionRate(){
		try {
			assertEquals(0.02518, lf.compressionRate(), 0.005);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test 
	public void testCompressionTime(){
		try {
			assertEquals(10.0, lfLong.compressionTime(100), 5.0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	



}
