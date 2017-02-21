package at.ac.uibk.dbis.textfeatures.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.languagetool.rules.Rule;
import edu.stanford.nlp.tagger.maxent.*;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;

/**
 * @author Balthasar Huber
 * @version 1.0
 *
 */
public class Utility {
	public static final String charsRegex = "";
	public static final String wordsRegex = "[^a-zA-Z0-9'’]+";

	/**
	 * Detects Sentences in a text file.
	 * @param filename
	 * @return Container, with 2 different Sentence Splits
	 */

	public static void main(String[] args) {
		
		
		
		BufferedReader txtReader = new BufferedReader(new InputStreamReader(Utility.class.getResourceAsStream("resources/dale-chall.txt")));
		try {
			System.out.println("buffered reader first line"+txtReader.readLine());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

	public String readInRersource(String path){
		
		BufferedReader txtReader = new BufferedReader(new InputStreamReader(Utility.class.getResourceAsStream(path)));
		String thisLine = null;
		StringBuilder builder = null;
		try {
			builder = new StringBuilder();
			 while ((thisLine = txtReader.readLine()) != null) {
		            builder.append(thisLine);
		            builder.append("\n");
			 }
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return builder.toString();
	}
	
	public SentenceSplitContainer sentDetect(String filename) {

		// option #1: By sentence.
		DocumentPreprocessor dp = new DocumentPreprocessor(filename);
		ArrayList<ArrayList<String>> sent = new ArrayList<>();
		ArrayList<List<HasWord>> sent2 = new ArrayList<>();
		for (List<HasWord> sentence : dp) {
			ArrayList<String> sentInner = new ArrayList<>();
			sent2.add(sentence);
			for (HasWord word : sentence) {
				sentInner.add(word.word());
			}
			sent.add(sentInner);

		}

		return new SentenceSplitContainer(sent, sent2);
	}
	
	public SentenceSplitContainer sentDetectString(String string) {

		// option #1: By sentence.
		StringReader reader = new StringReader(string);
		
		
		DocumentPreprocessor dp = new DocumentPreprocessor(reader);
		ArrayList<ArrayList<String>> sent = new ArrayList<>();
		ArrayList<List<HasWord>> sent2 = new ArrayList<>();
		for (List<HasWord> sentence : dp) {
			ArrayList<String> sentInner = new ArrayList<>();
			sent2.add(sentence);
			for (HasWord word : sentence) {
				sentInner.add(word.word());
			}
			sent.add(sentInner);

		}

		return new SentenceSplitContainer(sent, sent2);
	}
	
	

	/**Tokenizes a String into Chars
	 *
	 * @param text - as String 
	 * 
	 * @return tokenized text
	 */
	public ArrayList<String> tokenizeChars(String text) {

		
		ArrayList<String> tokList = new ArrayList<String>();

		String[] words = text.split("");
		for (int i = 0; i < words.length; i++) {
			if (true) {
				// words[i] = words[i].toLowerCase();

				tokList.add(words[i]);
			}

		}

		return tokList;

	}
	/**
	 * 
	 * @param x, value u want to calculate with
	 * @param base of the logarithm
	 * @return the result 
	 */
	public double LogBaseX(double x, double base) {
		return Math.log(x) / Math.log(base);
	}

	
	
	/**
	 * CAlculates the frequency of Strings in an ArrayList
	 * @param text - an ArrayList with Strings
	 * @return a sorted Map with K = String & V = frequency in text
	 */
	public Map<String, Double> frequencyOfWords(ArrayList<String> text) {

		HashMap<String, Double> map = new HashMap<String, Double>();
		for (String s : text) {
			if (!map.containsKey(s)) {
				map.put(s, 1.0);
			} else {
				map.put(s, map.get(s) + 1);
			}
		}

		return sortedByValuesDesc(map);

	}

	
	/**
	 * Calculates, how many times a String exactly occure in an ArrayList
	 * @param text
	 * @param numberOf - how many times the String occur exactly
	 * @return HashMap with String and Value("numberOf")
	 */
	public HashMap<String, Double> hapaxLegomenon(ArrayList<String> text, int numberOf) {

		// count words
		HashMap<String, Double> map = new HashMap<String, Double>();
		for (String s : text) {
			if (!map.containsKey(s)) {
				map.put(s, 1.0);
			} else {
				map.put(s, map.get(s) + 1);
			}
		}
		// only take words with # numberOf;
		HashMap<String, Double> hpMap = new HashMap<String, Double>();

		for (HashMap.Entry<String, Double> entry : map.entrySet()) {
			if (entry.getValue() == numberOf) {
				hpMap.put(entry.getKey(), entry.getValue());
			}
		}
		return hpMap;
	}
	/**
	 * Tokenizes a text with Stanford Parser
	 * @param filepath - filepath to the text file
	 * @return the tokenized text in an ArrayList
	 */
	public ArrayList<String> tokenize(String filepath) {
		PTBTokenizer<CoreLabel> ptbt = null;
		
		
		try {
			ptbt = new PTBTokenizer<>(new FileReader(filepath), new CoreLabelTokenFactory(), "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> chars = new ArrayList<String>();
		while (ptbt.hasNext()) {
			CoreLabel label = ptbt.next();
			chars.add(label.toString());

		}
		return chars;
	}
	
	public ArrayList<String> tokenizeString(String string){
		
		PTBTokenizer<CoreLabel> ptbt = null;
		StringReader reader = new StringReader(string);
		ptbt = new PTBTokenizer<>(reader, new CoreLabelTokenFactory(), "");
		ArrayList<String> chars = new ArrayList<String>();
		while (ptbt.hasNext()) {
			CoreLabel label = ptbt.next();
			chars.add(label.toString());

		}
		return chars;
		
		
		
	}
	
	/**
	 * Puts a POS-Tag to every Word in the text
	 * 
	 * @param sentences - has to be "ArrayList<List<HasWord>>" representation of sentences
	 * @return the tagged sentences
	 */
	public ArrayList<List<TaggedWord>> posTagging(ArrayList<List<HasWord>> sentences) {
		MaxentTagger tagger = PosTagger.getInstance();

		ArrayList<List<TaggedWord>> taggedSentences = new ArrayList<List<TaggedWord>>();
		for (List<HasWord> sentence : sentences) {
			taggedSentences.add(tagger.tagSentence(sentence));

		}

		return taggedSentences;
	}

	/**Read in a text file from a specific path
	 * @param path
	 * @return
	 */
	public String readIn(String path) {
		String fileName = path;
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toOneString(lines);
	}

	public String toOneString(List<String> lines) {
		StringBuilder builder = new StringBuilder();
		for (String s : lines) {
			builder.append(s);
		}

		return builder.toString();

	}

	/**An Algorithm that sorts a Map by Value Ascending
	 * @param map
	 * @return the sorted Map
	 */
	public <K, V extends Comparable<? super V>> Map<K, V> sortedByValuesAsc(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;

	}
	/**
	 * Filter out all punctuations of a text, so it only contains words
	 * @param tmp 
	 * @return the filtered text, containing only words
	 */
	public ArrayList<String> onlyWords(ArrayList<String> tmp) {
		ArrayList<String> text = new ArrayList<String>();
		// remove punctuations and make words lowercase for compare
		for (String w : tmp) {
			if (!w.matches("[^a-zA-Z0-9'’]+")) {
				if(!(w.equals("'")|| w.equals("’") || w.equals("\"") || w.equals("\''"))){
				text.add(w.toLowerCase());
				}

			}
		}
		return text;
	}
	/**
	 * Read a file and return it as a String
	 * @param fileName
	 * @return the file as String
	 * @throws IOException
	 */
	public String readFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}
	
	/**
	 * Normalize the Value of Map
	 * @param map
	 * @return the normalized map
	 */
	public Map<String, Double> normalizeFrequency(Map<String,Double> map){
		
		 double counter = 0;
		for(Entry<String, Double> entry : map.entrySet()){
				counter += entry.getValue();
		}
		Map<String, Double> normalizedMap = new HashMap<String, Double>();
		for(Entry<String, Double> entry : map.entrySet()){
			normalizedMap.put(entry.getKey(), (entry.getValue()/counter));
		}
		
		
		return normalizedMap;
		
	}
	/**
	 * Normalize the Value of Map
	 * @param map
	 * @return the normalized map
	 */
	public Map<ArrayList<String>, Double> normalizeFrequency2(Map<ArrayList<String>,Double> map){
		
		 double counter = 0;
		for(Entry<ArrayList<String>, Double> entry : map.entrySet()){
				counter += entry.getValue();
		}
		Map<ArrayList<String>, Double> normalizedMap = new HashMap<ArrayList<String>, Double>();
		for(Entry<ArrayList<String>, Double> entry : map.entrySet()){
			normalizedMap.put(entry.getKey(), (entry.getValue()/counter));
		}
		
		
		return normalizedMap;
		
	}
	
	/**
	 * Normalize the Value of Map
	 * @param map
	 * @return the normalized map
	 */
	public Map<Rule, Double> normalizeFrequency3(Map<Rule,Double> map){
		
		 double counter = 0;
		for(Entry<Rule, Double> entry : map.entrySet()){
				counter += entry.getValue();
		}
		Map<Rule, Double> normalizedMap = new HashMap<Rule, Double>();
		for(Entry<Rule, Double> entry : map.entrySet()){
			normalizedMap.put(entry.getKey(), (entry.getValue()/counter));
		}
		
		
		return normalizedMap;
		
	}

	
	/**An Algorithm that sorts a Map by Value descending
	 * @param map
	 * @return the sorted Map
	 */
	public <K, V extends Comparable<? super V>> Map<K, V> sortedByValuesDesc(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});

		
		
		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : sortedEntries) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
