package com.example.guest.pomodoro.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.models.Card;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CardFragment extends Fragment {
    @Bind(R.id.cardTextView) TextView mCardTextView;
    @Bind(R.id.parentContainer) RelativeLayout mParentContainer;
    int points = 0;
    private Card mCard;
    private GestureDetectorCompat mDetector;

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
        mCard = Parcels.unwrap(getArguments().getParcelable("card"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.bind(this, view);
        mCardTextView.setText(mCard.getQuestion());
        mDetector = new GestureDetectorCompat(getActivity(), new GestureListener());
//        mParentContainer.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                mDetector.onTouchEvent(event);
//                return true;
//            }
//        });
        return view;
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
            return true;
        }
    }

}
