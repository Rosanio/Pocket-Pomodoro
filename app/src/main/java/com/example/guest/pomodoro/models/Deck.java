package com.example.guest.pomodoro.models;

import java.util.Date;

/**
 * Created by Guest on 5/6/16.
 */
public class Deck {

    String name;
    String category;
    double rating;
    Date date;
    String id;

    public Deck() {}

    public Deck(String name, String category) {
        this.name = name;
        this.category = category;
        this.date = new Date();
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

    public double getRating() {
        return rating;
    }

    public Date getDate() {
        return date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
