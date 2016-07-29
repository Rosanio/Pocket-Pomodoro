package com.epicodus.pocketpomodoro.views;

/**
 * Created by Matt on 7/29/2016.
 */
public interface LoginNavigationListener {
    void navigateToSignIn();
    void navigateToMainActivity();
    boolean checkNetworkConnection();
    void showErrorToast(String message);
    void createAuthProgressDialog();
    void showProgressDialog();
    void hideProgressDialog();
}
