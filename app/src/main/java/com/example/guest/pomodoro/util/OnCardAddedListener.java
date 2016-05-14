package com.example.guest.pomodoro.util;

import com.example.guest.pomodoro.models.Card;

import java.util.ArrayList;

/**
 * Created by Matt on 5/14/2016.
 */
public interface OnCardAddedListener {
    public void onCardAdded(ArrayList<Card> cards);
}
