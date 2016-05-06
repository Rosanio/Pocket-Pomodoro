package com.example.guest.pomodoro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.models.Card;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QAListAdapter extends RecyclerView.Adapter<QAListAdapter.QAViewHolder> {
    private ArrayList<Card> mCards = new ArrayList<>();
    private Context mContext;

    public QAListAdapter(Context context, ArrayList<Card> cards) {
        mContext = context;
        mCards = cards;
    }

    @Override
    public QAListAdapter.QAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_list_item, parent, false);
        QAViewHolder viewHolder = new QAViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QAListAdapter.QAViewHolder holder, int position) {
        holder.bindQa(mCards.get(position));
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public class QAViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.questionTextView) TextView mQuestionTextView;
        @Bind(R.id.answerTextView) TextView mAnswerTextView;
        private Context mContext;

        public QAViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindQa(Card card) {
            mQuestionTextView.setText(card.getQuestion());
            mAnswerTextView.setText(card.getAnswer());
        }
    }
}
