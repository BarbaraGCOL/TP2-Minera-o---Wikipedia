package br.com.ufmg.wikipedia.processor;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import br.com.ufmg.wikipedia.enums.Category;
import br.com.ufmg.wikipedia.files.WikiFileReader;
import br.com.ufmg.wikipedia.files.WikiFileWriter;
import br.com.ufmg.wikipedia.preprocessor.Preprocessor;

public class Processor {

	private static WikiFileReader reader;
	private Stemmer stemmer;
	private StopWords stopWords;

	@SuppressWarnings("unused")
	private static String path = "wiki_files", test_path = "test_files", used_path;

	private Map<String, Integer> vocabulary;
	private Map<Integer, Map<String, Double>> frequencies;

	public Processor(){
		vocabulary = new HashMap<String, Integer>(); 
		frequencies = new LinkedHashMap<Integer, Map<String, Double>>();
		stemmer = new Stemmer();
		stopWords = new StopWords();
	}

	public static void main(String[] args) throws IOException{
		used_path = path;
		File file = new File(used_path);
		Processor processor = new Processor();
		processor.process(file);
	}

	public void process(File folder) throws IOException{

		reader = new WikiFileReader();
		File[] listOfFiles = folder.listFiles();
		String preprText, text;
		int count, countFiles=0;
		int idDoc;

		System.out.println("start: " + new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime()));

		Set<String> words;

		WikiFileReader reader = new WikiFileReader();
		Map<Integer, String> dictionary = reader.readDictionary();

		int sizeCategory;

		for (File file : listOfFiles) {
			countFiles++;
			if (file.isFile()) {
				idDoc = Integer.parseInt(file.getName());
				sizeCategory = dictionary.get(idDoc).length();
				
				if(sizeCategory <= 9){
					text = reader.read(file);
					Preprocessor preprocessor = new Preprocessor(text);

					text = null;

					preprText = preprocessor.getText();

					Map<String, Double> occurrences = 
							stemmer.splitWords(idDoc, preprText, stopWords.getStopWords());

					preprText = null;
					words = occurrences.keySet();

					for(String word: words){
						count = vocabulary.containsKey(word) ? vocabulary.get(word) : 0;
						vocabulary.put(word, count + 1);
					}

					frequencies.put(idDoc, occurrences);
					occurrences = null;
				}
			}
		}
		
		System.out.println("files: "+countFiles);
		
		System.out.println("end frequency: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
		stopWords = null;
		stemmer = null;
		reader = null;
		listOfFiles = null;

		processTdIdf();

		System.out.println("end process: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));

		WikiFileWriter csvFileWriter = new WikiFileWriter(frequencies, vocabulary);
		csvFileWriter.writeCsvFile("processed_files/bagOfWords_"+used_path+".csv");
		csvFileWriter.writeDictionaryCsvFile("processed_files/bagOfWords_dictionary_"+used_path+".csv");

		System.out.println("end csv save: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
	}

	private double log2(double num){
		double log2 = Math.log10(num)/Math.log10(2);
		return log2;
	}

	private void processTdIdf(){
		double tf, idf;
		int N = frequencies.size(), n;

		for(int id: frequencies.keySet()){
			Map<String, Double> docFrequencies = frequencies.get(id);
			for(String word: docFrequencies.keySet()){
				tf = docFrequencies.containsKey(word) ? 1 + log2(docFrequencies.get(word)) : 0;
				n = vocabulary.get(word);
				idf = log2(N/(double)n);

				docFrequencies.put(word, tf * idf);
			}
		}
	}

	@SuppressWarnings("unused")
	private void printTfsIdfs(){
		for(int id: frequencies.keySet()){
			System.out.println("Doc "+id);
			Map<String, Double> docTfs_idfs = frequencies.get(id);
			for(String w: docTfs_idfs.keySet()){
				System.out.println(w+": "+ docTfs_idfs.get(w));
			}
		}
	}
}
