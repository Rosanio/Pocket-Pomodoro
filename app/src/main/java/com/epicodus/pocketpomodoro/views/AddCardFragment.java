package com.epicodus.pocketpomodoro.views;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.epicodus.pocketpomodoro.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCardFragment extends DialogFragment implements TextView.OnEditorActionListener {
    @Bind(R.id.newCardQuestionEditText) EditText mQuestionEditText;
    @Bind(R.id.newCardAnswerEditText) EditText mAnswerEditText;

    public interface AddCardDialogListener {
        void onFinishEditDialog(String questionText, String answerText);
    }

    public static AddCardFragment newInstance() {
        AddCardFragment frag = new AddCardFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_card, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstance) {
        super.onViewCreated(v, savedInstance);
        mAnswerEditText.setOnEditorActionListener(this);
        mQuestionEditText.requestFocus();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(EditorInfo.IME_ACTION_DONE == actionId) {
            AddCardDialogListener listener = (AddCardDialogListener) getActivity();
            listener.onFinishEditDialog(mQuestionEditText.getText().toString(), mAnswerEditText.getText().toString());
            dismiss();
            return true;
        }
        return false;
    }

}
