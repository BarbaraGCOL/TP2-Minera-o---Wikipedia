package br.com.ufmg.wikipedia.analyser;

import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import br.com.ufmg.wikipedia.enums.Category;

@XmlRootElement(name = "Result")
@XmlType(propOrder = {"idCluster", "countCategories", "categoriesDistribution", "maxProportionCategories", "purity", "entropy", "percentEntropy", "maxEntropy"})
public class Result {
	
	private int idCluster, countCategories;
	private Map<Category, Integer> categoriesDistribution;
	private Set<Category> maxProportionCategories;
	private double purity, entropy, percentEntropy, maxEntropy;

	public int getIdCluster() {
		return idCluster;
	}
	public void setIdCluster(int idCluster) {
		this.idCluster = idCluster;
	}
	public Map<Category, Integer> getCategoriesDistribution() {
		return categoriesDistribution;
	}
	public void setCategoriesDistribution(Map<Category, Integer> categoriesDistribution) {
		this.categoriesDistribution = categoriesDistribution;
		this.countCategories = categoriesDistribution.size();
	}
	public double getPurity() {
		return purity;
	}
	public void setPurity(double purity) {
		this.purity = purity;
	}
	public double getEntropy() {
		return entropy;
	}
	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}
	public Set<Category> getMaxProportionCategories() {
		return maxProportionCategories;
	}
	public void setMaxProportionCategories(Set<Category> maxProportionCategories) {
		this.maxProportionCategories = maxProportionCategories;
	}
	public double getPercentEntropy() {
		return percentEntropy;
	}
	public void setPercentEntropy(double percentEntropy) {
		this.percentEntropy = percentEntropy;
	}
	public double getMaxEntropy() {
		return maxEntropy;
	}
	public void setMaxEntropy(double maxEntropy) {
		this.maxEntropy = maxEntropy;
	}
	public int getCountCategories() {
		return countCategories;
	}
	public void setCountCategories(int countCategories) {
		this.countCategories = countCategories;
	}
}
