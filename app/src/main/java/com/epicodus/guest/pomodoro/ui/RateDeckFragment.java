package com.epicodus.guest.pomodoro.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.epicodus.guest.pomodoro.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateDeckFragment extends DialogFragment {
    @Bind(R.id.ratingBar) RatingBar mRatingBar;

    public interface RateDeckDialogListener {
        void onFinishEditDialog(float rating);
    }

    public static RateDeckFragment newInstance(String title) {
        RateDeckFragment frag = new RateDeckFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rate_deck, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        String title = getArguments().getString("title", "Rate this Deck");
        getDialog().setTitle(title);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RateDeckDialogListener listener = (RateDeckDialogListener) getActivity();
                listener.onFinishEditDialog(mRatingBar.getRating());
                dismiss();
            }
        });
    }



}
