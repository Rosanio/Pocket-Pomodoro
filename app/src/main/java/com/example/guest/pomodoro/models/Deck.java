package com.example.guest.pomodoro.models;

/**
 * Created by Guest on 5/6/16.
 */
public class Deck {

    String name;
    String category;
    double rating;

    String id;

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

    public double getRating() {
        return rating;
    }

    public void setId(String id) {
        this.id = id;
    }

}
