package com.epicodus.pocketpomodoro.views;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.epicodus.pocketpomodoro.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdjustDeckFragment extends DialogFragment implements View.OnClickListener {

    @Bind(R.id.updateDeckButton) Button mUpdateButton;
    @Bind(R.id.deleteDeckButton) Button mDeleteButton;

    public static AdjustDeckFragment newInstance(String title) {
        AdjustDeckFragment frag = new AdjustDeckFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_adjust_deck, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        String title = getArguments().getString("title");
        getDialog().setTitle("Update or Delete Deck");

        mUpdateButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.updateDeckButton:
                //navigate to update activity
                break;
            case R.id.deleteDeckButton:
                //firebase magic to delete deck and cards
                break;
            default:
                break;
        }
    }

}
