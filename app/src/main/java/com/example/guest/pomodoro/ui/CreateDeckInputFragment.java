package com.example.guest.pomodoro.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateDeckInputFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.questionEditText) EditText mQuestionEditText;
    @Bind(R.id.answerEditText) EditText mAnswerEditText;
    @Bind(R.id.addCardButton) Button mAddCardButton;
    @Bind(R.id.languageSpinner) Spinner mLanguageSpinner;
    @Bind(R.id.translateQuestionButton) Button mTranslateQuestionButton;
    @Bind(R.id.loadingTextView) TextView mLoadingTextView;
    String[] languages = {"Spanish", "French", "German", "Italian"};
    ArrayList<Card> cards = new ArrayList<>();
    OnCardAddedListener mOnCardAddedListener;

    public CreateDeckInputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnCardAddedListener = (OnCardAddedListener)  context;
        } catch (ClassCastException e) {
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

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addCardButton:
                String question = mQuestionEditText.getText().toString();
                String answer = mAnswerEditText.getText().toString();
                if (question.length() > 0 && answer.length() > 0) {

                    if (cards.size() == 0) {
                        Card card = new Card(question, answer);
                        cards.add(card);
                        mOnCardAddedListener.onCardAdded(cards);
                    } else {
                        Boolean contains = false;
                        for (int i = 0; i < cards.size(); i++) {
                            if (cards.get(i).getQuestion().equals(question)) {
                                contains = true;
                            }
                        }
                        if (!contains) {
                            Card card = new Card(question, answer);
                            cards.add(card);
                            mOnCardAddedListener.onCardAdded(cards);
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
                break;

            case R.id.translateQuestionButton:
                String language = mLanguageSpinner.getSelectedItem().toString();
                String text = mQuestionEditText.getText().toString();
                if(text.length() > 0) {
                    if(cards.size() == 0) {
                        mLoadingTextView.setText("translating...");
                        translateText(text, language);
                    } else {
                        Boolean contains = false;
                        for(int i = 0; i < cards.size(); i++) {
                            if(cards.get(i).getQuestion().equals(text)) {
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
                cards = yandexService.processResults(cards, question, response);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mOnCardAddedListener.onCardAdded(cards);
                        mLoadingTextView.setText("");
                    }
                });

            }
        });
    }

}
