package com.example.guest.pomodoro.ui;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.guest.pomodoro.Constants;
import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.adapters.FirebaseDeckListAdapter;
import com.example.guest.pomodoro.models.Deck;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_deck);
        ButterKnife.bind(this);

        mFirebaseDecksRef = new Firebase(Constants.FIREBASE_URL_DECKS);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(SelectDeckActivity.this, android.R.layout.simple_list_item_1, sortOptions);
        mSortOptionsSpinner.setAdapter(spinnerAdapter);

        setUpFirebaseQuery();
        setUpRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //submit query to firebase to return decks which match this name
                String firstLetter = query.substring(0, 1).toUpperCase();
                mQuery = new Firebase(Constants.FIREBASE_URL_DECKS).orderByChild("name").startAt(query.toUpperCase()).endAt(firstLetter+"\uf8ff");
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

    private void setUpFirebaseQuery() {
        String location = mFirebaseDecksRef.toString();
        mQuery = new Firebase(location);
    }

    private void setUpRecyclerView() {
        mAdapter = new FirebaseDeckListAdapter(mQuery, Deck.class);
        mDecksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDecksRecyclerView.setAdapter(mAdapter);
    }


}
