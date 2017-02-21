/**
 * 
 */
package at.ac.uibk.dbis.textfeatures.tests;

import static org.junit.Assert.*;

//import java.io.File;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.uibk.dbis.textfeatures.utilities.SentenceSplitContainer;
import at.ac.uibk.dbis.textfeatures.utilities.TextContainer;
import at.ac.uibk.dbis.textfeatures.utilities.Utility;

/**
 * @author Balthasar Huber
 * @version 1.0
 *
 */
public class TextContainerTest {
	private static Utility ut;
	private static TextContainer tcLong;
	private static TextContainer tcShort;
	private static TextContainer tcString;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		ut = new Utility();
		tcLong =  new TextContainer(ut.readInRersource("resources/dalechallexample_5-6.txt"),false);
		tcShort =   new TextContainer(ut.readInRersource("resources/textShort.txt"),false);
		tcString =  new TextContainer("How are you \"foo-foo\"?",false);
	}

	@Test
	public void testFilePath() {

		
		assertEquals("How are you \"foo-foo\"?", tcString.getFilePath());

	}

	@Test
	public void testTextLenghtInWords() {

		assertSame(4, tcShort.getTextLengthInWords());
		assertSame(68, tcLong.getTextLengthInWords());
		assertSame(4, tcString.getTextLengthInWords());
	}

	@Test
	public void testTextLenghtInChars() {
		
		
		assertSame(23, tcShort.getTextLengthInChars());
		// assertSame(391, tcLong.getTextLengthInChars()); Does not work,
		// however...??!!!
		assertEquals(391, tcLong.getTextLengthInChars());
		assertSame(22, tcString.getTextLengthInChars());
		
	}

	@Test
	public void testTextLengthInTokens() {
		assertSame(7, tcShort.getTextLengthInTokens());
		assertSame(74, tcLong.getTextLengthInTokens());
		assertSame(7, tcString.getTextLengthInTokens());

	}

	@Test
	public void testNumberOfDifferentWords() {
		assertSame(4, tcShort.getNumberOfDifferentWords());
		assertSame(49, tcLong.getNumberOfDifferentWords());
		assertSame(4, tcString.getNumberOfDifferentWords());
	}

	@Test
	public void testTokenizedCharsList() {
		// testing if List only contains Strings of Length 1(characters)
		int counter = 0;
		for (String s : tcShort.getTokenizedChars()) {
			counter += s.length();
		}
		assertSame(1, counter / tcShort.getTokenizedChars().size());

		counter = 0;
		for (String s : tcLong.getTokenizedChars()) {
			counter += s.length();
		}
		assertSame(1, counter / tcLong.getTokenizedChars().size());
		
		counter = 0;
		for (String s : tcString.getTokenizedChars()) {
			counter += s.length();
		}
		assertSame(1, counter / tcString.getTokenizedChars().size());

	}

	@Test
	public void testTokenizedWordsList() {
		boolean containsWhitespace = false;
		for (String s : tcShort.getTokenizedWords()) {
			if (StringUtils.isBlank(s)) {
				containsWhitespace = true;
			}
		}
		assertFalse(containsWhitespace);
		
		
		
		containsWhitespace = false;
		for (String s : tcString.getTokenizedWords()) {
			if (StringUtils.isBlank(s)) {
				containsWhitespace = true;
			}
		}
		assertFalse(containsWhitespace);
	}

	@Test
	public void testSentenceSplit() {
		SentenceSplitContainer sentShort = tcShort.getSentSplitCont();
		SentenceSplitContainer sentLong = tcLong.getSentSplitCont();
		SentenceSplitContainer sentString = tcString.getSentSplitCont();

		assertSame(1, sentShort.getStringSplit().size());
		assertSame(1, sentShort.getHasWordSplit().size());

		assertSame(4, sentLong.getStringSplit().size());
		assertSame(4, sentLong.getHasWordSplit().size());
		
		assertSame(1, sentString.getStringSplit().size());
		assertSame(1, sentString.getHasWordSplit().size());
	}

	@Test
	public void testTaggedSentences() {
		assertSame(1, tcShort.getTaggedSentences().size());
		assertSame(4, tcLong.getTaggedSentences().size());
		assertSame(1, tcString.getTaggedSentences().size());
	}

	@Test
	public void testFrequencyOfWords() {

		for (Entry<String, Double> entry : tcShort.getFrequencyOfwords().entrySet()) {
			assertNotEquals(0, entry.getValue());
			assertFalse(StringUtils.isBlank(entry.getKey()));
		}

		for (Entry<String, Double> entry : tcLong.getFrequencyOfwords().entrySet()) {
			assertNotEquals(0, entry.getValue());
			assertFalse(StringUtils.isBlank(entry.getKey()));
		}
		for (Entry<String, Double> entry : tcString.getFrequencyOfwords().entrySet()) {
			assertNotEquals(0, entry.getValue());
			assertFalse(StringUtils.isBlank(entry.getKey()));
		}

		assertEquals(1.0, tcShort.getFrequencyOfwords().get("how"), 0);
		assertEquals(6.0, tcLong.getFrequencyOfwords().get("the"), 0);
		assertEquals(1.0, tcString.getFrequencyOfwords().get("how"), 0);
		

	}

	@Test
	public void testHapaxLegomenon() {
		for (Entry<String, Double> entry : tcShort.getHapaxLegomenon().entrySet()) {
			assertEquals(1.0, entry.getValue(), 0);
		}

		for (Entry<String, Double> entry : tcLong.getHapaxLegomenon().entrySet()) {
			assertEquals(1.0, entry.getValue(), 0);
		}
		
		for (Entry<String, Double> entry : tcString.getHapaxLegomenon().entrySet()) {
			assertEquals(1.0, entry.getValue(), 0);
		}

		assertSame(4, tcShort.getHapaxLegomenon().size());
		assertSame(37, tcLong.getHapaxLegomenon().size());
		assertSame(4, tcString.getHapaxLegomenon().size());

	}

	@Test
	public void testDislegomenon() {

		for (Entry<String, Double> entry : tcShort.getDislegomenon().entrySet()) {
			assertEquals(2.0, entry.getValue(), 0);
		}

		for (Entry<String, Double> entry : tcLong.getDislegomenon().entrySet()) {
			assertEquals(2.0, entry.getValue(), 0);
		}
		
		for (Entry<String, Double> entry : tcString.getDislegomenon().entrySet()) {
			assertEquals(2.0, entry.getValue(), 0);
		}

		assertSame(0, tcShort.getDislegomenon().size());
		assertSame(8, tcLong.getDislegomenon().size());
		assertSame(0, tcString.getDislegomenon().size());

	}

	@Test
	public void testOnlyWordsList() {

		ArrayList<String> listShort = tcShort.getOnlyWords();
		ArrayList<String> listLong = tcLong.getOnlyWords();
		ArrayList<String> listString = tcString.getOnlyWords();

		for (String s : listShort) {
			assertFalse(StringUtils.containsWhitespace(s));
			if (s.matches("[^a-z0-9'’-]")) {

				assertTrue(StringUtils.isAllLowerCase(s));

			}

		}
		for (String s : listLong) {
			assertFalse(StringUtils.containsWhitespace(s));
			if (s.matches("[^a-z0-9'’-]")) {
				assertTrue(StringUtils.isAllLowerCase(s));
			}
		}
		
		for (String s : listString) {
			assertFalse(StringUtils.containsWhitespace(s));
			if (s.matches("[^a-z0-9'’-]")) {

				assertTrue(StringUtils.isAllLowerCase(s));

			}

		}

	}

//	@Test
//	public void testTextAsString() {
//		String textShort = tcShort.getTextAsString();
//		String textLong = tcLong.getTextAsString();
//		String textString = tcString.getTextAsString();
//		int lengthShort = textShort.getBytes().length;
//		int lengthLong = textLong.getBytes().length;
//		int lengthString = textString.getBytes().length;
//
//		File fileShort = new File("src/at/ac/uibk/dbis/textfeatures/tests/testfiles/textShort.txt");
//		File fileLong = new File("src/at/ac/uibk/dbis/textfeatures/tests/testfiles/dalechallexample_5-6.txt");
//		
//
//		assertEquals(lengthShort, fileShort.length(), 1);
//		assertEquals(lengthLong, fileLong.length(), 1);
//		assertEquals(22, lengthString);
//
//	}
}
