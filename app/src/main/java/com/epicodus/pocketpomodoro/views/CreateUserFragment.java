package com.epicodus.pocketpomodoro.views;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.epicodus.pocketpomodoro.Constants;
import com.epicodus.pocketpomodoro.R;
import com.epicodus.pocketpomodoro.models.User;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateUserFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = CreateUserActivity.class.getSimpleName();

    @Bind(R.id.nameEditText)
    EditText mNameEditText;
    @Bind(R.id.emailEditText) EditText mEmailEditText;
    @Bind(R.id.passwordEditText) EditText mPasswordEditText;
    @Bind(R.id.confirmPasswordEditText) EditText mConfirmPasswordEditText;
    @Bind(R.id.newAccountButton)
    Button mNewAccountButton;
    private Firebase mFirebaseRef;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    private CreateUserNavigationListener mCreateUserNavigationListener;
    public CreateUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCreateUserNavigationListener = (CreateUserNavigationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_user, container, false);

        ButterKnife.bind(this, v);
        mFirebaseRef = new Firebase(Constants.FIREBASE_ROOT_URL);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mNewAccountButton.setOnClickListener(this);

        mConfirmPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            Log.d("it", "works");
                            if(mCreateUserNavigationListener.checkNetworkConnection()) {
                                createNewUser();
                            } else {
                                mCreateUserNavigationListener.showErrorToast("You are not connected to the internet");
                            }
                            return true;
                    }
                }
                return false;
            }
        });
        mCreateUserNavigationListener.createAuthProgressDialog();

        return v;
    }

    @Override
    public void onClick(View view) {
        if(view == mNewAccountButton) {
            if(mCreateUserNavigationListener.checkNetworkConnection()) {
                createNewUser();
            } else {
                mCreateUserNavigationListener.showErrorToast("You are not connected to the internet");
            }
        }
    }

    public void createNewUser() {
        final String name = mNameEditText.getText().toString();
        final String email = mEmailEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();
        final String confirmPassword = mConfirmPasswordEditText.getText().toString();

        boolean validEmail = isValidEmail(email);
        boolean validName = isValidName(name);
        boolean validPassword = isValidPassword(password, confirmPassword);
        if(!validEmail || !validName || !validPassword) return;

        mCreateUserNavigationListener.showAuthProgressDialog();

        mFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                String uid = result.get("uid").toString();
                createUserInFirebaseHelper(name, email, uid);
                mFirebaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        mCreateUserNavigationListener.hideAuthProgressDialog();
                        if(authData != null) {
                            String userUid = authData.getUid();
                            mSharedPreferencesEditor.putString(Constants.KEY_UID, userUid).apply();
                            mCreateUserNavigationListener.navigateToMainActivity();
                        }
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        mCreateUserNavigationListener.hideAuthProgressDialog();
                        switch(firebaseError.getCode()) {
                            case FirebaseError.INVALID_EMAIL:
                            case FirebaseError.USER_DOES_NOT_EXIST:
                                mEmailEditText.setError("Please check that you entered your email correctly");
                                break;
                            case FirebaseError.INVALID_PASSWORD:
                                mEmailEditText.setError(firebaseError.getMessage());
                                break;
                            case FirebaseError.NETWORK_ERROR:
                                mCreateUserNavigationListener.showErrorToast("There was a problem with the network connection");
                                break;
                            default:
                                mCreateUserNavigationListener.showErrorToast(firebaseError.toString());
                        }
                    }
                });
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                mCreateUserNavigationListener.hideAuthProgressDialog();
                Log.d(TAG, "error occured " + firebaseError);
            }
        });
    }

    private void createUserInFirebaseHelper(final String name, final String email, final String uid) {
        final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(uid);
        User newUser = new User(name, email);
        userLocation.setValue(newUser);
    }

    private boolean isValidEmail(String email) {
        boolean isGoodEmail = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if(!isGoodEmail) {
            mEmailEditText.setError("Please enter a valid email");
        }
        return isGoodEmail;
    }

    private boolean isValidName(String name) {
        if(name.equals("")) {
            mNameEditText.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if(password.length() < 4) {
            mPasswordEditText.setError("Please create a password containing at least 4 characters");
            return false;
        } else if(!password.equals(confirmPassword)) {
            mPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }

}
