package com.example.feuler.shoplist.viewmodel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.feuler.shoplist.R;

import com.example.feuler.shoplist.adapter.DataListAdapter;
import com.example.feuler.shoplist.adapter.ShopListAdapter;
import com.example.feuler.shoplist.util.DataIO;
import com.example.feuler.shoplist.util.MyException;
import com.example.feuler.shoplist.util.MyQuery;
import com.example.feuler.shoplist.util.SortingMeth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.example.feuler.shoplist.model.Items;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements DataListAdapter.OnItemSelectedListener {

    private static final String TAG = "MainActivity";

    public static boolean mInitState = true;

    @BindView(R.id.recycler_items)
    RecyclerView mItemsRecycler;

    @BindView(R.id.view_empty)
    ViewGroup mEmptyView;;

    private DataListAdapter mAdapter;

    private FirebaseFirestore mFireStore;

    private Query mQuery;

    //View Model
    private TextInputLayout item_name;
    private TextInputLayout item_quan;
    private TextInputLayout item_priority;
    private TextInputLayout  item_price;
    private FloatingActionButton fab_add;
    private FloatingActionButton fab_go;

    //Bank Account and Name statics
    public static String mName = "name";
    public static double mBudget = 0.0;
    public static double mBank = 0.0;

    //Arrays
    ArrayList<Items> mItems = new ArrayList<Items>();
    ArrayList<Items> mSortedItems = new ArrayList<Items>();
    ArrayList<Items> mInCart = new ArrayList<Items>();
    ArrayList<Items> mOutCart = new ArrayList<Items>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        //Text input View Model
        item_name = (TextInputLayout)findViewById(R.id.text_input_item_name);
        item_quan = (TextInputLayout)findViewById(R.id.text_input_quan);
        item_priority = (TextInputLayout)findViewById(R.id.text_input_priority);
        item_price = (TextInputLayout)findViewById(R.id.text_input_price);
        fab_add = (FloatingActionButton)findViewById(R.id.fab_add_item);
        fab_go = (FloatingActionButton)findViewById(R.id.fab_goshop);


        //onClick handlers for controls and text
        item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_name.getEditText().setText(""); }
        });

        item_quan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_quan.getEditText().setText(""); }
        });

        item_priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_priority.getEditText().setText(""); }
        });

        item_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_price.getEditText().setText(""); }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onAddItemsClicked();
            }
        });

        fab_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoShopClicked();
            }
        });

        // Initialize Firestore and the main RecyclerView
        initFirestore();
        initRecyclerView();
        //initArray();

        //Set init state to change text fields on activity_main layout
        //Asking initial questions
        //Change Item filed to Name
        //Change Quantity field to Budget
        //Hide the other text fields
        if ( mInitState ) {
          item_name.setHint("Name:");
          item_quan.setHint("Budget:");
          item_priority.setVisibility(View.GONE);
          item_price.setVisibility(View.GONE);
        }

    }

    //initialize main data array with main mydata collection;
    public void initArray() {
        if (mItems != null & mItems.size() > 0){

            ArrayList<Items> tempList = new ArrayList<>();
            MyQuery qr = new MyQuery();

            tempList = qr.getListItems("mydata");

            if ( tempList != null & tempList.size() > 0){
                mItems = tempList;
            }

        }
    }
        //

    private void initFirestore() {
        // TODO(developer): Implement
        mFireStore = FirebaseFirestore.getInstance();
        mQuery = mFireStore.collection("mydata")
                .orderBy("name", Query.Direction.DESCENDING);
    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new DataListAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mItemsRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mItemsRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mItemsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mItemsRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    //checking for empty string
    private boolean validateName() {

        String str = item_name.getEditText().getText().toString().trim();

        if (str.isEmpty()) {
            item_name.setError("Field can't be empty");
            return false;
        } else if (str.length() > 15) {
            item_name.setError("Username too long");
            return false;
        } else {
            item_name.setError(null);
            return true;
        }
    }

    //checking for a number
    private boolean validateQuantity() {
        String str = item_quan.getEditText().getText().toString().trim();

        str = str.replaceAll("\\D+", "");

        if (str.equals("")) {
            item_quan.setError("Field Needs a number");
            return false;
        }else {
            item_quan.setError(null);
            return true;
        }
    }

    //checking for a number
    private boolean validateBudget() {
        String str = item_quan.getEditText().getText().toString().trim();

        str = str.replaceAll("\\D+", "");

        if (str.equals("")) {
            item_quan.setError("Field Needs a number");
            return false;
        }else {
            item_quan.setError(null);
            return true;
        }
    }

    //checking for a number
    private boolean validatePriority() {
        String str = item_priority.getEditText().getText().toString().trim();

        str = str.replaceAll("\\D+", "");

        if (str.equals("")) {
            item_priority.setError("Field Needs a number");
            return false;
        }else {
            item_priority.setError(null);
            return true;
        }
    }

    //checking for a number
    private boolean validatePrice() {
        String str = item_price.getEditText().getText().toString().trim();

        str = str.replaceAll("\\D+", "");

        if (str.equals("")) {
            item_price.setError("Field Needs an a number");
            return false;
        }else {
            item_price.setError(null);
            return true;
        }
    }

    //Confirming correct data types are being entered in text fields
    public boolean confirmAllInput() {
        if (!validateName() | !validateQuantity()  | !validatePriority() | !validatePrice()) {


            String input = "Name: " + item_name.getEditText().getText().toString();
            input += "\n";
            input += "Quantity: " + item_quan.getEditText().getText().toString();
            input += "\n";
            input += "Priority: " + item_priority.getEditText().getText().toString();
            input += "\n";
            input += "Price: " + item_price.getEditText().getText().toString();

            Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
            return false;
        }else
        { return true;}
    }

    //Confirming correct datatypes are being entered in text fields
    public boolean confirmInputs() {
        if (!validateName() | !validateBudget()) {


            String input = "Name: " + item_name.getEditText().getText().toString();
            input += "\n";
            input += "Budget: " + item_quan.getEditText().getText().toString();

            Toast.makeText(this, input, Toast.LENGTH_SHORT).show();

            mInitState = true;

            return false;
        }else
        {

            return true;}
    }
    public void onAddItemsClicked( ) {

            if (!mInitState ) {
                String name ="";
                int quantity = 0;
                int priority = 0;
                double price = 0.0;

                //Catch Exception for bad Data conversion
                try {
                    name = item_name.getEditText().getText().toString().trim();
                    quantity = Integer.parseInt(item_quan.getEditText().getText().toString().trim());
                    priority = Integer.parseInt(item_priority.getEditText().getText().toString().trim());
                    price = Double.parseDouble(item_price.getEditText().getText().toString().trim());
                }catch (NumberFormatException e){
                    Toast.makeText(this, "Please enter numbers for Quantity, Prioriry and Price"+"\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                //we don't have all the input data
                if ( !confirmAllInput() ){
                    return;
                }

                //Catch if we already have an item of the same name
                if ( mItems != null & mItems.size() > 0){

                    for (int i=0; i < mItems.size(); i++){
                        try{
                         if ( mItems.get(i).getName().equalsIgnoreCase( item_name.getEditText().getText().toString())){
                             throw new MyException();
                         }
                        }catch(Exception e){
                            Toast.makeText(this, "Item already entered"+"\n", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }

                }

                Items myItem = new Items();

                myItem.setName(name);
                myItem.setPrice(price);
                myItem.setQuantity(quantity);
                myItem.setPriority(priority);
                String strcol = "mydata";

                //
                MyQuery qr = new MyQuery();
                myItem = qr.setData(myItem, strcol);

                //Init Items array with items in main data list
                ArrayList<Items> myArray = new ArrayList<Items>();
                myArray = qr.getListItems("mydata");

                if (myArray != null & myArray.size() > 0) {
                    mItems.equals(myArray);
                }

                //add Item to your items array
                mItems.add(new Items(myItem));


            } else if ( mInitState & confirmInputs() ) {

                //Set init state to change text fields on activity_main layout
                //Change Item filed to Name
                //Change Quatity field to Budget
                mInitState = false;

                //Reset
                item_name.setHint("Item Name:");
                item_quan.setHint("Quantity:");
                item_priority.setVisibility(View.VISIBLE);
                item_price.setVisibility(View.VISIBLE);

                mName = item_name.getEditText().getText().toString().trim();
                mBudget = Integer.parseInt(item_quan.getEditText().getText().toString().trim());
                mBank = mBudget;

                //clear text
                item_name.getEditText().setText("");
                item_quan.getEditText().setText("");
                item_priority.getEditText().setText("");
                item_price.getEditText().setText("");
            }

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                onAddHomeView();
                break;
            case R.id.menu_new_list:
                onAddNewListView();
                break;
            case R.id.menu_shop_list:
                onAddShopListView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onAddHomeView() {
        //no need to implement
}
    private void onAddNewListView() {
        // Start a new List
        MyQuery qr = new MyQuery();
        qr.createNewListState();

        Intent intent = new Intent(this, MainActivity.class);
        mInitState = true;

        startActivity(intent);
}

    private void onAddShopListView() {
        //
        // Go to Shopping List View
        Intent intent = new Intent(this, ShopListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(DocumentSnapshot Items) {

       //To Implement later

    }

    // Lets go shopping
    public void onGoShopClicked(){
        //
        if ( mItems != null & mItems.size() > 0){

            //setBank
            DataIO myData = new DataIO();
            DataIO.setBankAccount(mBank);

            // Sort Shopping List
            SortingMeth sort = new SortingMeth();
            mSortedItems = sort.bubbleSortList( mItems );
            int numItems = mSortedItems.size();

            // Display updated shopping list: Left in for debugging purposes
            ListDisplay show = new ListDisplay();
            show.displayList("Sorted List", numItems, mSortedItems);

            // Go Shopping, Spend at level of importance
            sort.goShopping(mSortedItems, numItems, mInCart, mOutCart );

            int inLen = mInCart.size();
            int outLen = mOutCart.size();

            // display all shopping carts : Left in for debugging purposes
            show.displayList("Purchased Cart::", inLen, mInCart);
            show.displayList("Items Left in Cart::", outLen, mOutCart);

            //Syncdatabases
            MyQuery qr = new MyQuery();
            qr.syncDataBases(mSortedItems, mInCart, mOutCart);
            //Sync main Array
            mItems.equals(mOutCart);

            // Display Bank account balance
            mBank = DataIO.getBankAccount();
            show.displayBalance(mName, mBank);
        }
    }

    private void showTodoToast() {
        Toast.makeText(this, "TODO: Implement", Toast.LENGTH_SHORT).show();
    }
}
