package com.epicodus.pocketpomodoro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.models.Card;
import com.epicodus.pocketpomodoro.models.Deck;
import com.epicodus.pocketpomodoro.util.FirebaseRecyclerAdapter;
import com.firebase.client.Query;

/**
 * Created by Matt on 9/25/2016.
 */
public class FirebaseMyCardsListAdapter extends FirebaseRecyclerAdapter<MyCardsViewHolder, Card> {
    public FirebaseMyCardsListAdapter(Query query, Class<Card> itemClass) {
        super(query, itemClass);
    }

    @Override
    public MyCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_list_item, parent, false);
        return new MyCardsViewHolder(view, getItems());
    }

    @Override
    public void onBindViewHolder(MyCardsViewHolder holder, int position) {
        holder.bindCard(getItem(position));
    }

    @Override
    protected void itemAdded(Card item, String key, int position) {

    }

    @Override
    protected void itemChanged(Card oldItem, Card newItem, String key, int position) {

    }

    @Override
    protected void itemRemoved(Card item, String key, int position) {

    }

    @Override
    protected void itemMoved(Card item, String key, int oldPosition, int newPosition) {

    }
}
