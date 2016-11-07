package br.com.ufmg.wikipedia.analyser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import br.com.ufmg.wikipedia.enums.Category;
import br.com.ufmg.wikipedia.enums.Extension;
import br.com.ufmg.wikipedia.files.Tuple;
import br.com.ufmg.wikipedia.files.WikiFileReader;

/**
 * Class to generate *.csv file from int[][]matrix
 * @author barbara.lopes
 *
 */
public class ResultWriter
{
	
	public void saveEvaluation(String evaluation, String fileName) 
            throws IOException{
        FileWriter writer = new FileWriter(fileName);
        writer.append(evaluation);
        writer.flush();
        writer.close();
    }
    
	public void generateCsvMap(String fileName, HashMap<Integer, List<Integer>> map) 
            throws IOException{
        FileWriter writer = new FileWriter(fileName);
        for(List<Integer> cluster: map.values()){
            for(int i: cluster){
                writer.append(i+"");
                writer.append(';');
            }
            writer.append("\n");
            writer.flush();
        }
        writer.close();
    }
	
	public void saveEvaluation(String evaluationPath, HashMap<Integer, List<Integer>> map) throws IOException{
		
		List<Result> evaluationResults = calculateClustersEvaluation("bagOfWords_dictionary", map);
		FileWriter writer = new FileWriter(evaluationPath + Extension.TEXT.extension);
		Set<Category> maxProportionCategories;
		int i = 0;
		
		for(Result result: evaluationResults){
			
			writer.append("Cluster " + i);
			writer.append("\n");
			writer.append("\n");
			maxProportionCategories = result.getMaxProportionCategories();

			for(Category c: maxProportionCategories){
				writer.append("purity("+c.name()+") = " + result.getPurity());
				writer.append("\n");
			}
			
			writer.append("\n");
			writer.append("entropy(Cluster " + i + ") = " + result.getEntropy());
			writer.append("\n");
			writer.append("max entropy = " + result.getMaxEntropy());
			writer.append("\n");
			writer.append("entropy(%) = " + result.getPercentEntropy());
			writer.append("\n");
			writer.append("\n");
			writer.append("\n");
			
	        writer.flush();
	        i++;
		}
		writer.close();
		
		Evaluation evaluation = new Evaluation();
		evaluation.setResults(evaluationResults);
		
		jaxbObjectToXML(evaluationPath, evaluation);
	}
	
	private List<Result> calculateClustersEvaluation(String dictionaryPath, HashMap<Integer, List<Integer>> map){
		
		WikiFileReader reader = new WikiFileReader();
		Map<Integer, Tuple> dictionary = reader.readProcessDictionary(dictionaryPath);
		List<Result> results = new LinkedList<Result>();
		Result result;
		Map<Category, Integer> categoriesDistribution;
		Tuple tuple;
		List<Integer> instances;
		Category category; 
		Set<Category> maxOccurrenceCategories;
		int maxOccurrence = 0, occurrences, distinctCategories;
		double purity, entropy, proportion, totalInstances, maxEntropy, entropyPercent;
		
		for(Integer cluster: map.keySet()){
			
			categoriesDistribution = new HashMap<Category, Integer>();
			
			result = new Result();
			result.setIdCluster(cluster);
			
			instances = map.get(cluster);
			
			maxOccurrence = 0;
			entropy = 0;
			maxEntropy = 0;
			
			for(Integer instance: instances){
				tuple = dictionary.get(instance);
				category = tuple.getCategory();
				
				if(!categoriesDistribution.containsKey(category)){
					categoriesDistribution.put(category, 1);
				}
				else{
					categoriesDistribution.put(category, (categoriesDistribution.get(category)+1));
				}
			}
			
			result.setCategoriesDistribution(categoriesDistribution);
			maxOccurrenceCategories = new HashSet<Category>();
			totalInstances = (double)instances.size();
			
			for(Category c: categoriesDistribution.keySet()){
				occurrences = categoriesDistribution.get(c);
				
				if(occurrences == maxOccurrence){
					maxOccurrenceCategories.add(c);
				}
				else
					if(occurrences > maxOccurrence){
						maxOccurrenceCategories = new HashSet<Category>();
						maxOccurrenceCategories.add(c);
						maxOccurrence = occurrences;
					}
				proportion = occurrences/totalInstances;
				entropy += (-(proportion) * log2(proportion));
			}
			
			distinctCategories = categoriesDistribution.size();
			proportion = 1/(double)distinctCategories;
			
			for(int i = 0; i < distinctCategories; i++){
				maxEntropy += (-(proportion) * log2(proportion));
			}
			
			result.setMaxEntropy(maxEntropy);
			
			entropyPercent = (entropy * 100)/maxEntropy;
			
			result.setPercentEntropy(entropyPercent);
			result.setEntropy(entropy);
			result.setMaxProportionCategories(maxOccurrenceCategories);
			
			purity = maxOccurrence/totalInstances;
			
			result.setPurity(purity);
			
			results.add(result);
		}
		
		return results;
	}
	
	private double log2(double n){
		return Math.log10(n) / Math.log10(2.);
	}
	
	private static void jaxbObjectToXML(String outName, Evaluation evaluation) {

        try {
            JAXBContext context = JAXBContext.newInstance(Evaluation.class);
            Marshaller m = context.createMarshaller();
            //for pretty-print XML in JAXB
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Write to System.out for debugging
            // m.marshal(emp, System.out);

            // Write to File
            m.marshal(evaluation, new File(outName + Extension.XML.extension));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}