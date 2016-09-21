package com.epicodus.pocketpomodoro.models;

/**
 * Created by Guest on 5/6/16.
 */
public class User {
    private String mName;
    private String mEmail;
    private String mId;

    public User() {}

    public User(String name, String email, String id) {
        mName = name;
        mEmail = email;
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getId() {
        return mId;
    }

}
