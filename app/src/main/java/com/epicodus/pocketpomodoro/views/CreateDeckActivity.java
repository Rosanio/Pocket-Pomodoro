
package com.epicodus.pocketpomodoro.views;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.epicodus.pocketpomodoro.models.Card;
import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.util.OnCardAddedListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class CreateDeckActivity extends AppCompatActivity implements OnCardAddedListener {
    private ArrayList<Card> mCards;

    public void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if(!(view instanceof EditText || view instanceof Button)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(CreateDeckActivity.this);
                    findViewById(R.id.parentContainer).requestFocus();
                    return false;
                }
            });
        }

        if(view instanceof ViewGroup) {
            for(int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            mCards = Parcels.unwrap(savedInstanceState.getParcelable("cards"));
            if(mCards != null) {
                this.onCardAdded(mCards);
            }
            CreateDeckInputFragment inputFrag = (CreateDeckInputFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentInput);
            if(inputFrag != null) {
                inputFrag.setCards(mCards);
            }
        }
        setContentView(R.layout.activity_create_deck);
        ButterKnife.bind(this);
        setupUI(findViewById(R.id.parentContainer));
    }

    @Override
    //this method is called from the CreateDeckInputFragment, and will replace the current value of mCards with the passed in arraylist cards. Then it finds the instance of CreateDeckDisplayFragment which it contains and calls its updateCardsList method, passing in the updated list of cards.
    public void onCardAdded(ArrayList<Card> cards) {
        mCards = cards;
        CreateDeckDisplayFragment displayFrag = (CreateDeckDisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentDisplay);
        if(displayFrag != null) {
            displayFrag.updateCardsList(mCards);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(mCards != null) {
            outState.putParcelable("cards", Parcels.wrap(mCards));
        }
        super.onSaveInstanceState(outState);
    }
}
