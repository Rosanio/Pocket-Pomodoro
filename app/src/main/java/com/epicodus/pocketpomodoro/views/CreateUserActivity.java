//todo: Use RxJava here to make text change colors and check for internet connection before attempting login

package com.epicodus.pocketpomodoro.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epicodus.pocketpomodoro.Constants;
import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.models.User;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateUserActivity extends AppCompatActivity implements CreateUserNavigationListener {

    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

    }

    public void showErrorToast(String message) {
        Toast.makeText(CreateUserActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }

    public void showAuthProgressDialog() {
        mAuthProgressDialog.show();
    }

    public void hideAuthProgressDialog() {
        mAuthProgressDialog.hide();
    }

    public void navigateToMainActivity() {
        Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
