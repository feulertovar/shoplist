package com.example.feuler.shoplist.util;

import java.util.ArrayList;
import com.example.feuler.shoplist.model.Items;
import com.example.feuler.shoplist.viewmodel.ListDisplay;

public abstract class ShopList extends Store {

	public static void main(String[] args) {
		
		ArrayList<Items> item = new ArrayList<Items>();
		ArrayList<Items> sortedItem = new ArrayList<Items>();
		ArrayList<Items> inCart = new ArrayList<Items>();
		ArrayList<Items> outCart = new ArrayList<Items>();
		int numItems = 0;
		int iIndex = 0;
		String name;
		double bankBalance = 0.0;

		//
		DataIO myData = new DataIO();
		
		// Ask you if they want to load a previous list
		if (myData.initializeList()) {
			// runShoppingList(numItems, iIndex);
			myData.loadList(item);
		}
		
		//new Store
		Store myStore = new Store();
		
		// Get user name, budget and number of items
		name = myStore.inputName();

		// Get number of items
		numItems = myStore.inputInfo(numItems);

		// Get Quantity and BankAccount
		myStore.inputBudget();

		// Get user input item name.
		myStore.inputItems(item, numItems, iIndex);

		// Get user input priority.
		myStore.inputPriority(item, numItems);

		// Set Price for item
		myStore.inputPrice(item, numItems, Store.getBankAccount());

		ListDisplay show = new ListDisplay();

		// Display Bank account balance
		bankBalance = Store.getBankAccount();
		show.displayBalance(name, bankBalance);

		// Display Current shopping list
		show.displayList("Original", numItems, item);

		// Sort Shopping List
		SortingMeth list = new SortingMeth();
		sortedItem = list.bubbleSortList(item);

		// Display updated shopping list
		show.displayList("Sorted List", numItems, sortedItem);

		// Go Shopping, Spend at level of importance
		list.goShopping(sortedItem, numItems, inCart, outCart );

		int inLen = inCart.size();
		int outLen = outCart.size();

		// save remaining items shopping list to .txt file
		myData.saveList(outCart);

		// display all shopping carts
		show.displayList("Purchased Cart::", inLen, inCart);
		show.displayList("Items Left in Cart::", outLen, outCart);

		// Display Bank account balance
		bankBalance = Store.getBankAccount();
		show.displayBalance(name, bankBalance);
	}

}
