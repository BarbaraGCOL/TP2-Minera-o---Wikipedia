package br.com.ufmg.wikipedia.files;

import br.com.ufmg.wikipedia.enums.Category;

public class Tuple {
	private long idPage;
	private Category category;
	
	public Tuple(long idPage, Category category) {
		this.idPage = idPage;
		this.category = category;
	}
	public long getIdPage() {
		return idPage;
	}
	public void setIdPage(long idPage) {
		this.idPage = idPage;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
}
