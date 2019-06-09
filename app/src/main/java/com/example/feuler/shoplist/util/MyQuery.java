package com.example.feuler.shoplist.util;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.feuler.shoplist.model.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;
import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class MyQuery {

    private FirebaseFirestore mFireStore;
    private  static ArrayList<Items> mListArray = new ArrayList<Items>();

    //sync database with inCart and outCart
    public void syncDataBases(ArrayList<Items> data, ArrayList<Items> inCart, ArrayList<Items> outCart){

        //Remove all items from collections
        createNewListState();
        /*
        for ( int i = 0; i < data.size(); i++){
            delDocument("mydata", data.get(i).getId());
        }*/

        //sync myshoplist
        for ( int i = 0; i < inCart.size(); i++){
        //populate shopping list
        setData(inCart.get(i), "myshoplist");
        }

        //sync mylist
        for ( int i = 0; i < outCart.size(); i++){
            //populate data list
            setData(outCart.get(i), "mydata");
        }
    }


    //Adding a Document
    public Items setData(Items item, String pathname){

        mFireStore = FirebaseFirestore.getInstance();

        // Get a reference to the restaurants collection
        CollectionReference mylists = mFireStore.collection( pathname );

        //get ID and assign to model
        String documentId = mylists.document().getId();
        item.setId( documentId );

        //add to Items array and database
        mylists.document(documentId).set(item);
        return item;
    }

    //Deleting a Document
    public void delDocument(String collection, String docId ){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection( collection ).document(docId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }

    //Get all the documents from a Collection and add them to an array of model (Items)
    public ArrayList<Items>getListItems( String pathname ) {
        mFireStore = FirebaseFirestore.getInstance();



        mFireStore.collection(pathname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Convert the whole Query Snapshot to a list
                                // of objects directly! No need to fetch each
                                // document.
                                Items item = document.toObject(Items.class);

                                // Add all to your list
                                mListArray.add(item);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return mListArray;
    }

    public void createNewListState(){

        //Empty all docs from collections
        //Temp Arrays
        ArrayList<Items> myData = new ArrayList<>();
        ArrayList<Items> myShopList = new ArrayList<>();

        //erase myData collection
        myData = getListItems("mydata");

        if ( myData != null & myData.size() > 0) {
            for (int i = 0; i < myData.size(); i++) {
                //populate shopping list
                delDocument("mydata", myData.get(i).getId());
            }
        }
        //erase mylist collection
        myShopList = getListItems("myshoplist");

        if ( myShopList!= null & myShopList.size() > 0) {
            for (int i = 0; i < myShopList.size(); i++) {
                //populate shopping list
                delDocument("myshoplist", myShopList.get(i).getId());
            }
        }
        //

    }

}
