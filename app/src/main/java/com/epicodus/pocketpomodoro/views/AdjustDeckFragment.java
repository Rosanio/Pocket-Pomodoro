package com.epicodus.pocketpomodoro.views;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.models.Deck;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdjustDeckFragment extends DialogFragment implements View.OnClickListener {

    @Bind(R.id.updateDeckButton) Button mUpdateButton;
    @Bind(R.id.deleteDeckButton) Button mDeleteButton;

    private static int mItemPosition;
    private static ArrayList<Deck> mDecks;
    private Deck mDeck;

    public static AdjustDeckFragment newInstance(int itemPosition, ArrayList<Deck> decks) {
        AdjustDeckFragment frag = new AdjustDeckFragment();
        mItemPosition = itemPosition;
        mDecks = decks;
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
        getDialog().setTitle("Update or Delete Deck");

        mUpdateButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);

        mDeck = mDecks.get(mItemPosition);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.updateDeckButton:
                Intent intent = new Intent(getActivity(), UpdateDeckActivity.class);
                intent.putExtra("deck", Parcels.wrap(mDeck));
                startActivity(intent);
                break;
            case R.id.deleteDeckButton:
                //firebase magic to delete deck and cards
                break;
            default:
                break;
        }
    }

}
