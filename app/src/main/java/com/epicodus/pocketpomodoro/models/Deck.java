package com.epicodus.pocketpomodoro.models;

import org.parceler.Parcel;

/**
 * Created by Guest on 5/6/16.
 */
@Parcel
public class Deck {

    String name;
    String category;
    float rating;
    //date is saved as a long so that it will display by most recent on select deck screen, but can still be displayed property to the user
    long date;
    String id;
    int timesCompleted;

    public Deck() {}

    public Deck(String name, String category) {
        this.name = name;
        this.category = category;
        this.rating = 0;
        this.timesCompleted = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public float getRating() {
        return rating;
    }

    public long getDate() {
        return date;
    }

    public int getTimesCompleted() {
        return timesCompleted;
    }

    public void setTimesCompleted(int timesCompleted) {
        this.timesCompleted = timesCompleted;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

}
