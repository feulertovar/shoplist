package com.example.feuler.shoplist.viewmodel;

import com.example.feuler.shoplist.Interface.DisplayFace;
import com.example.feuler.shoplist.model.Items;
import java.util.ArrayList;

public class ListDisplay implements DisplayFace {

	String name;
	public ListDisplay() {
		// Constructor
		name = "Shopping List";
	}

	// Copy Constructor
	ListDisplay ( ListDisplay cDisplay ){
		
		this.name = cDisplay.name;
	}
	
	//equals method
	public boolean equals( ListDisplay obj) {
		return(this.name == obj.name);		
	}
	
	public void displayBalance (String userName, double BankAccount) {
		
		System.out.println("------------------------------");
		System.out.println(userName + "::");
		System.out.println("Bank Account Balance is $" + BankAccount + "\n");
		System.out.println("------------------------------");
	}
	
	
	public void displayList(String listType, int numItems, ArrayList<Items> item) {

		String lineFormat = "|%9s %9s     %8s %8s     |%n";
		System.out.println("\n" + name + ": " + listType);
		System.out.format("+-----+------+-----+-----+---------+-----------+%n");
		System.out.format("|    Item    |  Quantity |  Price  |  Priority |%n");
		System.out.format("+-----+------+-----+-----+---------+-----------+%n");

		for (int i = 0; i < item.size(); i++) {
			if ( (item.get(i) != null) && (item.get(i).getPrice() != 0) ) {
				System.out.format(lineFormat, item.get(i).getName(), item.get(i).getQuantity(), "$"+item.get(i).getPrice(), item.get(i).getPriority());

			}
		}
	}
	

}
