package br.com.ufmg.wikipedia.hierarchical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFile {
	
	public static double calculateStandardDeviation(String[] arr2){
		float[] arr = new float[arr2.length];
		
		for(int i = 0; i < arr.length; i++)
			arr[i] = Float.parseFloat(arr2[i]);
		
		float sum = 0, average = 0, v = 0;
		double variation = 0, sumVar = 0, averageVar = 0;
		
		for(int i = 0; i < arr.length; i++)
			sum+=arr[i];
		
		average = sum/arr.length;
		
		for(int i = 0; i < arr.length; i++){
			v = arr[i] - average;
			variation = Math.pow(v, 2);
			sumVar+=variation;
		}
		averageVar = sumVar/arr.length;
		
		return Math.sqrt(averageVar);
	}
	
	public static double[][] pearsonSimilarity(ArrayList<Double> al){
		double[][] mat = new double[al.size()][al.size()];
		double r = 0;
		
		for(int i = 0; i < mat.length; i++){
			for(int j = 0; j < mat[i].length; j++){
				if(i == j)
					mat[i][j] = Double.POSITIVE_INFINITY;
				else{
					r = al.get(i) * al.get(j);
					mat[i][j] = r;
				}
			}
		}
			
		return mat;	
	}
	
	public static double[][] run() {

		String csvFile = "bagOfWords.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		float sum = 0;
		float average = 0;
		//ArrayList<Double> allAverages = new Double<Float>();
		//ArrayList<Double> allSV = new ArrayList<Double>();
		ArrayList<Double> pearson = new ArrayList<Double>(); 
		
		double summingUP = 0;
		double sd = 0;

		try {
			Scanner scanner = new Scanner(new File("bagOfWords.csv"));
			scanner.useDelimiter(",");

			String x=scanner.nextLine();
			//String[] arr=x.split(",");
			//Data Section
			int y = 1;
			while(scanner.hasNext()){
				x=scanner.nextLine();
				String[] arr=x.split(",");
				for(int i = 0; i < arr.length; i++)
					sum+=Float.parseFloat(arr[i]);
				
				//allSV.add(ReadFile.calculateStandardDeviation(arr));
				average = sum/arr.length;
				sd = ReadFile.calculateStandardDeviation(arr);
				
				for(int i = 0; i < arr.length; i++){
					summingUP+=(Float.parseFloat(arr[i]) - average)/sd;
				}
				pearson.add(summingUP);
				sum = 0;
				summingUP = 0;
				y++;
				//System.out.println(x);
			}
			
			//Average of all genes combined
			//float generalAverage, sumAll = 0;
			
			//for(float f : allAverages){
				//sumAll+=f;
			//}
			
			//generalAverage = sumAll/allAverages.size();
			//System.out.println(generalAverage);
			
			//double result = 0;
			
			//for(int i = 0; i < allAverages.size(); i++){
				//result = (allAverages.get(i) - generalAverage) / allSV.get(i);
				//pearson.add(result);
			//}
			
			scanner.close();
			//return mat;
			//System.out.println("The file was read correctly!!!");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		double[][] mat = ReadFile.pearsonSimilarity(pearson);
		return mat;
	}
}
