package com.example.guest.pomodoro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 4/29/16.
 */
public class QAListAdapter extends RecyclerView.Adapter<QAListAdapter.QAViewHolder> {
    private ArrayList<QA> mQAs = new ArrayList<>();
    private Context mContext;

    public QAListAdapter(Context context, ArrayList<QA> qas) {
        mContext = context;
        mQAs = qas;
    }

    @Override
    public QAListAdapter.QAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_list_item, parent, false);
        QAViewHolder viewHolder = new QAViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QAListAdapter.QAViewHolder holder, int position) {
        holder.bindQa(mQAs.get(position));
    }

    @Override
    public int getItemCount() {
        return mQAs.size();
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

        public void bindQa(QA qa) {
            mQuestionTextView.setText(qa.getQuestion());
            mAnswerTextView.setText(qa.getAnswer());
        }
    }
}
