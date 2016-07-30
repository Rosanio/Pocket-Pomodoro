package com.epicodus.pocketpomodoro.views;

/**
 * Created by Matt on 7/30/2016.
 */
public interface CreateUserNavigationListener {
    void showErrorToast(String message);
    void createAuthProgressDialog();
    void showAuthProgressDialog();
    void hideAuthProgressDialog();
    void navigateToMainActivity();
    boolean checkNetworkConnection();
}
