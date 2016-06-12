/*todo:
    possibly order decks by top rated (by default) and other options
    possibly categorize decks of cards
    incorporate timer
    have keyboard push up all other elements of page
    possibly add animation
 */

package com.epicodus.guest.pomodoro.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.epicodus.guest.pomodoro.Constants;
import com.epicodus.guest.pomodoro.R;
import com.firebase.client.Firebase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.makeDeckButton) Button mDeckButton;
    @Bind(R.id.studyButton) Button mStudyButton;
    @Bind(R.id.emailTextView) TextView mEmailLabel;
    private Firebase mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFirebaseRef = new Firebase(Constants.FIREBASE_ROOT_URL);

        mDeckButton.setOnClickListener(this);
        mStudyButton.setOnClickListener(this);
        mEmailLabel.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.makeDeckButton:
                Intent studyIntent = new Intent(MainActivity.this, CreateDeckActivity.class);
                startActivity(studyIntent);
                break;
            case R.id.studyButton:
                Intent gameIntent = new Intent(MainActivity.this, SelectDeckActivity.class);
                startActivity(gameIntent);
                break;
            case R.id.emailTextView:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("*/*");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"mmrosani@syr.edu"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pocket Pomodoro Feedback");
                startActivity(emailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void logout() {
        mFirebaseRef.unauth();
        takeUserToLoginScreen();
    }

    private void takeUserToLoginScreen() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
