package br.com.ufmg.wikipedia.preprocessor;

import java.util.regex.Pattern;

import org.jsoup.Jsoup;

/***
 * Classe respons·vel pelo processamento do texto do artigo
 * 
 * @author B·rbaraGabrielle
 *
 */
public class Preprocessor {
	
	private String text;
	private static Pattern objPonctuation;
	
	public Preprocessor(String text){
		this.text = text;
		htmlToText();
		removeNumbers();
		removeUrl();
		removePonctuation();
		removeBrackets();
		removeSpecialCharaters();
		removeDoubleBlankSpaces();
		removeBlankSpaces();
	}
	
	private void removeUrl()
	{
		String regularExpression = "(((http|ftp|https):\\/\\/)?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?)";
	    String str = text.replaceAll(regularExpression,"");
	    text = str;
	}
	
	public void removeDoubleBlankSpaces(){
		while(text.contains("  "))
		{
			text = text.replaceAll("  ", " ");
		}
	}
	
	public void removeBlankSpaces(){
		String tmp;
		while(text.startsWith(" ")){
			tmp = text.substring(1);
			text = tmp;
		}
	
		while(text.endsWith(" ")){
			tmp = text.substring(0, text.length()-1);
			text = tmp;
		}
	}
	
	/***
	 * Remove os caracteres especiais do texto 
	 */
	public void removeSpecialCharaters()
	{
		String temp = text;
		temp = temp.toLowerCase();
		temp = temp.replaceAll("[‚„·‡‰]","a");
		temp = temp.replaceAll("[ÈËÍÎ]","e");
		temp = temp.replaceAll("[ÌÏÓÔ]","i");
		temp = temp.replaceAll("[ÛÚÙıˆ]","o");
		temp = temp.replaceAll("[˚˙˘¸]","u");
		temp = temp.replaceAll("[-]"," ");

		this.text = temp;
	}
	
	public void removePonctuation()
	{
		if(objPonctuation == null)
		{
			objPonctuation = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		}
		
		this.text = objPonctuation.matcher(this.text).replaceAll(" ");
	}
	
	public void removeBrackets(){
		this.text = this.text.replaceAll("\\p{P}","");
	}
	
	public void removeNumbers(){
		String tmp = text.replaceAll("\\d","");
		this.text = tmp;
	}
	
	/***
	 * Converte o texto HTML para texto
	 */
	public void htmlToText() {
	    this.text = Jsoup.parse(this.text).text();
	}
	
	public String getPreprocessedText() {
		return text;
	}

	public String getText() {
		return this.text;
	}
}
