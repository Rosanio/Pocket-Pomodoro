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

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    ArrayList<TranslatedText> translatedTexts = new ArrayList<>();
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
        mTranslateQuestionButton.setOnClickListener(this);

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
                break;
            case R.id.translateQuestionButton:
                Log.d("it","works");
                String language = mLanguageSpinner.getSelectedItem().toString();
                String text = mQuestionEditText.getText().toString();
                translateText(text, language);
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
                translatedTexts = yandexService.processResults(response);

                CreateDeckActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String translatedText = translatedTexts.get(0).getText();
                        questions.add(question);
                        answers.add(translatedText);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }
}
