package com.example.guest.pomodoro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    ArrayList<String> guessedQuestions = new ArrayList<String>();
    Random randomNumberGenerator = new Random();
    int index;
    int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        ButterKnife.bind(this);

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
                mResultsTextView.setText("Your Answer: " + answer);
                if(answer.toLowerCase().equals(answers.get(index).toLowerCase())) {
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
}
