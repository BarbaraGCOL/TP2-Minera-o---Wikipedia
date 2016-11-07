package br.com.ufmg.wikipedia.kmeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import br.com.ufmg.wikipedia.analyser.ResultWriter;
import br.com.ufmg.wikipedia.enums.Extension;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/**
 * Class for Clustering
 * @author barbara.lopes
 *
 */
public class Cluster {
		
	private static String csv_path = "files/", arff_path = "arff_files/", 
			outs_kmeans_folder = "outs_kmeans/", file;
	private static HashMap<Integer, List<Integer>> map;
	
    /**
     * Read file
     * @param filename
     * @return
     */
    public static BufferedReader readDataFile(String filename) {
        BufferedReader datafile = null;

        try {
            datafile = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }

        return datafile;
    }

    /**
     * Class for Clustering using K-Means
     * @param arffFile
     * @throws Exception
     */
    public static void runKMeans(String name, int k) throws Exception {
    	
    	file = name;
    	
        BufferedReader datafile = null;

        datafile = readDataFile(arff_path+file+Extension.ARFF.extension);

        SimpleKMeans kmeans = new SimpleKMeans();

        kmeans.setSeed(10);

        //important parameter to set: preserver order, number of cluster.
        kmeans.setPreserveInstancesOrder(true);
        kmeans.setNumClusters(k);

        Instances data = new Instances(datafile);
        
        DistanceFunction m_DistanceFunction = new EuclideanDistance();//new ManhattanDistance();

        kmeans.setDistanceFunction(m_DistanceFunction);

        kmeans.setMaxIterations(data.size()*10);

        kmeans.buildClusterer(data);

        // This array returns the cluster number (starting with 0) for each instance
        // The array has as many elements as the number of instances
        int[] assignments = kmeans.getAssignments();

        map = new HashMap<Integer, List<Integer>>();
        
        int i=0;
        for(int clusterNum : assignments) {
            if(map.containsKey(clusterNum)){
                map.get(clusterNum).add(i);
            }
            else{
                List<Integer>values = new ArrayList<Integer>();
                values.add(i);
                map.put(clusterNum, values);
            }
            i++;
        }
        
        // evaluate clusterer
        ClusterEvaluation eval = new ClusterEvaluation();
        eval.setClusterer(kmeans);
        eval.evaluateClusterer(data);
        
        ResultWriter writer = new ResultWriter();
        writer.saveEvaluation(eval.clusterResultsToString(), 
        		outs_kmeans_folder + "kmeans_evaluation_" + file + "_k" + k + Extension.TEXT.extension);
    }

    public static void runEM(String file) throws Exception {
        BufferedReader datafile = null;

        datafile = readDataFile(arff_path+file+Extension.ARFF.extension);

        String[] options = new String[2];
        options[0] = "-I";                 // max. iterations
        options[1] = "100";
        EM clusterer = new EM();   // new instance of clusterer
        clusterer.setOptions(options);     // set the options
        
        Instances data = new Instances(datafile);
        
        clusterer.buildClusterer(data);    // build the clusterer

        System.out.println(clusterer.toString());
    }
    
    private static void saveClusters(HashMap<Integer, List<Integer>> map, String fileKmeans) throws IOException{

        String pathOut = outs_kmeans_folder+"clusters_" + fileKmeans + Extension.CSV.extension;
        
        ResultWriter csvGenerator = new ResultWriter();
        csvGenerator.generateCsvMap(pathOut, map);
        System.out.println("Arquivo "+pathOut+" salvo com sucesso!!!");
    }
    
    public static void saveArff(String file) throws IOException{
    	
    	File csvFile = new File(csv_path+file+Extension.CSV.extension);
    	
    	// load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(csvFile);
        Instances data = loader.getDataSet();
     
        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(arff_path+file+Extension.ARFF.extension));
        saver.writeBatch();
        System.out.println("Arff Saved");
    }
    
    public static void main(String[] args) throws Exception {
    	
    	String file = "bagOfWords_wiki_files", fileKmeans;
    	
    	ResultWriter w = new ResultWriter();
    	
//    	System.out.println("start arff generation: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
//    	saveArff(file);
//    	System.out.println("end arff generation: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
    	
    	int k = 4;
    	
    	while(k <= 28){
    		fileKmeans = file+"_k"+k;
    		System.out.println("start kmeans k"+ k + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
    		Cluster.runKMeans(file, k);
    		System.out.println("end kmeans: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
    		/**
             * Salva clusters gerados
             */
            saveClusters(map, fileKmeans);
        	
        	w.saveEvaluation(outs_kmeans_folder + "evaluation_" + fileKmeans, map);
        	System.out.println("end process: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        	
        	k += 4;
    	}
    }
}