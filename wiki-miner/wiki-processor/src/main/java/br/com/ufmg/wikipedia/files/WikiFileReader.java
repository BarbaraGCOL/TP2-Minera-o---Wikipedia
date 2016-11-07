package br.com.ufmg.wikipedia.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;
import br.com.ufmg.wikipedia.enums.Category;
import br.com.ufmg.wikipedia.enums.Extension;

public class WikiFileReader {

	Map<Long, Map<String, Double>> map = new HashMap<Long, Map<String, Double>>(); 

	Set<Integer> ids = new LinkedHashSet<Integer>();
	List<List<Double>> tf_idfs = new LinkedList<List<Double>>();
	Set<String> words = new LinkedHashSet<String>();

	public String read(File file) throws IOException{
		String text = "";
		BufferedReader br = new BufferedReader(new FileReader(file));

		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			
			while (line != null && !line.equals("See also") && !line.equals("External links")){
				sb.append(line);
				sb.append(System.lineSeparator());
				text += line + " ";
				line = br.readLine();
			}

			return text;

		} finally {
			br.close();
		}
	}
	
	public Map<Integer, String> readDictionary(){
		String filePath = "files/dictionary.csv", category;
		Integer id;
		Map<Integer, String> mapCategories = new LinkedHashMap<Integer, String>();

		CSVReader reader = null;
		try
		{
			//Get the CSVReader instance with specifying the delimiter to be used
			reader = new CSVReader(new FileReader(filePath),',');
			String [] nextLine;

			nextLine = reader.readNext();

			//Read one line at a time
			while ((nextLine = reader.readNext()) != null) 
			{
				id = Integer.parseInt(nextLine[0]);
				category = "";
				if(nextLine.length == 2){
					category = nextLine[1];
				}

				mapCategories.put(id, category);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return mapCategories;
	}
	
	public Map<Integer, Tuple> readProcessDictionary(String fileName){
		String filePath = "C:/Users/BárbaraGabrielle/workspace-mineracao/wiki-miner/wiki-processor/processed_files/"+fileName+Extension.CSV.extension;
		int instance;
		Map<Integer, Tuple> mapCategories = new LinkedHashMap<Integer, Tuple>();
		Tuple tuple;
		CSVReader reader = null;
		
		try
		{
			//Get the CSVReader instance with specifying the delimiter to be used
			reader = new CSVReader(new FileReader(filePath),',');
			String [] nextLine;

			nextLine = reader.readNext();
			nextLine = reader.readNext();
			
			//Read one line at a time
			while (nextLine != null) 
			{
				if(nextLine.length == 3){
					instance = Integer.parseInt(nextLine[0]);
					tuple = new Tuple(Long.parseLong(nextLine[1]),Category.fromString(nextLine[2]));
					mapCategories.put(instance, tuple);
//					System.out.println(instance+" => "+mapCategories.get(instance).getIdPage()+", "+mapCategories.get(instance).getCategory());
				}

				nextLine = reader.readNext();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return mapCategories;
	}
}
