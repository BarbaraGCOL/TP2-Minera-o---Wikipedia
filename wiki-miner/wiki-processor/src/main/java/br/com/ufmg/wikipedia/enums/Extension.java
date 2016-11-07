package br.com.ufmg.wikipedia.enums;

public enum Extension {
	CSV(".csv"), ARFF(".arff"), TEXT(".text"), XML(".xml");
	
	public String extension;
	
	Extension (String extension){
		this.extension = extension;
	}
}
