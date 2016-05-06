/*todo:
*/

package com.example.guest.pomodoro.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.pomodoro.Constants;
import com.example.guest.pomodoro.models.Card;
import com.example.guest.pomodoro.adapters.QAListAdapter;
import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.models.Deck;
import com.example.guest.pomodoro.services.YandexService;
import com.firebase.client.Firebase;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreateDeckActivity extends AppCompatActivity implements View.OnClickListener, AddDeckFragment.AddDeckDialogListener {

    @Bind(R.id.questionEditText) EditText mQuestionEditText;
    @Bind(R.id.answerEditText) EditText mAnswerEditText;
    @Bind(R.id.addCardButton) Button mAddCardButton;
    @Bind(R.id.recyclerView) RecyclerView mQaRecyclerView;
    @Bind(R.id.createDeckButton) Button mCreateDeckButton;
    @Bind(R.id.languageSpinner) Spinner mLanguageSpinner;
    @Bind(R.id.translateQuestionButton) Button mTranslateQuestionButton;
    @Bind(R.id.loadingTextView) TextView mLoadingTextView;
    ArrayList<String> questions = new ArrayList<String>();
    ArrayList<String> answers = new ArrayList<String>();
    ArrayList<Card> cards = new ArrayList<>();
    QAListAdapter adapter;
    String[] languages = {"Spanish", "French", "German", "Italian"};
    private Firebase mDecksRef;
    private Firebase mCardsRef;

    public void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if(!(view instanceof EditText || view instanceof Button)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(CreateDeckActivity.this);
                    findViewById(R.id.parentContainer).requestFocus();
                    return false;
                }
            });
        }

        if(view instanceof ViewGroup) {
            for(int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deck);
        ButterKnife.bind(this);
        setupUI(findViewById(R.id.parentContainer));

        mDecksRef = new Firebase(Constants.FIREBASE_URL_DECKS);
        mCardsRef = new Firebase(Constants.FIREBASE_URL_CARDS);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, languages);
        mLanguageSpinner.setAdapter(spinnerAdapter);

        mAddCardButton.setOnClickListener(this);
        mCreateDeckButton.setOnClickListener(this);
        mTranslateQuestionButton.setOnClickListener(this);

        adapter = new QAListAdapter(this, cards);
        mQaRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CreateDeckActivity.this);
        mQaRecyclerView.setLayoutManager(layoutManager);
        mQaRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addCardButton:
                String question = mQuestionEditText.getText().toString();
                String answer = mAnswerEditText.getText().toString();
                if(question.length()>0 && answer.length() > 0) {

                    if(cards.size()==0) {
                        Card card = new Card(question, answer);
                        cards.add(card);
                        adapter.notifyDataSetChanged();
                    } else {
                        Boolean contains = false;
                        for(int i = 0; i < cards.size(); i++) {
                            if(cards.get(i).getQuestion().equals(question)) {
                                contains = true;
                            }
                        }
                        if(!contains) {
                            Card card = new Card(question, answer);
                            cards.add(card);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CreateDeckActivity.this, "This question has already been added", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(CreateDeckActivity.this, "Please fill out both question and answer forms", Toast.LENGTH_LONG).show();
                }

                mQuestionEditText.setText("");
                mAnswerEditText.setText("");
                mQuestionEditText.requestFocus();
                break;
            case R.id.createDeckButton:
                if(cards.size() > 0) {
                    showNewDeckDialog();
                } else {
                    Toast.makeText(this, "Please add at least one card", Toast.LENGTH_SHORT).show();
                }

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
                            Toast.makeText(CreateDeckActivity.this, "This question has already been added", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(CreateDeckActivity.this, "Please fill out question form", Toast.LENGTH_LONG).show();
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

                CreateDeckActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        mLoadingTextView.setText("");
                    }
                });

            }
        });
    }

    private void showNewDeckDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddDeckFragment addDeckDialogFragment = AddDeckFragment.newInstance("Add New Deck:");
        addDeckDialogFragment.show(fm, "fragment_add_deck");
    }

    @Override
    public void onFinishEditDialog(String nameText, String categoryText) {
        if(nameText.length()>0 && categoryText.length()>0) {
            Deck newDeck = new Deck(nameText, categoryText);
            Firebase newDeckRef = mDecksRef.push();
            String deckId = newDeckRef.getKey();
            newDeck.setId(deckId);
            newDeckRef.setValue(newDeck);
            Firebase deckCardsRef = new Firebase(Constants.FIREBASE_URL_CARDS).child(deckId);
            for(int i = 0; i < cards.size(); i++) {
                Card newCard = cards.get(i);
                Firebase newCardRef = deckCardsRef.push();
                String cardId = newCardRef.getKey();
                newCard.setId(cardId);
                newCardRef.setValue(newCard);
            }
            Intent intent = new Intent(CreateDeckActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(CreateDeckActivity.this, "You need to name and categorize your deck", Toast.LENGTH_SHORT).show();
        }

    }
}
