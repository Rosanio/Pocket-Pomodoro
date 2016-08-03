package com.epicodus.pocketpomodoro.contracts;

import android.support.annotation.Nullable;

import com.epicodus.pocketpomodoro.models.Card;

import java.util.ArrayList;

/**
 * Created by Matt on 8/2/2016.
 */
public interface CreateDeckInputContract {
    interface Presenter {
        void translateQuestion(String language, String question);
        boolean checkQuestionValidity(String question, @Nullable String answer);
        void setCards(ArrayList<Card> cards);
        void addCard(String question, String answer);
    }

    interface View {
        void setTextTranslate();
        void unsetTextTranslate();
        void makeErrorToast(String message);
        void addCards(ArrayList<Card> cards);
        void informActivity(ArrayList<Card> cards);
    }
}
