package com.epicodus.pocketpomodoro.presenters;

import android.support.annotation.Nullable;

import com.epicodus.pocketpomodoro.contracts.CreateDeckInputContract;
import com.epicodus.pocketpomodoro.models.Card;
import com.epicodus.pocketpomodoro.services.YandexService;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Presenter associated with CreateDeckInputFragment
 */
public class CreateDeckInputPresenter implements CreateDeckInputContract.Presenter {
    private ArrayList<Card> mCards = new ArrayList<>();
    private CreateDeckInputContract.View mView;

    public CreateDeckInputPresenter(CreateDeckInputContract.View view) {
        mView = view;
        if(mCards == null) {
            mCards = new ArrayList<>();
        }
    }

    public void translateQuestion(String language, String question) {
        mView.setTextTranslate();
        final YandexService yandexService = new YandexService();
        final String text = question;

        yandexService.translateText(text, language, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mCards = yandexService.processResults(mCards, question, response);
                mView.addCards(mCards);
            }
        });
    }

    public boolean checkQuestionValidity(String question, @Nullable String answer) {
        if(answer != null) {
            if(answer.length() == 0) {
                return false;
            }
        }
        if(question.length() > 0) {
            if(mCards.size() == 0) {
                return true;
            } else {
                for(int i = 0; i < mCards.size(); i++) {
                    if(mCards.get(i).getQuestion().equals(question)) {
                        mView.makeErrorToast("This question has already been added");
                        return false;
                    }
                }
                return true;
            }
        } else {
            mView.makeErrorToast("Please fill out question form");
            return false;
        }
    }

    public void setCards(ArrayList<Card> cards) {
        mCards = cards;
    }

    public void addCard(String question, String answer) {
        Card card = new Card(question, answer);
        mCards.add(card);
        mView.informActivity(mCards);
    }
}
