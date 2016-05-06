/*todo:
    possibly adjust text size based on length
    try and incorporate timer
    add warnings for locking phone and how to use accents
    maybe prompt user for time to spend studying
    maybe just display option to study same deck if score is not high enough
 */

package com.example.guest.pomodoro.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.pomodoro.Constants;
import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.adapters.CardPagerAdapter;
import com.example.guest.pomodoro.models.Card;
import com.example.guest.pomodoro.models.Deck;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StudyActivity extends AppCompatActivity implements View.OnClickListener, RateDeckFragment.RateDeckDialogListener {

    //    @Bind(R.id.pointsTextView) TextView mPointsTextView;
    private Deck mDeck;
    ArrayList<Card> mCards = new ArrayList<>();
    ArrayList<String> answeredQuestions = new ArrayList<>();
    @Bind(R.id.pointsTextView) TextView mPointsTextView;
    @Bind(R.id.answerEditText) EditText mAnswerEditText;
    @Bind(R.id.submitButton) Button mSubmitButton;
    @Bind(R.id.resultsTextView) TextView mResultsTextView;
    @Bind(R.id.adjustPointsTextView) TextView mAdjustPointsTextView;
    @Bind(R.id.viewPager) ViewPager mViewPager;
    @Bind(R.id.studyAgainButton) Button mStudyAgainButton;
    @Bind(R.id.showAnswerButton) Button mShowAnswerButton;
    private CardPagerAdapter adapterViewPager;
    private boolean won = false;
    private Firebase mDeckCardsRef;
    private Firebase mDeckRatingRef;
    private SharedPreferences mSharedPreferences;
    private String mUId;

    int index;
    int points;

    public void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if(!won) {
                        hideKeyboard(StudyActivity.this);
                    }
                    findViewById(R.id.parentContainer).requestFocus();
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        ButterKnife.bind(this);
        setupUI(findViewById(R.id.parentContainer));

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUId = mSharedPreferences.getString(Constants.KEY_UID, null);

        Intent intent = getIntent();
        mDeck = Parcels.unwrap(intent.getParcelableExtra("deck"));
        mDeckCardsRef = new Firebase(Constants.FIREBASE_URL_CARDS).child(mDeck.getId());
        mDeckCardsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map cardsMap = (Map) snapshot.getValue();

                if(cardsMap != null) {
                    List cardsObjectList = new ArrayList<>(cardsMap.values());
                    for(int i = 0; i < cardsObjectList.size(); i++) {
                        Map thisCard = (Map) cardsObjectList.get(i);
                        String cardQuestion = thisCard.get("question").toString();
                        String cardAnswer = thisCard.get("answer").toString();
                        Card newCard = new Card(cardQuestion, cardAnswer);
                        mCards.add(newCard);
                    }

                    adapterViewPager = new CardPagerAdapter(getSupportFragmentManager(), mCards);
                    mViewPager.setAdapter(adapterViewPager);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mDeckRatingRef = new Firebase(Constants.FIREBASE_ROOT_URL + "/rating/" + mDeck.getId());
        mDeckRatingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map ratingsMap = (Map) snapshot.getValue();
                if(ratingsMap != null) {
                    List ratingsList = new ArrayList<>(ratingsMap.values());
                    float totalRating = 0;
                    for(int i = 0; i < ratingsList.size(); i++) {
                        double ratingDouble = (double) ratingsList.get(i);
                        float rating = (float) ratingDouble;
                        totalRating += rating;
                    }
                    float averageRating = totalRating/ratingsList.size();
                    mDeck.setRating(averageRating);
                    Firebase deckRatingRef = new Firebase(Constants.FIREBASE_URL_DECKS).child(mDeck.getId()).child("rating");
                    deckRatingRef.setValue(averageRating);
                    Log.d("rating", mDeck.getRating()+"");
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {

            }
        });


        mSubmitButton.setOnClickListener(this);
        mStudyAgainButton.setOnClickListener(this);
        mShowAnswerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitButton:
                if(!won) {
                    index = mViewPager.getCurrentItem();
                    if(answeredQuestions.size()==0) {
                        guessAnswer(index);
                    } else {
                        Boolean contains = false;
                        for(int i = 0; i < answeredQuestions.size(); i++) {
                            if(answeredQuestions.get(i).equals(mCards.get(index).getQuestion())) {
                                contains = true;
                            }
                        }
                        if(!contains) {
                            guessAnswer(index);
                        } else {
                            Toast.makeText(this, "You've already answered this question", Toast.LENGTH_LONG).show();
                        }
                    }
                    if(answeredQuestions.size()== mCards.size()) {
                        won = true;
                        mResultsTextView.setText("You've correctly guessed all questions!");
                        mAdjustPointsTextView.setText("Final score: " + points);
                        mAnswerEditText.setVisibility(View.INVISIBLE);
                        mStudyAgainButton.setVisibility(View.VISIBLE);
                        mSubmitButton.setText("Make a New Deck");
                    }
                } else {
                    Intent intent = new Intent(StudyActivity.this, CreateDeckActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.studyAgainButton:
                finish();
                startActivity(getIntent());
                break;
            case R.id.showAnswerButton:
                if(!won) {
                    index = mViewPager.getCurrentItem();
                    Toast.makeText(this, mCards.get(index).getAnswer(), Toast.LENGTH_LONG).show();
                    points -= 2;
                    mPointsTextView.setText(String.valueOf(points));
                } else {
                    showRateDeckDialog();
                }

        }

    }

    public void guessAnswer(int index) {
        String answer = mAnswerEditText.getText().toString();
        mAnswerEditText.setText("");
        mResultsTextView.setText("Your Answer: " + answer);
        if (answer.toLowerCase().equals(mCards.get(index).getAnswer().toLowerCase())) {
            answeredQuestions.add(mCards.get(index).getQuestion());
            points += 2;
            mPointsTextView.setText(String.valueOf(points));
            mAdjustPointsTextView.setText("+2 points");
        } else {
            points -= 1;
            mPointsTextView.setText(String.valueOf(points));
            mAdjustPointsTextView.setText("-1 points");
        }
    }

    private void showRateDeckDialog() {
        FragmentManager fm = getSupportFragmentManager();
        RateDeckFragment rateDeckFragment = RateDeckFragment.newInstance("Rate this Deck:");
        rateDeckFragment.show(fm, "fragment_rate_deck");
    }

    @Override
    public void onFinishEditDialog(float rating) {
        Firebase deckUsersRatingRef = new Firebase(mDeckRatingRef + "/" + mUId);
        deckUsersRatingRef.setValue(rating);
        Toast.makeText(StudyActivity.this, "You rated " + mDeck.getName() + " at " + rating + " stars.", Toast.LENGTH_SHORT).show();
    }
}
