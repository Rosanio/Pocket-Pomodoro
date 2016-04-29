package com.example.guest.pomodoro;

/**
 * Created by Guest on 4/29/16.
 */
public class TranslatedText {
    private String mText;
    private String mLanguageTranslation;

    public TranslatedText(String text, String languageTranslation) {
        mText = text;
        mLanguageTranslation = languageTranslation;
    }

    public String getLanguageTranslation() {
        return mLanguageTranslation;
    }

    public String getText() {

        return mText;
    }
}
