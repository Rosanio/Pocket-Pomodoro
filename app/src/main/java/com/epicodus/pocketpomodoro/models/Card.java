package com.epicodus.pocketpomodoro.models;

import org.parceler.Parcel;

/**
 * Created by Guest on 4/29/16.
 */

@Parcel
public class Card {
    String question;
    String answer;
    String id;

    public Card() {}

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
