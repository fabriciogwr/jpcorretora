package com.fgwr.jpcorretora.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Checklist {

	
	private String item;
	private String status;
	
	public Checklist(String item, String status) {
		super();
		this.item = item;
		this.status = status;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public StringProperty item() {
		StringProperty item = new SimpleStringProperty((String) this.item);
		return item;
	}
	
	public StringProperty status() {
		StringProperty status = new SimpleStringProperty((String) this.status);
		return status;
	}
}
