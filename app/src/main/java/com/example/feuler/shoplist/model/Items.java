package com.example.feuler.shoplist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Items {

	private String id;
	private String name;
	private int priority;
	private double price;
	private int quantity;

	public Items() {
		
		//constructor
        id = "";
		name = "no item";
		price = 0.0;
		quantity = 1;
		priority = 0;	

	}
	
	public Items(String docid, String strName, double dPrice, int iQuantity, int iPriority ) {
		
		//constructor
        id = docid;
		name = strName;
		price = dPrice;
		quantity = iQuantity;
		priority = iPriority;	

	}
	
	// copy constructor
	public Items(Items cItems) {

        this.id = cItems.id;
		this.name = cItems.name;
		this.price = cItems.price;
		this.quantity = cItems.quantity;
		this.priority = cItems.priority;

	}

    public void setId(String did) {
        id = did;
    }

    public String getId() {
        return id;
    }
	
	//equals method
	public boolean equals( Items obj) {
		return(this.name.equals(obj.name));		
	}

	public void setName(String iName) {
		name = iName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPriority(int iPriority) {
		priority = iPriority;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPrice(double iPrice) {
		price = iPrice;
	}
	
	public double getPrice() {
		return price;
	}

	public void setQuantity(int iQuantity) {
		quantity = iQuantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
}
