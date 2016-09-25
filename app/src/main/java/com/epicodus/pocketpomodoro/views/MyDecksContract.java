package com.epicodus.pocketpomodoro.views;

import com.epicodus.pocketpomodoro.models.Deck;

import java.util.ArrayList;

/**
 * Created by Matt on 9/24/2016.
 */
public interface MyDecksContract {
    void createAdjustDeckDialog(int itemPosition, ArrayList<Deck> decks);
}
