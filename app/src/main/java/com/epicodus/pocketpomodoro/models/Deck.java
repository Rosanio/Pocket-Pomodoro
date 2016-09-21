package com.epicodus.pocketpomodoro.models;

import org.parceler.Parcel;

/**
 * Created by Guest on 5/6/16.
 */
@Parcel
public class Deck {

    String mName;
    String mCategory;
    float mRating;
    //date is saved as a long so that it will display by most recent on select deck screen, but can still be displayed property to the user
    long mDate;
    String mId;
    String mCreatorId;
    int mTimesCompleted;

    public Deck() {}

    public Deck(String name, String category, String creatorId) {
        mName = name;
        mCategory = category;
        mCreatorId = creatorId;
        mRating = 0;
        mTimesCompleted = 0;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getCategory() {
        return mCategory;
    }

    public float getRating() {
        return mRating;
    }

    public long getDate() {
        return mDate;
    }

    public int getTimesCompleted() {
        return mTimesCompleted;
    }

    public String getCreeatorId() {
        return mCreatorId;
    }

    public void setTimesCompleted(int timesCompleted) {
        mTimesCompleted = timesCompleted;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setRating(float rating) {
        mRating = rating;
    }

}
