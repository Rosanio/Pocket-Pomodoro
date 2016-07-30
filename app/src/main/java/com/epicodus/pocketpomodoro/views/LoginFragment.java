package com.epicodus.pocketpomodoro.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.epicodus.pocketpomodoro.Constants;
import com.epicodus.pocketpomodoro.R;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;

/**
 * Fragment for associated LoginActivity
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    final Pattern emailPattern = Pattern.compile(
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    @Bind(R.id.logInEmailEditText) EditText mEmailEditText;
    @Bind(R.id.logInPasswordEditText) EditText mPasswordEditText;
    @Bind(R.id.logInButton) Button mLogInButton;
    @Bind(R.id.signUpTextView) TextView mSignUpTextView;

    private Firebase mFirebaseRef;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    private LoginNavigationListener mLoginNavigationListener;

    private Subscription mEmailEditTextSubscription;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mLoginNavigationListener = (LoginNavigationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);

        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mFirebaseRef = new Firebase(Constants.FIREBASE_ROOT_URL);

        mLogInButton.setOnClickListener(this);
        mSignUpTextView.setOnClickListener(this);

        mPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch(keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            loginWithPassword();
                            return true;
                    }
                }
                return false;
            }
        });

        mLoginNavigationListener.createAuthProgressDialog();
        String signUpEmail = mSharedPreferences.getString(Constants.KEY_USER_EMAIL, null);
        if(signUpEmail != null) {
            mEmailEditText.setText(signUpEmail);
        }

        if(mEmailEditTextSubscription != null) {
            mEmailEditTextSubscription.unsubscribe();
        }

         mEmailEditTextSubscription = RxTextView.textChanges(mEmailEditText)
                .map(t -> emailPattern.matcher(t).matches())
                .map(b -> b ? Color.BLACK : Color.RED)
                .subscribe(color -> {mEmailEditText.setTextColor(color);Log.d("color", color.toString());});

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.signUpTextView:
                mLoginNavigationListener.navigateToSignIn();
                break;
            case R.id.logInButton:
                loginWithPassword();
        }
    }

    public void loginWithPassword() {
        final String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if(email.equals("")) {
            mEmailEditText.setError("Please enter your email");
            return;
        }
        if(password.equals("")) {
            mPasswordEditText.setError("Password cannot be blank");
            return;
        }

        if(mLoginNavigationListener.checkNetworkConnection()) {
            mLoginNavigationListener.showProgressDialog();
            mFirebaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    mLoginNavigationListener.hideProgressDialog();
                    mSharedPreferencesEditor.putString(Constants.KEY_USER_EMAIL, email).apply();
                    if(authData != null) {
                        String userUid = authData.getUid();
                        mSharedPreferencesEditor.putString(Constants.KEY_UID, userUid).apply();
                        mLoginNavigationListener.navigateToMainActivity();
                    }
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    mLoginNavigationListener.hideProgressDialog();
                    switch(firebaseError.getCode()) {
                        case FirebaseError.INVALID_EMAIL:
                        case FirebaseError.USER_DOES_NOT_EXIST:
                            mEmailEditText.setError("Please check that you entered your email correctly");
                            break;
                        case FirebaseError.INVALID_PASSWORD:
                            mEmailEditText.setError(firebaseError.getMessage());
                            break;
                        case FirebaseError.NETWORK_ERROR:
                            mLoginNavigationListener.showErrorToast("There was a problem with the network connection");
                            break;
                        default:
                            mLoginNavigationListener.showErrorToast(firebaseError.toString());
                    }
                }
            });
        } else {
            mLoginNavigationListener.showErrorToast("You are not connected to the internet");
        }


    }

}
