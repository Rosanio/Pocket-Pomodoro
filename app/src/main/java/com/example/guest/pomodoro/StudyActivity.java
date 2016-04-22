package com.example.guest.pomodoro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StudyActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        questions = intent.getStringArrayListExtra("questions");
        answers = intent.getStringArrayListExtra("answers");
        int index = randomNumberGenerator.nextInt(questions.size());
        mCardTextView.setText(questions.get(index));
    }
}
