/*todo:
    wrap questions in a scroll view, possibly adjust text size based on length
    try and incorporate timer
    add warnings for locking phone and how to use accents
    maybe prompt user for time to spend studying
    try to incorporate fragments somehow, maybe scroll through flash cards, just for this week hopefully
    on completion, display options to study the same deck or pick a new one
    maybe just display option to study same deck if score is not high enough
 */

package com.example.guest.pomodoro.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.adapters.CardPagerAdapter;
import com.example.guest.pomodoro.models.QA;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StudyActivity extends AppCompatActivity implements View.OnClickListener {

    //    @Bind(R.id.pointsTextView) TextView mPointsTextView;
    ArrayList<QA> mQas = new ArrayList<>();
    ArrayList<String> answeredQuestions = new ArrayList<>();
    @Bind(R.id.pointsTextView) TextView mPointsTextView;
    @Bind(R.id.answerEditText) EditText mAnswerEditText;
    @Bind(R.id.submitButton) Button mSubmitButton;
    @Bind(R.id.resultsTextView) TextView mResultsTextView;
    @Bind(R.id.adjustPointsTextView) TextView mAdjustPointsTextView;
    @Bind(R.id.viewPager) ViewPager mViewPager;
    @Bind(R.id.playAgainButton) Button mPlayAgainButton;
    private CardPagerAdapter adapterViewPager;
    private boolean won = false;

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

        Intent intent = getIntent();
        mQas = Parcels.unwrap(intent.getParcelableExtra("qas"));
        adapterViewPager = new CardPagerAdapter(getSupportFragmentManager(), mQas);
        mViewPager.setAdapter(adapterViewPager);
        mSubmitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitButton:

                index = mViewPager.getCurrentItem();
                if(answeredQuestions.size()==0) {
                    guessAnswer(index);
                } else {
                    Boolean contains = false;
                    for(int i = 0; i < answeredQuestions.size(); i++) {
                        if(answeredQuestions.get(i).equals(mQas.get(index).getQuestion())) {
                            contains = true;
                        }
                    }
                    if(!contains) {
                        guessAnswer(index);
                    } else {
                        Toast.makeText(this, "You've already answered this question", Toast.LENGTH_LONG).show();
                    }
                }
                if(answeredQuestions.size()==mQas.size()) {
                    won = true;
                    mResultsTextView.setText("You've correctly guessed all questions!");
                    mAdjustPointsTextView.setText("Final score: " + points);
                    mAnswerEditText.setVisibility(View.INVISIBLE);
                    mPlayAgainButton.setVisibility(View.VISIBLE);
                }

        }

    }

    public void guessAnswer(int index) {
        String answer = mAnswerEditText.getText().toString();
        mAnswerEditText.setText("");
        mResultsTextView.setText("Your Answer: " + answer);
        if (answer.toLowerCase().equals(mQas.get(index).getAnswer().toLowerCase())) {
            answeredQuestions.add(mQas.get(index).getQuestion());
            points += 1;
            mPointsTextView.setText(String.valueOf(points));
            mAdjustPointsTextView.setText("+1 points");
        } else {
            points -= 1;
            mPointsTextView.setText(String.valueOf(points));
            mAdjustPointsTextView.setText("-1 points");
        }
    }
}
