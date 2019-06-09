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

import com.example.feuler.shoplist.adapter.ShopListAdapter;
import com.example.feuler.shoplist.util.DataIO;
import com.example.feuler.shoplist.util.MyQuery;
import com.example.feuler.shoplist.util.SortingMeth;
import com.example.feuler.shoplist.util.Store;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.CollectionReference;
import com.example.feuler.shoplist.model.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopListActivity extends AppCompatActivity implements ShopListAdapter.OnItemSelectedListener {

    private static final String TAG = "ShopListActivity";

    private static final int RC_SIGN_IN = 9001;

    private static final int LIMIT = 50;

    @BindView(R.id.recycler_items_shop)
    RecyclerView mItemsRecyclerShop;

    @BindView(R.id.view_empty_shop)
    ViewGroup mEmptyViewShop;;

    private ShopListAdapter mAdapter;

    private FirebaseFirestore mFireStore;

    private Query mQuery;

    //Bank Account and Name statics
    public static String mName;
    public static double mBank;

    //View Model
    private TextView user_name;
    private TextView user_budget;
    private TextView user_bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);
        ButterKnife.bind(this);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        //Text input View Model
      user_name = (TextView)findViewById(R.id.user_name);
      user_budget = (TextView)findViewById(R.id.budget);
      user_bank = (TextView)findViewById(R.id.bank);

      String strname = ("Name: " + MainActivity.mName);
      String strbudget = ("Budget: " + Double.toString(MainActivity.mBudget));
      String strbank = ("Bank: " + Double.toString(MainActivity.mBank));
      user_name.setText(strname);
      user_budget.setText(strbudget);
      user_bank.setText(strbank);

        // Initialize Firestore and the ma.in RecyclerView
        initFirestore();
        initRecyclerView();

    }

    private void initFirestore() {
        // TODO(developer): Implement
        mFireStore = FirebaseFirestore.getInstance();
        mQuery = mFireStore.collection("myshoplist")
                .orderBy("name", Query.Direction.DESCENDING);
    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new ShopListAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mItemsRecyclerShop.setVisibility(View.GONE);
                    mEmptyViewShop.setVisibility(View.VISIBLE);
                } else {
                    mItemsRecyclerShop.setVisibility(View.VISIBLE);
                    mEmptyViewShop.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mItemsRecyclerShop.setLayoutManager(new LinearLayoutManager(this));
        mItemsRecyclerShop.setAdapter(mAdapter);
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
        // Go to home View
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.mInitState = true;
        startActivity(intent);
    }

    private void onAddNewListView() {
        // Clear Collections and initialize main view to Main View

        MyQuery qr = new MyQuery();
        qr.createNewListState();

        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.mInitState = true;

        startActivity(intent);
    }

    private void onAddShopListView() {
        // Go to Shopping List View
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


    @Override
    public void onItemSelected(DocumentSnapshot Items) {

    }


    private void showTodoToast() {
        Toast.makeText(this, "TODO: Implement", Toast.LENGTH_SHORT).show();
    }
}
