package com.epicodus.pocketpomodoro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.models.Card;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matt on 9/25/2016.
 */
public class MyCardsViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.questionTextView)
    TextView mQuestionTextView;
    @Bind(R.id.answerTextView) TextView mAnswerTextView;

    private Context mContext;
    private ArrayList<Card> mCards;

    public MyCardsViewHolder(View itemView, ArrayList<Card> cards) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mCards = cards;
        itemView.setOnClickListener(v -> {
            int position = getLayoutPosition();
            Card card = mCards.get(position);
            Log.d("Question: ", card.getQuestion());
        });
    }

    public void bindCard(Card card) {
        mQuestionTextView.setText(card.getQuestion());
        mAnswerTextView.setText(card.getAnswer());
    }
}
