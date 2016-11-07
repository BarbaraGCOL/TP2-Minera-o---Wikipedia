package br.com.ufmg.wikipedia.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tartarus.snowball.ext.EnglishStemmer;

/***
 * Classe responsavel por fazer o Stemmer dos textos
 * 
 * @author BárbaraGabrielle
 *
 */
public class Stemmer  {
	
	private Map<String, Double> ocurrences;
	
	private EnglishStemmer stemmer;
	private StanfordNER ner;
	
	public Stemmer(){
		ner = new StanfordNER();
		stemmer = new EnglishStemmer();
	}
	
	/***
	 * Separa os termos do texto, extraindo os radicais e indexando as ocorrencias
	 * @param pagina pagina da wikipedia
	 * @param stopWords lista de stopwords a serem ignoradas
	 * @param idx Index
	 */
	public Map<String, Double> splitWords(int id, String text, Set<String> stopWords){
		
		//Separa as palavras do texto do artigo
		String[] words = text.split(" ");
		ocurrences = new HashMap<String, Double>();
		String term;
		for(String word: words){
			if(word.length() > 1){
				term = word;
				if(!stopWords.contains(word)){
					term = extractRoot(word);
				}
				//Verifica se o Map de ocorrencias ja possui o termo
				if(!ocurrences.containsKey(term)){
					ocurrences.put(term, (double) 1);
				}
				else{
					ocurrences.put(term, (ocurrences.get(term)+1));
				}
			}
		}
		return ocurrences;
	}
	
	//Extrai o radical da palavra
	private String extractRoot(String word){
		List<String> ignore = ner.identifyNER(word);
		if(ignore.isEmpty()){
			stemmer.setCurrent(word);
			stemmer.stem();
			return stemmer.getCurrent();
		}
		return word;
	}
}
