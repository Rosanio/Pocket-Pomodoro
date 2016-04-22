package com.example.guest.pomodoro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateDeckActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.questionEditText) EditText mQuestionEditText;
    @Bind(R.id.answerEditText) EditText mAnswerEditText;
    @Bind(R.id.addCardButton) Button mAddCardButton;
    @Bind(R.id.cardsListView) ListView mCardsListView;
    ArrayList<String> questions = new ArrayList<String>();
    ArrayList<String> answers = new ArrayList<String>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deck);
        ButterKnife.bind(this);

        mAddCardButton.setOnClickListener(this);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, questions);
        mCardsListView.setAdapter(adapter);

        mCardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index = (int) l;
                String answer = answers.get(index);
                String question = questions.get(index);
                String currentText = ((TextView) view).getText().toString();
                if(currentText.equals(question)) {
                    ((TextView) view).setText(answer);
                } else {
                    ((TextView) view).setText(question);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addCardButton:
                String question = mQuestionEditText.getText().toString();
                String answer = mAnswerEditText.getText().toString();
                questions.add(question);
                answers.add(answer);
                adapter.notifyDataSetChanged();
        }
    }
}
