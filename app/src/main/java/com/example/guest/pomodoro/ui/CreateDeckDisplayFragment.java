package com.example.guest.pomodoro.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.guest.pomodoro.Constants;
import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.adapters.CardListAdapter;
import com.example.guest.pomodoro.models.Card;
import com.example.guest.pomodoro.models.Deck;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateDeckDisplayFragment extends Fragment implements View.OnClickListener, AddDeckFragment.AddDeckDialogListener {

    @Bind(R.id.recyclerView)
    RecyclerView mCardsRecyclerView;
    @Bind(R.id.createDeckButton)
    Button mCreateDeckButton;

    CardListAdapter adapter;
    private Firebase mDecksRef;
    private Firebase mCardsRef;
    private ArrayList<Card> cards = new ArrayList<>();


    public CreateDeckDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_deck_display, container, false);
        ButterKnife.bind(this, view);

        mDecksRef = new Firebase(Constants.FIREBASE_URL_DECKS);
        mCardsRef = new Firebase(Constants.FIREBASE_URL_CARDS);

        mCreateDeckButton.setOnClickListener(this);

        adapter = new CardListAdapter(getActivity(), cards);
        mCardsRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mCardsRecyclerView.setLayoutManager(layoutManager);
        mCardsRecyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.createDeckButton:
                if(cards.size() > 0) {
                    showNewDeckDialog();
                } else {
                    Toast.makeText(getActivity(), "Please add at least one card", Toast.LENGTH_SHORT).show();
                }

                break;


        }
    }

    private void showNewDeckDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddDeckFragment addDeckDialogFragment = AddDeckFragment.newInstance("Add New Deck:");
        addDeckDialogFragment.show(fm, "fragment_add_deck");
    }

    @Override
    public void onFinishEditDialog(String nameText, String categoryText) {
        if(nameText.length()>0 && categoryText.length()>0) {
            nameText = Character.toUpperCase(nameText.charAt(0)) + nameText.substring(1);
            Deck newDeck = new Deck(nameText, categoryText);
            Firebase newDeckRef = mDecksRef.push();
            String deckId = newDeckRef.getKey();
            newDeck.setId(deckId);
            newDeck.setDate(new Date());
            newDeckRef.setValue(newDeck);
            Firebase deckCardsRef = new Firebase(Constants.FIREBASE_URL_CARDS).child(deckId);
            for(int i = 0; i < cards.size(); i++) {
                Card newCard = cards.get(i);
                Firebase newCardRef = deckCardsRef.push();
                String cardId = newCardRef.getKey();
                newCard.setId(cardId);
                newCardRef.setValue(newCard);
            }
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "You need to name and categorize your deck", Toast.LENGTH_SHORT).show();
        }

    }

}
