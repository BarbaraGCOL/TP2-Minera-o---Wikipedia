package br.com.ufmg.wikipedia.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/***
 * Classe responsavel por carregar as stopwords do arquivo
 * @author BárbaraGabrielle
 *
 */
public class StopWords
{
	Set<String> stopWords = new LinkedHashSet<String>();
	
	/***
	 * Le as stopwords do arquivo e salva em um Set
	 */
	private void leStopWords(){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("files/stop-word-list.txt"));
			for(String line;(line = br.readLine()) != null;)
				   stopWords.add(line.trim());
				 br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Set<String> getStopWords() {
		leStopWords();
		return stopWords;
	}

	public void setStopWords(Set<String> stopWords) {
		this.stopWords = stopWords;
	}
}