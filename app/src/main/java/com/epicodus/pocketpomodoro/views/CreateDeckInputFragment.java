//todo: use RxJava to make api call

package com.epicodus.pocketpomodoro.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.contracts.CreateDeckInputContract;
import com.epicodus.pocketpomodoro.models.Card;
import com.epicodus.pocketpomodoro.presenters.CreateDeckInputPresenter;
import com.epicodus.pocketpomodoro.util.OnCardAddedListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateDeckInputFragment extends Fragment implements View.OnClickListener, CreateDeckInputContract.View {
    @Bind(R.id.questionEditText) EditText mQuestionEditText;
    @Bind(R.id.answerEditText) EditText mAnswerEditText;
    @Bind(R.id.addCardButton) Button mAddCardButton;
    @Bind(R.id.languageSpinner) Spinner mLanguageSpinner;
    @Bind(R.id.translateQuestionButton) Button mTranslateQuestionButton;
    @Bind(R.id.loadingTextView) TextView mLoadingTextView;
    String[] languages = {"Spanish", "French", "German", "Italian"};
    OnCardAddedListener mOnCardAddedListener;
    CreateDeckInputContract.Presenter mPresenter;

    public CreateDeckInputFragment() {
        // Required empty public constructor
    }

    @Override
    //called as soon as a fragment is attached to an activity
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //if the parent activity implements this interface, then mOnCardAddedListener will be set to the instance of OnCardAddedListener which exists in the activity. This means information can be passed to the activity by calling its onCardAdded method.
            mOnCardAddedListener = (OnCardAddedListener) context;
        } catch (ClassCastException e) {
            //if the parent activity does not implement OnCardAddedListener, this error will fire.
            throw new ClassCastException(context.toString() + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_deck_input, container, false);
        ButterKnife.bind(this, view);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, languages);
        mLanguageSpinner.setAdapter(spinnerAdapter);

        mAddCardButton.setOnClickListener(this);
        mTranslateQuestionButton.setOnClickListener(this);

        mQuestionEditText.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN) {
                switch(keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        mAnswerEditText.requestFocus();
                        return true;
                }
            }
            return false;
        });
        mAnswerEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        addCard();
                        return true;
                }
            }
            return false;
        });

        mPresenter = new CreateDeckInputPresenter(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addCardButton:
                addCard();
                break;

            case R.id.translateQuestionButton:
                String language = mLanguageSpinner.getSelectedItem().toString();
                String text = mQuestionEditText.getText().toString();

                if(mPresenter.checkQuestionValidity(text, null)) {
                    if(mOnCardAddedListener.checkNetworkConnection()) {
                        mPresenter.translateQuestion(language, text);
                    } else {
                        makeErrorToast("You are not connected to the internet");
                    }
                }

                mQuestionEditText.setText("");
                mAnswerEditText.setText("");
                mQuestionEditText.requestFocus();
                break;
        }
    }

    public void setCards(ArrayList<Card> cards) {
        if(mPresenter == null) {
            mPresenter = new CreateDeckInputPresenter(this);
        }
        mPresenter.setCards(cards);
    }

    public void addCard() {
        String question = mQuestionEditText.getText().toString();
        String answer = mAnswerEditText.getText().toString();
        if(mPresenter.checkQuestionValidity(question, answer)) {
            mPresenter.addCard(question, answer);
        }

        mQuestionEditText.setText("");
        mAnswerEditText.setText("");
        mQuestionEditText.requestFocus();
    }

    public void setTextTranslate() {
        mLoadingTextView.setText("translating...");
    }

    public void unsetTextTranslate() {
        mLoadingTextView.setText("");
    }

    public void makeErrorToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public void addCards(ArrayList<Card> cards) {
        getActivity().runOnUiThread(() -> {
            mOnCardAddedListener.onCardAdded(cards);
            unsetTextTranslate();
            informActivity(cards);
        });
    }

    public void informActivity(ArrayList<Card> cards) {
        mOnCardAddedListener.onCardAdded(cards);
    }

}
