/*todo:
    add warnings for locking phone and how to use accents
    maybe prompt user for time to spend studying
    maybe just display option to study same deck if score is not high enough
 */

package com.epicodus.pocketpomodoro.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.pocketpomodoro.Constants;
import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.game.GameActivity;
import com.epicodus.pocketpomodoro.models.Card;
import com.epicodus.pocketpomodoro.models.Deck;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StudyActivity extends AppCompatActivity implements View.OnClickListener, RateDeckFragment.RateDeckDialogListener {

    private Deck mDeck;
    ArrayList<Card> mCards = new ArrayList<>();
    ArrayList<String> answeredQuestions = new ArrayList<>();
    @Bind(R.id.pointsTextView) TextView mPointsTextView;
    @Bind(R.id.answerEditText) EditText mAnswerEditText;
    @Bind(R.id.submitButton) Button mSubmitButton;
    @Bind(R.id.resultsTextView) TextView mResultsTextView;
    @Bind(R.id.adjustPointsTextView) TextView mAdjustPointsTextView;
    @Bind(R.id.studyAgainButton) Button mStudyAgainButton;
    @Bind(R.id.showAnswerButton) Button mShowAnswerButton;
    @Bind(R.id.cardContainer) FrameLayout mCardContainer;
    private boolean won = false;
    private Firebase mDeckRef;
    private Firebase mDeckCardsRef;
    private Firebase mDeckRatingRef;
    private SharedPreferences mSharedPreferences;
    private String mUId;
    private Card mCard;
    private Random randomNumberGenerator;
    private int deckSize;
    private Timer timer;
    private TimerTask task;
    private long currentTime;
    private long startTime;

    private GestureDetectorCompat mDetector;

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

        startTime = System.currentTimeMillis();

        randomNumberGenerator = new Random();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUId = mSharedPreferences.getString(Constants.KEY_UID, null);

        Intent intent = getIntent();
        Log.d("intent", intent+"");
        mDeck = Parcels.unwrap(intent.getParcelableExtra("deck"));
        Log.d("Times Completed Create", mDeck.getTimesCompleted()+"");
        mDeckRef = new Firebase(Constants.FIREBASE_URL_DECKS).child(mDeck.getId());
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
                    deckSize = mCards.size();
                    int position = randomNumberGenerator.nextInt(mCards.size());
                    createCardFragment(position);

                    mDetector = new GestureDetectorCompat(StudyActivity.this, new GestureListener());
                    mCardContainer.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            mDetector.onTouchEvent(event);
                            return true;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mDeckRatingRef = new Firebase(Constants.FIREBASE_ROOT_URL + "/ratings/" + mDeck.getId());
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
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {

            }
        });


        mSubmitButton.setOnClickListener(this);
        mStudyAgainButton.setOnClickListener(this);
        mShowAnswerButton.setOnClickListener(this);

        mAnswerEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(EditorInfo.IME_ACTION_DONE == actionId) {
                    if(!won) {
                        hideKeyboard(StudyActivity.this);
                        guessAnswer(mCard);
                        return true;
                    }
                }
                return false;
            }
        });

        task = new TimerTask() {
            @Override
            public void run() {
                currentTime = System.currentTimeMillis();
                if(currentTime - startTime > 30000) {
                    Intent intent = new Intent(StudyActivity.this, GameActivity.class);
                    intent.putExtra("deck", Parcels.wrap(mDeck));
                    finish();
                    startActivity(intent);
                }
            }
        };

        timer = new Timer();

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    private void createCardFragment(int position) {
        mCard = mCards.get(position);
        Log.d("card set", mCard.getQuestion());
        CardFragment cardFragment = CardFragment.newInstance(mCard);
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cardContainer, cardFragment);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitButton:
                if(!won) {
                    guessAnswer(mCard);
                } else {
                    Intent intent = new Intent(StudyActivity.this, SelectDeckActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.studyAgainButton:
                Intent intent = new Intent(StudyActivity.this, StudyActivity.class);
                intent.putExtra("deck", Parcels.wrap(mDeck));
                finish();
                startActivity(intent);
                break;
            case R.id.showAnswerButton:
                if(!won) {
                    Toast.makeText(this, mCard.getAnswer(), Toast.LENGTH_LONG).show();
                    points -= 2;
                    mPointsTextView.setText(String.valueOf(points));
                } else {
                    showRateDeckDialog();
                }

        }

    }

    public void guessAnswer(Card card) {
        String answer = mAnswerEditText.getText().toString();
        if (answer.toLowerCase().equals(mCard.getAnswer().toLowerCase())) {
            Log.d("correct", mCard.getAnswer());
            answeredQuestions.add(card.getQuestion());
            points += 2;
            mPointsTextView.setText(String.valueOf(points));
            mAdjustPointsTextView.setText("+2 points");
            checkWin();
        } else {
            Log.d("incorrect", mCard.getAnswer() + " " + answer);
            points -= 1;
            mPointsTextView.setText(String.valueOf(points));
            mAdjustPointsTextView.setText("-1 points");
        }
        mAnswerEditText.setText("");
        mResultsTextView.setText("Your Answer: " + answer);
    }

    public void checkWin() {
        if(answeredQuestions.size()== deckSize) {
            won = true;
            mResultsTextView.setText("You've correctly guessed all questions!");
            mAdjustPointsTextView.setText("Final score: " + points);
            mAnswerEditText.setVisibility(View.INVISIBLE);
            mStudyAgainButton.setVisibility(View.VISIBLE);
            mSubmitButton.setText("Study a New Deck");
            mShowAnswerButton.setText("Rate this Deck");
            int timesCompleted = mDeck.getTimesCompleted();
            Log.d("Times Completed", timesCompleted+"");
            mDeckRef.child("timesCompleted").setValue(timesCompleted-1);
            mDeck.setTimesCompleted(timesCompleted-1);
        } else {
            mCards.remove(mCard);
            int position = randomNumberGenerator.nextInt(mCards.size());
            createCardFragment(position);
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
        deckUsersRatingRef.setValue(-rating);
        Toast.makeText(StudyActivity.this, "You rated " + mDeck.getName() + " at " + rating + " stars.", Toast.LENGTH_SHORT).show();
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            mCardContainer.animate().translationX(1000).setDuration(300);
            Timer cardAnimationTimer = new Timer();
            TimerTask cardAnimationTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.d("it", "works");
                    String newCardQuestion = mCard.getQuestion();
                    int position = 0;
                    if(mCards.size() > 1) {
                        while(newCardQuestion.equals(mCard.getQuestion())) {
                            position = randomNumberGenerator.nextInt(mCards.size());
                            newCardQuestion = mCards.get(position).getQuestion();
                        }
                        createCardFragment(position);
                        mCardContainer.setX(0);
                    }
                }
            };
            timer.schedule(cardAnimationTimerTask, 350);
            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }
}
