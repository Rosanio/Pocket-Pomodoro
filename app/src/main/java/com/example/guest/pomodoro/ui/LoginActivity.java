package com.example.guest.pomodoro.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.guest.pomodoro.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.logInEmailEditText) EditText mEmailEditText;
    @Bind(R.id.logInPasswordEditText) EditText mPasswordEditText;
    @Bind(R.id.logInButton) Button mLogInButton;
    @Bind(R.id.signUpTextView) TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mSignUpTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.signUpTextView:
                Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
                startActivity(intent);
                finish();
        }
    }
}
