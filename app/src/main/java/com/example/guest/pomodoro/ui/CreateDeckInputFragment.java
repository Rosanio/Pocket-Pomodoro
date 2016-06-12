package com.example.guest.pomodoro.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.models.Card;
import com.example.guest.pomodoro.services.YandexService;
import com.example.guest.pomodoro.util.OnCardAddedListener;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreateDeckInputFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.questionEditText) EditText mQuestionEditText;
    @Bind(R.id.answerEditText) EditText mAnswerEditText;
    @Bind(R.id.addCardButton) Button mAddCardButton;
    @Bind(R.id.languageSpinner) Spinner mLanguageSpinner;
    @Bind(R.id.translateQuestionButton) Button mTranslateQuestionButton;
    @Bind(R.id.loadingTextView) TextView mLoadingTextView;
    String[] languages = {"Spanish", "French", "German", "Italian"};
    ArrayList<Card> mCards = new ArrayList<>();
    OnCardAddedListener mOnCardAddedListener;

    public CreateDeckInputFragment() {
        // Required empty public constructor
    }

    @Override
    //called as soon as a fragment is attached to an activity
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //if the parent activity implements this interface, then mOnCardAddedListener will be set to the instance of OnCardAddedListener which exists in the activity. This means information can be passed to the activity by calling its onCardAdded method.
            mOnCardAddedListener = (OnCardAddedListener) context;
        } catch (ClassCastException e) {
            //if the parent activity does not implement OnCardAddedListener, this error will fire.
            throw new ClassCastException(context.toString() + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_deck_input, container, false);
        ButterKnife.bind(this, view);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, languages);
        mLanguageSpinner.setAdapter(spinnerAdapter);

        mAddCardButton.setOnClickListener(this);
        mTranslateQuestionButton.setOnClickListener(this);

        mQuestionEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch(keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            mAnswerEditText.requestFocus();
                            return true;
                    }
                }
                return false;
            }
        });
        mAnswerEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            addCard();
                            return true;
                    }
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addCardButton:
                addCard();
                break;

            case R.id.translateQuestionButton:
                String language = mLanguageSpinner.getSelectedItem().toString();
                String text = mQuestionEditText.getText().toString();
                if(text.length() > 0) {
                    if(mCards.size() == 0) {
                        mLoadingTextView.setText("translating...");
                        translateText(text, language);
                    } else {
                        Boolean contains = false;
                        for(int i = 0; i < mCards.size(); i++) {
                            if(mCards.get(i).getQuestion().equals(text)) {
                                contains = true;
                            }
                        }
                        if(!contains) {
                            mLoadingTextView.setText("translating...");
                            translateText(text, language);
                        } else {
                            Toast.makeText(getActivity(), "This question has already been added", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Please fill out question form", Toast.LENGTH_LONG).show();
                }
                mQuestionEditText.setText("");
                mAnswerEditText.setText("");
                mQuestionEditText.requestFocus();
                break;
        }
    }

    private void translateText(String text, String language) {
        final YandexService yandexService = new YandexService();
        final String question = text;

        yandexService.translateText(text, language, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mCards = yandexService.processResults(mCards, question, response);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //This method refers to the overwritten onCardAdded method in CreateDeckActivity, meaning the value of mCards in that activity will be replaced with the value cards being passed into this method.
                        mOnCardAddedListener.onCardAdded(mCards);
                        mLoadingTextView.setText("");
                    }
                });

            }
        });
    }

    public void setCards(ArrayList<Card> cards) {
        mCards = cards;
    }

    public void addCard() {
        String question = mQuestionEditText.getText().toString();
        String answer = mAnswerEditText.getText().toString();
        if (question.length() > 0 && answer.length() > 0) {
            if(mCards == null) {
                mCards = new ArrayList<>();
            }

            if (mCards.size() == 0) {
                Card card = new Card(question, answer);
                mCards.add(card);
                //This method refers to the overwritten onCardAdded method in CreateDeckActivity, meaning the value of mCards in that activity will be replaced with the value cards being passed into this method.
                mOnCardAddedListener.onCardAdded(mCards);
            } else {
                Boolean contains = false;
                for (int i = 0; i < mCards.size(); i++) {
                    if (mCards.get(i).getQuestion().equals(question)) {
                        contains = true;
                    }
                }
                if (!contains) {
                    Card card = new Card(question, answer);
                    mCards.add(card);
                    //This method refers to the overwritten onCardAdded method in CreateDeckActivity, meaning the value of mCards in that activity will be replaced with the value cards being passed into this method.
                    mOnCardAddedListener.onCardAdded(mCards);
                } else {
                    Toast.makeText(getActivity(), "This question has already been added", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(getActivity(), "Please fill out both question and answer forms", Toast.LENGTH_LONG).show();
        }

        mQuestionEditText.setText("");
        mAnswerEditText.setText("");
        mQuestionEditText.requestFocus();
    }

}
