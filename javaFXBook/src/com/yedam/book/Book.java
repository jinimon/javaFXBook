package com.yedam.book;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {
	private SimpleStringProperty title;
	private SimpleStringProperty name;
	private SimpleStringProperty company;
	private SimpleIntegerProperty price;
	
	public Book(String title, String name, String company, int price) {
		this.title = new SimpleStringProperty(title);
		this.name = new SimpleStringProperty(name);
		this.company = new SimpleStringProperty(company);
		this.price = new SimpleIntegerProperty(price);
	}
	
	public String getTitle() {
		return this.title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public String getName() {
		return this.name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getCompany() {
		return this.company.get();
	}

	public void setCompany(String company) {
		this.company.set(company);
	}

	public int getPrice() {
		return this.price.get();
	}

	public void setPrice(int price) {
		this.price.set(price);
	}
}
