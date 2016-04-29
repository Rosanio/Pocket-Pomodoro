package com.example.guest.pomodoro.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.models.QA;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CardFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.pointsTextView) TextView mPointsTextView;
    @Bind(R.id.cardTextView) TextView mCardTextView;
    @Bind(R.id.answerEditText) EditText mAnswerEditText;
    @Bind(R.id.submitButton) Button mSubmitButton;
    @Bind(R.id.resultsTextView) TextView mResultsTextView;
    @Bind(R.id.adjustPointsTextView) TextView mAdjustPointsTextView;

    int points = 0;
    private QA mQa;

    public static CardFragment newInstance(QA qa) {
        CardFragment cardFragment = new CardFragment();
        Bundle args = new Bundle();
        args.putParcelable("qa", Parcels.wrap(qa));
        cardFragment.setArguments(args);
        return cardFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mQa = Parcels.unwrap(getArguments().getParcelable("qa"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.bind(this, view);
        mPointsTextView.setText(points+"");
        mCardTextView.setText(mQa.getQuestion());
        mSubmitButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submitButton:
                String answer = mAnswerEditText.getText().toString();
                if(answer.equals(mQa.getAnswer())) {
                    Log.d("it", "works");
                    points++;
                    mPointsTextView.setText(points+"");
                }
        }
    }

}
