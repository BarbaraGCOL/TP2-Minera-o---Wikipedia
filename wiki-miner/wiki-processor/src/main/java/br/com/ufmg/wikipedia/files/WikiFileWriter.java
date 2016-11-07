package br.com.ufmg.wikipedia.files;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WikiFileWriter {
	
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	private Map<Integer, Map<String, Double>> frequencies;
	private Map<String, Double> pageFrequencies;
	private Map<String, Integer> vocabulary;
	private List<String> relevantVocabulary;
	private Map<Integer, String> dictionary;
	
	public WikiFileWriter(Map<Integer, Map<String, Double>> frequencies, Map<String, Integer> vocabulary){
		this.frequencies = frequencies;
		this.vocabulary = vocabulary;
		relevantVocabulary = new LinkedList<String>(); 
		for(String word: vocabulary.keySet()){
			if(vocabulary.get(word) > 5 && vocabulary.get(word) < 3294){
				relevantVocabulary.add(word);
			}
		}
		System.out.println("columns: "+relevantVocabulary.size());
		WikiFileReader reader = new WikiFileReader();
		dictionary = reader.readDictionary();
	}
	
	public void writeQueryCsvFile(String fileName){
		FileWriter fileWriter = null;
		
		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append("create table bagOfWords (pageId integer");
			for(String word: vocabulary.keySet()){
				if(vocabulary.get(word) > 1){
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(word+" double");
				}
			}
			fileWriter.append(");");
			
			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
	
	public void writeDictionaryCsvFile(String fileName) {
		FileWriter fileWriter = null;
		
		try {
			fileWriter = new FileWriter(fileName);
			
			//Write the CSV file header
			fileWriter.append("instance");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append("pageId");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append("category");
			
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			int id = 0;
			
			//Write a new student object list to the CSV file
			for (int idPage: frequencies.keySet()) {
				fileWriter.append(String.valueOf(id));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(idPage));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(dictionary.get(idPage));
				fileWriter.append(NEW_LINE_SEPARATOR);
				
				id++;
			}
			
			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}	
	}

	public void writeCsvFile(String fileName) {
		
		FileWriter fileWriter = null;
				
		try {
			fileWriter = new FileWriter(fileName);
			
			//Write the CSV file header
			fileWriter.append(relevantVocabulary.get(0));
			
			for(int i = 1; i < relevantVocabulary.size();i++){
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(relevantVocabulary.get(i));
			}
			
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			String word;
			Set<Integer> pages = frequencies.keySet();
			
			//Writ a new student object list to the CSV file
			for (int idPage: pages) {
				
				pageFrequencies = frequencies.get(idPage);
				word = relevantVocabulary.get(0);
				
				if(pageFrequencies.containsKey(word)){
					fileWriter.append(pageFrequencies.get(word)+"");
				}
				else{
					fileWriter.append(0+"");
				}
				
				for(int i = 1; i < relevantVocabulary.size(); i++){
					fileWriter.append(COMMA_DELIMITER);
					word = relevantVocabulary.get(i);
					if(pageFrequencies.containsKey(word)){
						fileWriter.append(pageFrequencies.get(word)+"");
					}
					else{
						fileWriter.append(0+"");
					}
				}
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
			
			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
		
	public void writeCsvFileJoin(String fileName) {
		
		FileWriter fileWriter = null;
				
		try {
			fileWriter = new FileWriter(fileName);
			
			//Write the CSV file header
			fileWriter.append("pageId");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append("category");
			
			for(String word: relevantVocabulary){
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(word);
			}
			
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			
			//Write a new student object list to the CSV file
			for (int idPage: frequencies.keySet()) {
				pageFrequencies = frequencies.get(idPage);
				
				fileWriter.append(String.valueOf(idPage));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(dictionary.get(idPage));
				
				for(String word: relevantVocabulary){
					fileWriter.append(COMMA_DELIMITER);
					if(pageFrequencies.containsKey(word)){
						fileWriter.append(pageFrequencies.get(word)+"");
					}
					else{
						fileWriter.append(0+"");
					}
				}
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
			
			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
}