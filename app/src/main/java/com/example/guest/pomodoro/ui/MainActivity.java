package com.example.guest.pomodoro.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.guest.pomodoro.R;
import com.example.guest.pomodoro.game.GameActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.makeDeckButton) Button mDeckButton;
    @Bind(R.id.studyButton) Button mStudyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDeckButton.setOnClickListener(this);
        mStudyButton.setOnClickListener(this);
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
        }
    }
}
