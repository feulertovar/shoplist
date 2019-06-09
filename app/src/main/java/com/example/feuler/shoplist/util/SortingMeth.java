package com.example.feuler.shoplist.util;

import java.util.ArrayList;
import java.util.List;

import com.example.feuler.shoplist.Interface.SortingFace;
import com.example.feuler.shoplist.model.Items;

public class SortingMeth implements SortingFace {

	double cashLeft;
    MyQuery myquery = new MyQuery();

	public SortingMeth() {
		//
		cashLeft = DataIO.getBankAccount();
	}

	// Copy Constructor
	SortingMeth(SortingMeth cSort) {
		this.cashLeft = cSort.cashLeft;
	}

	// equals method
	public boolean equals(SortingMeth obj) {
		return (this.cashLeft == obj.cashLeft);
	}

	// Bubble Sort
	public ArrayList<Items> bubbleSortList(ArrayList<Items> item) {

		// Sort by Bubble Sort
		for (int i = 0; i < item.size() - 1; i++) {

			for (int j = 0; j < item.size() - i - 1; j++) {
				if (item.get(j).getPriority() > item.get(j + 1).getPriority()) {
					// Swap the temp and item[i]
					Items temp = item.get(j);
					item.set(j, item.get(j + 1));
					item.set(j + 1, temp);
				}
			}
		}
		return item;
	}

	// Selection Sort
	public ArrayList<Items> selectSortList(ArrayList<Items> item) {

		// Sort by selection
		for (int i = 0; i < item.size() - 1; i++) {
			// Find the lowest element in array
			int min = i;
			for (int j = i + 1; j < item.size(); j++)
				if (item.get(j).getPriority() < item.get(min).getPriority())
					min = j;

			// Swap the found lowest element with the first
			Items temp = item.get(min);
			item.set(min, item.get(i));
			item.set(i, temp);
		}
		return item;
	}

	// Method that shops based on shopping list and user Bank Account amount
	public void goShopping(ArrayList<Items> sortedList, int numItems, ArrayList<Items> inCart,
			ArrayList<Items> outCart ) {

		double dTotal = 0.0;
		int iItems = 0;

		for (int i = 0; i < numItems; i++) {


			// Total amount for item
			dTotal = ((sortedList.get(i).getPrice()) * (sortedList.get(i).getQuantity()));

			// check priority item priority && price left

			if ((sortedList.get(i).getPriority() == i + 1) & dTotal <= cashLeft) {
				//
				cashLeft = cashLeft - dTotal;
				DataIO.setBankAccount(cashLeft);
				
				// initialize array objects
				inCart.add(new Items(sortedList.get(i)));
			} else {
				outCart.add(new Items(sortedList.get(i)));
			}

			iItems = iItems + sortedList.get(i).getQuantity();
		}

		///
		if (outCart != null & cashLeft > 0 & outCart.size() > 0 ){
			// new loop index based on size of remaining items "outCart"
			//inCart is shopping list
			//outCart is items not in list
			int inIndex = inCart.size();
			int outIndex = outCart.size();
			int newIndex = inCart.size();



			// loop for outCart, remaining Items get be added
			for (int i = 0; i < outIndex; i++) {
                // get the quantity of the item
				int itemQuantity = outCart.get(i).getQuantity();

				// Sort the outCart to add remaining items by priority
				outCart = bubbleSortList(outCart);

				// loop though quantities for the item above
				for (int j = 0; j < itemQuantity; j++) {

                    // check price of remaining items to see if they can be added to the cart
                    if ((outCart.get(i).getPrice() <= cashLeft) & (outCart.get(i).getPrice() != 0)) {

                        // adjust Bank Account accordingly
                        cashLeft = cashLeft - outCart.get(i).getPrice();
                        //bankAccount = cashLeft;
                        DataIO.setBankAccount(cashLeft);

                        //check in inCart to see if its empty
                        //If Index = 0, then just add item
						inIndex = inCart.size();
                        if (inIndex != 0) {

                            for (int in = 0; in < inIndex; in++) {
                                // check if item present in inCart,
                                if (inCart.get(in).getName().toString().equalsIgnoreCase(outCart.get(i).getName().toString())) {

                                	if ( outCart != null & outCart.size()!= 0  ){

                                		outCart.get(i).setQuantity(outCart.get(i).getQuantity() - 1);
									}
                                    inCart.get(in).setQuantity(inCart.get(in).getQuantity() + 1);

                                } else
                                //
                                {
                                	//add item to inCart if not present
                                    inCart.add(newIndex, new Items(outCart.get(i)));
                                    inCart.get(newIndex).setQuantity(1);
                                    outCart.get(i).setQuantity(outCart.get(i).getQuantity() - 1);
                                    //increase index
                                    newIndex += 1;

                                }

                                // remove item from outCart if quantity goes to 0
                                if (outCart != null & outCart.get(i).getQuantity() == 0) {
                                    outCart.remove(outCart.get(i));

                                    //adjust index
                                    outIndex -= 1;
                                }


                            }
                        } else {// inCart is Empty, add new item
                            inCart.add(newIndex, new Items(outCart.get(i)));
                            inCart.get(newIndex).setQuantity(1);
                            outCart.get(i).setQuantity(outCart.get(i).getQuantity() - 1);
                            //increase index
                            newIndex += 1;

                        }

                    }


				}
			}
		}

	}

}