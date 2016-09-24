package com.epicodus.pocketpomodoro.presenters;

import com.epicodus.pocketpomodoro.Constants;
import com.epicodus.pocketpomodoro.contracts.CreateDeckDisplayContract;
import com.epicodus.pocketpomodoro.models.Card;
import com.epicodus.pocketpomodoro.models.Deck;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Presenter tied to CreateDeckDisplayFragment
 */
public class CreateDeckDisplayPresenter implements CreateDeckDisplayContract.Presenter {
    private CreateDeckDisplayContract.View mView;
    private Firebase mDecksRef;
    private Firebase mCardsRef;
    private Firebase mUserRef;

    private ArrayList<Card> mCards = new ArrayList<>();

    public CreateDeckDisplayPresenter(CreateDeckDisplayContract.View view, String uid) {
        mView = view;
        mDecksRef = new Firebase(Constants.FIREBASE_URL_DECKS);
        mCardsRef = new Firebase(Constants.FIREBASE_URL_CARDS);
        mUserRef = (new Firebase(Constants.FIREBASE_URL_USERS)).child(uid);
    }

    public void createDeck(String name, String category) {
        if(name.length()>0 && category.length()>0) {
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
            Deck newDeck = new Deck(name, category);
            createCardsInFirebase(createDeckInFirebase(newDeck));

            mView.navigateToMain();
        } else {
            mView.makeErrorToast("You need to name and categorize your deck");
        }

    }

    public ArrayList<Card> getCards() {
        return mCards;
    }

    public void updateCards(ArrayList<Card> cards) {
        if(mCards != null) {
            mCards.clear();
            mCards.addAll(cards);
        }
    }

    private String createDeckInFirebase(Deck deck) {
        Firebase newDeckRef = mDecksRef.push();
        String deckId = newDeckRef.getKey();
        deck.setId(deckId);
        Date deckCreatedDate = new Date();
        deck.setDate(-deckCreatedDate.getTime());
        newDeckRef.setValue(deck);
        mUserRef.child("decks").child(deckId).setValue(deck);
        return deckId;
    }

    private void createCardsInFirebase(String deckId) {
        Firebase deckCardsRef = mCardsRef.child(deckId);
        for(int i = 0; i < mCards.size(); i++) {
            Card newCard = mCards.get(i);
            Firebase newCardRef = deckCardsRef.push();
            String cardId = newCardRef.getKey();
            newCard.setId(cardId);
            newCardRef.setValue(newCard);
        }
    }

}
