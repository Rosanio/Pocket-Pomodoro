package com.epicodus.pocketpomodoro.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.models.Deck;
import com.epicodus.pocketpomodoro.views.MyDecksContract;
import com.epicodus.pocketpomodoro.views.StudyActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matt on 9/24/2016.
 */
public class UserDecksViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.nameTextView)
    TextView mNameTextView;
    @Bind(R.id.categoryTextView) TextView mCategoryTextView;
    @Bind(R.id.dateTextView) TextView mDateTextView;
    @Bind(R.id.ratingTextView) TextView mRatingTextView;
    @Bind(R.id.timesCompletedTextView) TextView mTimesCompletedTextView;

    private Context mContext;
    private MyDecksContract mActivity;

    private ArrayList<Deck> mDecks = new ArrayList<>();

    public UserDecksViewHolder(View itemView, ArrayList<Deck> decks) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mActivity = (MyDecksContract) mContext;
        mDecks = decks;
        itemView.setOnClickListener(v -> {
            mActivity.createAdjustDeckDialog();
        });
    }

    public void bindDeck(Deck deck) {
        mNameTextView.setText(deck.getName());
        mCategoryTextView.setText("Category: " + deck.getCategory());
        mDateTextView.setText((new Date(-deck.getDate())).toString());
        int starEmoji = 0x2B50;
        String starString = new String(Character.toChars(starEmoji));
        String ratingString = starString;
        double rating = deck.getRating();
        if(rating <= -1.50) {
            ratingString += starString;
        }
        if(rating <= -2.50) {
            ratingString += starString;
        }
        if(rating <= -3.50) {
            ratingString += starString;
        }
        if(rating <= -4.50) {
            ratingString += starString;
        }
        if(rating == 0) {
            ratingString = "Not yet rated";
            mRatingTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        }
        mRatingTextView.setText(ratingString);
        mTimesCompletedTextView.setText("Times Played: " + -deck.getTimesCompleted());
    }
}
