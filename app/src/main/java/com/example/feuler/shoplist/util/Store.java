package com.example.feuler.shoplist.util;

import java.util.ArrayList;
import java.util.Scanner;
import com.example.feuler.shoplist.model.Items;


public class Store {

	private double totalItems = 0.0;
	private boolean dupes;
	private String userName;
	private static double BankAccount = 0.0;
	private Scanner keyboard = new Scanner(System.in);

	public Store() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("static-access")

	// Copy Constructor
	Store(Store cStore) {
		totalItems = cStore.totalItems;
	}

	public String inputName() {

		// Ask user to input first name
		System.out.println("\n|----------------------------------------|");
		System.out.println("|--------------Shopping list-------------|");
		System.out.println("|________________________________________|");
		System.out.println("Enter '!' at anytime to exit this program.\n");

		System.out.println("Enter your first name:");
		//
		String name = keyboard.nextLine();
		if (!name.equalsIgnoreCase("!") ){
			// check first name for bad characters
			userName = name.replaceAll("[^a-zA-Z]", "");
		} else {
			exit();
		}
		return userName;
	}

	public int inputInfo(int numItems) {

		String myItems = "";
		do {
			try {
				System.out.println("How many items are you shopping for? (items from previous list will be included)");

				myItems = keyboard.nextLine();
				if (myItems.equalsIgnoreCase("!")) {
					exit();
				} 
				myItems = myItems.replaceAll("\\D+", "");

				if (!myItems.equals("")) {
					numItems = numItems + Integer.parseInt(myItems);
				} else {
					throw new MyException("Please enter an interger");
				}
			} catch (MyException e) {
				System.out.println(e.getMessage());
			}

		} while (myItems.equals(""));

		return numItems;

	}

	public void inputBudget() {

		String myItems = "";
		do {
			try {
				System.out.println("What is your budget for shopping?");

				myItems = keyboard.nextLine();
				
				if (myItems.equalsIgnoreCase("!")) {
					exit();
				} 
				
				myItems = myItems.replaceAll("\\D+", "");

				if (!myItems.equals("")) {
					BankAccount = Double.parseDouble(myItems);
				} else {
					throw new MyException("Please enter a price with cents ( 1.01 )");
				}
			} catch (MyException e) {
				System.out.println(e.getMessage());
			}

		} while (myItems.equals(""));

	}

	@SuppressWarnings("unused")
	public void inputItems(ArrayList<Items> item, int numItems, int iIndex) {
		// get user item name and Priority
		String iName = "None";
		dupes = false;
		String myQuantity = "";
		int iQuantity = 0;

		System.out.println("\nlets get started with your shopping cart...");

		for (int i = iIndex; i < numItems; i++) {
			// initialize array objects
			item.add(i, new Items());

			System.out.println("Please enter the name of your item");
			do {

				iName = keyboard.nextLine();
				try {
					for (int j = 0; j < item.size(); j++) {
						if (item.get(j) != null) {
							if (iName.equalsIgnoreCase(item.get(j).getName())) {
								dupes = true;
								throw new MyException("You already have a " + iName + " Please enter another item...");
							} else if (iName.equalsIgnoreCase("!")) {
								exit();
							} else {
								dupes = false;
							}
						} else {
							dupes = false;
							break;
						}
					}
				} catch (MyException e) {
					System.out.println(e.getMessage());
				}
			} while (dupes);

			// Set Name
			item.get(i).setName(iName);

			// Enter quantity of items
			do {
				try {

					System.out.println("Enter the quantity for " + item.get(i).getName());

					myQuantity = keyboard.nextLine();

					if (myQuantity.equalsIgnoreCase("!")) {
						exit();
					} 
					
					myQuantity = myQuantity.replaceAll("\\D+", "");
					if (!myQuantity.equals("")) {
						iQuantity = Integer.parseInt(myQuantity);
					} else {
						throw new MyException("Please enter an interger");
					}
				} catch (MyException e) {
					System.out.println(e.getMessage());
				}

			} while (myQuantity.equals(""));

			// Set Quantity
			item.get(i).setQuantity(iQuantity);
		}
	}

	public void inputPriority(ArrayList<Items> item, int numItems) {

		String myPriority = "";
		int iPriority = 0;

		System.out.println("Please enter the priority for the items");

		for (int i = 0; i < item.size(); i++) {
			// check if its an integer

			do {

				try {
					System.out.println("Enter the priority for the " + item.get(i).getName());

					myPriority = keyboard.nextLine();
					
					if (myPriority .equalsIgnoreCase("!")) {
						exit();
					} 
					
					myPriority = myPriority.replaceAll("\\D+", "");

					if (!myPriority.equals("")) {
						iPriority = Integer.parseInt(myPriority);
					} else {
						throw new MyException("Please enter an interger");
					}

					if (iPriority < 1 | iPriority > numItems) {
						throw new MyException("Please enter a priority between 1 and " + numItems);
					}

				} catch (MyException e) {

					System.out.println(e.getMessage());
				}
			} while (myPriority.equals("") | iPriority < 1 | iPriority > numItems);

			// Set Priority
			item.get(i).setPriority(iPriority);
		}
	}

	public void inputPrice(ArrayList<Items> item, int numItems, double BankAccount) {

		double dPrice = 0.0;
		System.out.println("|---------------------------------|");
		System.out.println("|Now Enter the price for each item|");
		System.out.println("|---------------------------------|");
		for (int i = 0; i < numItems; i++) {
			// iPrice =
			System.out.println("Please enter the price of the " + item.get(i).getName());
			while (!keyboard.hasNextInt() && !keyboard.hasNextDouble()) {
				keyboard.next(); // Read and discard offending non-int input
				// exit if "!"
				if ( dPrice == '!') {
					exit();
				}
				System.out.print("Please enter a price for " + item.get(i).getName()); // Re-prompt
			}

			dPrice = keyboard.nextDouble();

			item.get(i).setPrice(dPrice);
			totalItems += dPrice;
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

	// Exit the program
	public void exit() {
		System.out.println("Exiting program...Goodbye!");
		System.exit(0);
	}

}
