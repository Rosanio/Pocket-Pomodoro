/*todo:
    error handling for empty input fields
    make sure no two cards can have the same name
*/

package com.example.guest.pomodoro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
    @Bind(R.id.studyButton) Button mStudyButton;
    @Bind(R.id.languageSpinner) Spinner mLanguageSpinner;
    @Bind(R.id.translateQuestionButton) Button mTranslateQuestionButton;
    ArrayList<String> questions = new ArrayList<String>();
    ArrayList<String> answers = new ArrayList<String>();
    ArrayAdapter adapter;
    String[] languages = {"Spanish", "French", "German", "Italian"};

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

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, languages);
        mLanguageSpinner.setAdapter(spinnerAdapter);

        mAddCardButton.setOnClickListener(this);
        mStudyButton.setOnClickListener(this);

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
                mQuestionEditText.setText("");
                mAnswerEditText.setText("");
                mQuestionEditText.requestFocus();
                break;
            case R.id.studyButton:
                Intent intent = new Intent(CreateDeckActivity.this, StudyActivity.class);
                intent.putExtra("questions", questions);
                intent.putExtra("answers", answers);
                startActivity(intent);
        }
    }
}
