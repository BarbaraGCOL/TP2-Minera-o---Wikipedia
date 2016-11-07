package br.com.ufmg.wikipedia.processor;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * Standford Named Entity Demo
 * @author Ganesh
 */
public class StanfordNER
{
	private static String serializedClassifier = 
			"classifiers\\english.conll.4class.distsim.crf.ser.gz";
	
	private CRFClassifier<CoreLabel> classifier;
	
	public StanfordNER(){
		classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	}
	
	/**
	 * identify Name,organization location etc entities and return Map<List>
	 * @param text -- data
	 * @param model - Stanford model names out of the three models
	 * @return
	 */
	public List<String> identifyNER(String text)
	{
		List<List<CoreLabel>> classify = classifier.classify(text);
		List<String> entities = new ArrayList<String>();
		
		CoreLabel coreLabel = classify.get(0).get(0);
		
//		for (List<CoreLabel> coreLabels : classify)
//		{
//			for (CoreLabel coreLabel : coreLabels)
//			{

				String word = coreLabel.word();
				String category = coreLabel.get(CoreAnnotations.AnswerAnnotation.class);
				if(!"O".equals(category))
				{
					if(!entities.contains(category))
					{
						entities.add(word);
					}
				}
//			}
//		}
		return entities;
	}
}