package com.epicodus.pocketpomodoro.contracts;

import com.epicodus.pocketpomodoro.models.Card;

import java.util.ArrayList;

/**
 * Created by Matt on 8/13/2016.
 */
public interface CreateDeckDisplayContract {
    interface View {
        void navigateToMain();
        void makeErrorToast(String error);
    }

    interface Presenter {
        void createDeck(String name, String category, String uid);
        ArrayList<Card> getCards();
        void updateCards(ArrayList<Card> cards);
    }
}
