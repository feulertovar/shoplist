package com.example.feuler.shoplist.util;

import com.example.feuler.shoplist.model.Items;
import com.example.feuler.shoplist.viewmodel.ListDisplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import java.util.ArrayList;

public class DataIO {

	public DataIO() {
		// TODO Auto-generated constructor stub
	}
	
	private Scanner keyboard = new Scanner(System.in);
    private String userName;
    private static double BankAccount = 0.0;
	
	public boolean initializeList() {
		
		// Ask user what they want to do, load previous cart or begin a new one
		// if old cart then load previous cart info into Array and continue to add more
		// items
		System.out.println("Welcome to your shopping cart...");
		System.out.println("Would you like to load items from a previous list ( y/n )?");

		String strLoad = keyboard.nextLine();

		if (strLoad.equalsIgnoreCase("y")) {
			return true;
		} else
			return false;
	}
	
	
	// load list of remaining items from text file to an array
	public void loadList(ArrayList<Items>  item) {

		String fileName = "shoppingcart.txt";

		try {
			Scanner read = new Scanner(new File(fileName));
			read.useDelimiter(";");
			String id;
			String strName;
			double dPrice;
			int iQuantity, iPriority;
			while (read.hasNext()) {
				id = read.next();
				strName = read.next();
				dPrice = Double.parseDouble(read.next());
				iQuantity = Integer.parseInt(read.next());
				iPriority = Integer.parseInt(read.next());
				item.add(new Items(id,strName, dPrice, iQuantity, iPriority));
				read.nextLine();
				//
			}
			read.close();

			int iIndex = item.size();
			int numItems = iIndex;
			ListDisplay show = new ListDisplay();

			// show remaining cart
			show.displayList("Remaining List: " + numItems + " items", numItems, item);

		} catch (FileNotFoundException e) {
			System.out.println("Problem opening the file " + fileName);
		}
	}
	
	// save list of remaining items to a text file
	public void saveList(ArrayList<Items> shopcart) {
		String fileName = "shoppingcart.txt";
		try {
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			for (Items item : shopcart) {
				if ((item.getName() != "no item") && (item.getPrice() != 0.0)) {
					writer.print(item.getName() + ";" + item.getPrice() + ";" + item.getQuantity() + ";"
							+ item.getPriority() + ";\n");
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("Problem opening the file " + fileName);
		} catch (IOException e) {
			System.out.println("Problem with outputing to file " + fileName);
		}

	}

	// return Bank Account
	public static double getBankAccount() {
		return BankAccount;
	}

	// return Bank Account
	public static void setBankAccount(double cashLeft) {
		BankAccount = cashLeft;
	}

	// return the user name
	public String getUserName() {
		return userName;
	}

}
