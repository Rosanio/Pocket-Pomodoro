package com.epicodus.guest.pomodoro.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.epicodus.guest.pomodoro.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddDeckFragment extends DialogFragment implements TextView.OnEditorActionListener {
    @Bind(R.id.deckNameEditText) EditText mDeckNameEditText;
    @Bind(R.id.deckCategoryEditText) EditText mDeckCategoryEditText;

    public interface AddDeckDialogListener {
        void onFinishEditDialog(String nameText, String categoryText);
    }

    public static AddDeckFragment newInstance(String title) {
        AddDeckFragment frag = new AddDeckFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_deck, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        String title = getArguments().getString("title", "Enter deck information");
        getDialog().setTitle(title);

        mDeckCategoryEditText.setOnEditorActionListener(this);

        mDeckNameEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(EditorInfo.IME_ACTION_DONE == actionId) {
            AddDeckDialogListener listener = (AddDeckDialogListener) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentDisplay);
            listener.onFinishEditDialog(mDeckNameEditText.getText().toString(), mDeckCategoryEditText.getText().toString());
            dismiss();
            return true;
        }
        return false;
    }

}
