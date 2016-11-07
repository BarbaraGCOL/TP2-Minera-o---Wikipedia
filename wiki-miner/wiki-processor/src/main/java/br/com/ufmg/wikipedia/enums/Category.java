package br.com.ufmg.wikipedia.enums;

public enum Category {
	HISTORYSCIENCE("HISTORYSCIENCE"), CULTUREHISTORYSCIENCE("CULTUREHISTORYSCIENCE"), 
	SCIENCE("SCIENCE"),  GEOGRAPHY("GEOGRAPHY"), NONE(""),
	CULTUREGEOGRAPHY("CULTUREGEOGRAPHY"), CULTURE("CULTURE"),
	CULTUREGEOGRAPHYHISTORYSCIENCE("CULTUREGEOGRAPHYHISTORYSCIENCE"),
	CULTUREHISTORY("CULTUREHISTORY"), GEOGRAPHYHISTORY("GEOGRAPHYHISTORY"),
	HISTORY("HISTORY"), CULTUREGEOGRAPHYSCIENCE("CULTUREGEOGRAPHYSCIENCE"),
	CULTUREGEOGRAPHYHISTORY("CULTUREGEOGRAPHYHISTORY"), GEOGRAPHYSCIENCE("GEOGRAPHYSCIENCE"),
	CULTURESCIENCE("CULTURESCIENCE"), GEOGRAPHYHISTORYSCIENCE("GEOGRAPHYHISTORYSCIENCE");
	
	String name;

	private Category(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Category fromString(String name) {
		if (name == null || name.equals(""))
			return Category.NONE;

		for (Category c : Category.values()) {
			if (name.equalsIgnoreCase(c.name)) {
				return c;
			}
		}
		System.out.println("="+name+"=");
		return null;
	}
}

