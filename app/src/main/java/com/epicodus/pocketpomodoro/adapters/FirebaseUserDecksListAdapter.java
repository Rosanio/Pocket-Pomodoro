package com.epicodus.pocketpomodoro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.models.Deck;
import com.epicodus.pocketpomodoro.util.FirebaseRecyclerAdapter;
import com.firebase.client.Query;

/**
 * Created by Matt on 9/24/2016.
 */
public class FirebaseUserDecksListAdapter extends FirebaseRecyclerAdapter<UserDecksViewHolder, Deck> {
    public FirebaseUserDecksListAdapter(Query query, Class<Deck> itemClass) {
        super(query, itemClass);
    }

    @Override
    public UserDecksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_list_item, parent, false);
        return new UserDecksViewHolder(view, getItems());
    }

    @Override
    public void onBindViewHolder(UserDecksViewHolder holder, int position) {
        holder.bindDeck(getItem(position));
    }

    @Override
        protected void itemAdded(Deck item, String key, int position) {

    }

    @Override
        protected void itemChanged(Deck oldItem, Deck newItem, String key, int position) {

    }

    @Override
    protected void itemRemoved(Deck item, String key, int position) {

    }

    @Override
    protected void itemMoved(Deck item, String key, int oldPosition, int newPosition) {

    }
}
