package com.epicodus.pocketpomodoro.views;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.epicodus.pocketpomodoro.Constants;
import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.adapters.FirebaseDeckListAdapter;
import com.epicodus.pocketpomodoro.models.Deck;
import com.epicodus.pocketpomodoro.util.NpaLinearLayoutManager;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectDeckActivity extends AppCompatActivity {
    @Bind(R.id.decksRecyclerView) RecyclerView mDecksRecyclerView;
    @Bind(R.id.sortOptionsSpinner) Spinner mSortOptionsSpinner;
    private String[] sortOptions = {"Rating", "Popularity", "Most Recent", "Alphebetical"};
    private Query mQuery;
    private Firebase mFirebaseDecksRef;
    private FirebaseDeckListAdapter mAdapter;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_deck);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = sharedPreferences.getString(Constants.KEY_UID, "");

        mFirebaseDecksRef = new Firebase(Constants.FIREBASE_URL_DECKS);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(SelectDeckActivity.this, android.R.layout.simple_list_item_1, sortOptions);
        mSortOptionsSpinner.setAdapter(spinnerAdapter);
        mSortOptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("position", position+"");
                setUpFirebaseQuery(position);
                setUpRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setUpFirebaseQuery(0);
        setUpRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        Log.d("Menu Item", "it works");
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //submit query to firebase to return decks which match this name
                String firstLetter = query.substring(0, 1).toUpperCase();
                mQuery = new Firebase(Constants.FIREBASE_URL_DECKS).child(uid).orderByChild("name").startAt(query.toUpperCase()).endAt(firstLetter+"\uf8ff");
                setUpRecyclerView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setUpFirebaseQuery(int selectedItemId) {
        if(selectedItemId == 0) {
            mQuery = mFirebaseDecksRef.orderByChild("rating");
        } else if (selectedItemId == 1) {
            mQuery = mFirebaseDecksRef.orderByChild("timesCompleted");
        } else if (selectedItemId == 2) {
            mQuery = mFirebaseDecksRef.orderByChild("date");
        } else if (selectedItemId == 3) {
            mQuery = mFirebaseDecksRef.orderByChild("name");
        }
    }

    private void setUpRecyclerView() {
        mAdapter = new FirebaseDeckListAdapter(mQuery, Deck.class);
        mDecksRecyclerView.setLayoutManager(new NpaLinearLayoutManager(this));
        mDecksRecyclerView.setAdapter(mAdapter);
    }


}
