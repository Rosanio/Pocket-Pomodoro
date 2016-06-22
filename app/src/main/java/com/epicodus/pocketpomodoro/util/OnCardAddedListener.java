package com.epicodus.pocketpomodoro.util;

import com.epicodus.pocketpomodoro.models.Card;

import java.util.ArrayList;

/**
 * Created by Matt on 5/14/2016.
 */
public interface OnCardAddedListener {
    public void onCardAdded(ArrayList<Card> cards);
}
