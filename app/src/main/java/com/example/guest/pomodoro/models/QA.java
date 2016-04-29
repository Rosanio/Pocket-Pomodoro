package com.example.guest.pomodoro.models;

import org.parceler.Parcel;

/**
 * Created by Guest on 4/29/16.
 */

@Parcel
public class QA {
    private String mQuestion;
    private String mAnswer;

    public QA() {}

    public QA(String question, String answer) {
        mQuestion = question;
        mAnswer = answer;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getAnswer() {

        return mAnswer;
    }
}
