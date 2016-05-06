package com.example.guest.pomodoro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.models.Deck;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 5/6/16.
 */
public class DeckViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.nameTextView) TextView mNameTextView;
    @Bind(R.id.categoryTextView) TextView mCategoryTextView;
    @Bind(R.id.dateTextView) TextView mDateTextView;
    @Bind(R.id.ratingTextView) TextView mRatingTextView;

    private Context mContext;
    private ArrayList<Deck> mDecks = new ArrayList<>();

    public DeckViewHolder(View itemView, ArrayList<Deck> decks) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mDecks = decks;
        itemView.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               int itemPosition = getLayoutPosition();
               //navigate to study activity
           }
        });
    }

    public void bindDeck(Deck deck) {
        mNameTextView.setText(deck.getName());
        mCategoryTextView.setText("Category: " + deck.getCategory());
        mDateTextView.setText(deck.getDate().toString());
        int starEmoji = 0x2B50;
        String starString = new String(Character.toChars(starEmoji));
        String ratingString = starString;
        double rating = deck.getRating();
        if(rating >= 1.50) {
            ratingString += starString;
        }
        if(rating >= 2.50) {
            ratingString += starString;
        }
        if(rating >= 3.50) {
            ratingString += starString;
        }
        if(rating >= 4.50) {
            ratingString += starString;
        }
        mRatingTextView.setText(ratingString);
    }

}
