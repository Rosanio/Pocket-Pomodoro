package com.example.guest.pomodoro;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StudyActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.pointsTextView) TextView mPointsTextView;
    @Bind(R.id.cardTextView) TextView mCardTextView;
    @Bind(R.id.answerEditText) EditText mAnswerEditText;
    @Bind(R.id.submitButton) Button mSubmitButton;
    @Bind(R.id.resultsTextView) TextView mResultsTextView;
    @Bind(R.id.adjustPointsTextView) TextView mAdjustPointsTextView;
    ArrayList<String> questions;
    ArrayList<String> answers;
    ArrayList<String> answeredQuestions = new ArrayList<String>();
    Random randomNumberGenerator = new Random();
    int index;
    int points;

    public void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(StudyActivity.this);
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
        setContentView(R.layout.activity_study);
        ButterKnife.bind(this);
        setupUI(findViewById(R.id.parentContainer));

        Intent intent = getIntent();
        questions = intent.getStringArrayListExtra("questions");
        answers = intent.getStringArrayListExtra("answers");
        index = randomNumberGenerator.nextInt(questions.size());
        mCardTextView.setText(questions.get(index));
        mSubmitButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submitButton:
                String answer = mAnswerEditText.getText().toString();
                mAnswerEditText.setText("");
                mResultsTextView.setText("Your Answer: " + answer);
                if(answer.toLowerCase().equals(answers.get(index).toLowerCase())) {
                    points += 1;
                    mPointsTextView.setText(String.valueOf(points));
                    mAdjustPointsTextView.setText("+1 points");
                    answeredQuestions.add(questions.get(index));
                } else {
                    points -= 1;
                    mPointsTextView.setText(String.valueOf(points));
                    mAdjustPointsTextView.setText("-1 points");
                }
                if(answeredQuestions.size() == questions.size()) {
                    mCardTextView.setText("You've completed this deck!");
                    mAdjustPointsTextView.setText("Final Score: " + String.valueOf(points) + " points");
                    mCardTextView.setTextSize(24);
                } else {
                    boolean foundQuestion = false;
                    while(!foundQuestion) {
                        index = randomNumberGenerator.nextInt(questions.size());
                        if(!(answeredQuestions.contains(questions.get(index)))) {
                            foundQuestion = true;
                        }
                    }
                    mCardTextView.setText(questions.get(index));
                }
        }

    }
}
