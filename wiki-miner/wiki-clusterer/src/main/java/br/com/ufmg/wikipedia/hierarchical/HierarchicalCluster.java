package br.com.ufmg.wikipedia.hierarchical;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HierarchicalCluster {
	
	public static double[] printMatrix(double[][] mat, int index){
		double[] arr = new double[mat[0].length];
		
		for(int i = 0; i < mat.length; i++){
			for (int j = 0; j < mat[0].length; j++){
				if(i == index)
					arr[j] = mat[i][j];
					//System.out.print(mat[i][j] + "  |  ");
			}
			//System.out.println("\n");
		}
		return arr;
	}
	
	public static int[] removeDuplicates(int[] arr) {
		  Set<Integer> alreadyPresent = new HashSet<Integer>();
		  int[] whitelist = new int[0];

		  for (int nextElem : arr) {
		    if (!alreadyPresent.contains(nextElem)) {
		      whitelist = Arrays.copyOf(whitelist, whitelist.length + 1);
		      whitelist[whitelist.length - 1] = nextElem;
		      alreadyPresent.add(nextElem);
		    }
		  }

		  return whitelist;
		}
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		long total = 0;
		for (int i = 0; i < 10000000; i++) 
			total += i;
		double[][] d = ReadFile.run();
		//Dynamic array that avoids checking out the main matrix n * n times
		int[] dmin = new int[d.length];
		
		//int[] indexes = new int[]{157, 183, 318, 508, 1150, 1328};
		
		//for (int i = 0; i < indexes.length; i++) {
			
		
		//double[] arr = HierarchicalCluster.printMatrix(d, indexes[i]);
		
		//for(int j = 0; j < indexes.length; j++)
			//System.out.println(arr[indexes[j]]);
		
		//System.out.println("\n");
		//}
		
		ArrayList<Integer> count = new ArrayList<Integer>();
		
		double INFINITY = Double.POSITIVE_INFINITY;
		
		for (int i = 0; i < d[0].length; i++) {
			for (int j = 0; j < d[0].length; j++) {
				if (d[i][j] < d[i][dmin[i]]) dmin[i] = j;
			}
		}
		
		ArrayList<String> al = new ArrayList<String>();
		//printMatrix(d);
		
		Writer w = null;
		
		try{
			w = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("src/clusters"), "utf-8"));

			for (int s = 0; s < d[0].length-1; s++) {
				// find closest pair of clusters (i1, i2)
				int i1 = 0;
				for (int i = 0; i < d[0].length; i++)
					if (d[i][dmin[i]] < d[i1][dmin[i1]]) i1 = i;
				int i2 = dmin[i1];
				// overwrite row i1 with minimum of entries in row i1 and i2
				for (int j = 0; j < d[0].length; j++)
					if (d[i2][j] < d[i1][j]) d[i1][j] = d[j][i1] = d[i2][j];
				d[i1][i1] = INFINITY;
				// infinity-out old row i2 and column i2
				for (int i = 0; i < d[0].length; i++)
					d[i2][i] = d[i][i2] = INFINITY;
				// update dmin and replace ones that previous pointed to
				// i2 to point to i1
				for (int j = 0; j < d[0].length; j++) {
					if (dmin[j] == i2) dmin[j] = i1;
					if (d[i1][j] < d[i1][dmin[i1]]) dmin[i1] = j;
				}
				count.add(i1);
				al.add(Integer.toString(i1) + " " + Integer.toString(i2));
			}
			
			int[] elem = new int[count.size()];
			
			for(int i = 0; i < elem.length; i++)
				elem[i] = count.get(i);
			
			int[] countElements = new int[5];
			
			int aux = elem[0];
			int c = 1;
			
			for(int i = 1, k = 0; i < elem.length; i++)
				if(elem[i] == aux)
					c++;
				else {
					countElements[k] = c;
					k++;
					c = 0;
					aux = elem[i];
					c++;
				}
				
			for(int i : countElements)
				System.out.println(i);
			
			int[] nonDup = HierarchicalCluster.removeDuplicates(elem);
			
			//for(int i : nonDup)
				//System.out.println(i);

			for(String s : al){
				//System.out.print(s + " ");
				w.write(s + " ");
				//System.out.println("\n");
				w.write(System.getProperty("line.separator"));
			}
		
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			System.out.println("Temporal Complexity:" + elapsedTime);
			
		} catch (IOException ex) {
			// report
		} finally {
			try {w.close();} catch (Exception ex) {/*ignore*/}
		}
	}
}
