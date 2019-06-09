package com.example.feuler.shoplist.Interface;

import java.util.ArrayList;
import com.example.feuler.shoplist.model.Items;

public interface DisplayFace {
	
	//use to display shopping list at any given time
	public void displayList(String listType, int numItems, ArrayList<Items> item);

}
