package com.example.guest.pomodoro.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.models.Card;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CardFragment extends Fragment {

    @Bind(R.id.cardTextView) TextView mCardTextView;

    int points = 0;
    private Card mCard;

    public static CardFragment newInstance(Card card) {
        CardFragment cardFragment = new CardFragment();
        Bundle args = new Bundle();
        args.putParcelable("card", Parcels.wrap(card));
        cardFragment.setArguments(args);
        return cardFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mCard = Parcels.unwrap(getArguments().getParcelable("qa"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.bind(this, view);
        mCardTextView.setText(mCard.getQuestion());
        return view;
    }

}
