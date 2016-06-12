package com.example.guest.pomodoro.models;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by Guest on 5/6/16.
 */
@Parcel
public class Deck {

    String name;
    String category;
    float rating;
    long date;
    String id;

    public Deck() {}

    public Deck(String name, String category) {
        this.name = name;
        this.category = category;
        this.rating = 0;
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
