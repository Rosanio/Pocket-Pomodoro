//todo: move add deck functionality to presenter and check for connection before attempting to add a deck

package com.epicodus.pocketpomodoro.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.adapters.CardListAdapter;
import com.epicodus.pocketpomodoro.contracts.CreateDeckDisplayContract;
import com.epicodus.pocketpomodoro.models.Card;
import com.epicodus.pocketpomodoro.presenters.CreateDeckDisplayPresenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateDeckDisplayFragment extends Fragment implements View.OnClickListener,
        CreateDeckDisplayContract.View,
        AddDeckFragment.AddDeckDialogListener {

    @Bind(R.id.recyclerView) RecyclerView mCardsRecyclerView;
    @Bind(R.id.createDeckButton) Button mCreateDeckButton;

    CardListAdapter adapter;
    private CreateDeckDisplayContract.Presenter mPresenter;

    public CreateDeckDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_deck_display, container, false);
        ButterKnife.bind(this, view);

        Log.d("it", "works");

        mPresenter = new CreateDeckDisplayPresenter(this);

        mCreateDeckButton.setOnClickListener(this);

        adapter = new CardListAdapter(getActivity(), mPresenter.getCards());
        mCardsRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mCardsRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createDeckButton:
                if(mPresenter.getCards().size() > 0) {
                    showNewDeckDialog();
                } else {
                    Toast.makeText(getActivity(), "Please add at least one card", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onFinishEditDialog(String nameText, String categoryText) {
        mPresenter.createDeck(nameText, categoryText);
    }

    public void showNewDeckDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddDeckFragment addDeckDialogFragment = AddDeckFragment.newInstance("Add New Deck:");
        addDeckDialogFragment.show(fm, "fragment_add_deck");
    }

    public void updateCardsList(ArrayList<Card> cards) {
        mPresenter.updateCards(cards);
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void navigateToMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void makeErrorToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

}
