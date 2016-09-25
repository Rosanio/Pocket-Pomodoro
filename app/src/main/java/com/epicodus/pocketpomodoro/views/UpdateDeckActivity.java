package com.epicodus.pocketpomodoro.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import com.epicodus.pocketpomodoro.Constants;
import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.adapters.FirebaseMyCardsListAdapter;
import com.epicodus.pocketpomodoro.models.Card;
import com.epicodus.pocketpomodoro.models.Deck;
import com.epicodus.pocketpomodoro.util.NpaLinearLayoutManager;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UpdateDeckActivity extends AppCompatActivity {
    @Bind(R.id.deckNameEditText) EditText mDeckNameEditText;
    @Bind(R.id.deckCategoryEditText) EditText mDeckCategoryEditText;
    @Bind(R.id.myCardsRecyclerView) RecyclerView mCardsRecyclerView;

    private Deck mDeck;
    private ArrayList<Card> mCards;

    private Query mCardsRef;
    private FirebaseMyCardsListAdapter mMyCardsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_deck);
        ButterKnife.bind(this);

        mDeck = Parcels.unwrap(getIntent().getParcelableExtra("deck"));

        mDeckNameEditText.setText(mDeck.getName());
        mDeckCategoryEditText.setText(mDeck.getCategory());

        mCardsRef = (new Firebase(Constants.FIREBASE_URL_CARDS)).child(mDeck.getId());
        Log.d(mCardsRef+"", "");
        populateCardsRecyclerView();
    }

    public void populateCardsRecyclerView() {
        mMyCardsAdapter = new FirebaseMyCardsListAdapter(mCardsRef, Card.class);
        mCardsRecyclerView.setLayoutManager(new NpaLinearLayoutManager(this));
        mCardsRecyclerView.setAdapter(mMyCardsAdapter);
    }
}
