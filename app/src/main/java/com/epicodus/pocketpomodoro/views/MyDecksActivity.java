package com.epicodus.pocketpomodoro.views;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.epicodus.pocketpomodoro.Constants;
import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.adapters.FirebaseDeckListAdapter;
import com.epicodus.pocketpomodoro.adapters.FirebaseUserDecksListAdapter;
import com.epicodus.pocketpomodoro.models.Deck;
import com.epicodus.pocketpomodoro.util.NpaLinearLayoutManager;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyDecksActivity extends AppCompatActivity implements MyDecksContract {

    @Bind(R.id.myDecksRecyclerView) RecyclerView mMyDecksRecyclerView;
    private SharedPreferences mSharedPreferences;
    private Firebase mUserDecksRef;
    private FirebaseUserDecksListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_decks);
        ButterKnife.bind(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String uid = mSharedPreferences.getString(Constants.KEY_UID,"");
        mUserDecksRef = (new Firebase(Constants.FIREBASE_URL_USERS)).child(uid).child("decks");
        Query query = mUserDecksRef;
        mAdapter = new FirebaseUserDecksListAdapter(query, Deck.class);
        mMyDecksRecyclerView.setLayoutManager(new NpaLinearLayoutManager(this));
        mMyDecksRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void createAdjustDeckDialog(int itemPosition, ArrayList<Deck> decks) {
        FragmentManager fm = getSupportFragmentManager();
        AdjustDeckFragment dialogFragment = AdjustDeckFragment.newInstance(itemPosition, decks);
        dialogFragment.show(fm, "fragment_adjust_deck");
    }
}
