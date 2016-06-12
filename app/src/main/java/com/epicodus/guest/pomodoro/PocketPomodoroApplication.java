package com.epicodus.guest.pomodoro;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Guest on 5/6/16.
 */
public class PocketPomodoroApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
