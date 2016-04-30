package com.example.guest.pomodoro.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.game.GameActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.makeDeckButton) Button mDeckButton;
    @Bind(R.id.studyButton) Button mStudyButton;
    @Bind(R.id.emailTextView) TextView mEmailLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
                Log.d("TAG", "it works");
                Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
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
}
