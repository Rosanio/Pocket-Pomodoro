package com.epicodus.guest.pomodoro;

/**
 * Created by Guest on 4/29/16.
 */
public class Constants {
    public static final String YANDEX_API_KEY = BuildConfig.YANDEX_API_KEY;

    public static final String YANDEX_BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
    public static final String YANDEX_TEXT_QUERY_PARAMETER = "text";
    public static final String YANDEX_LANGUAGE_QUERY_PARAMETER = "lang";
    public static final String YANDEX_KEY_QUERY_PARAMETER = "key";

    public static final String FIREBASE_ROOT_URL = BuildConfig.FIREBASE_ROOT_URL;

    public static final String FIREBASE_LOCATION_USERS = "users";
    public static final String FIREBASE_PROPERTY_EMAIL = "email";
    public static final String KEY_UID = "UID";
    public static final String FIREBASE_URL_USERS = FIREBASE_ROOT_URL + "/" + FIREBASE_LOCATION_USERS;
    public static final String FIREBASE_LOCATION_DECKS = "decks";
    public static final String FIREBASE_URL_DECKS = FIREBASE_ROOT_URL + "/" + FIREBASE_LOCATION_DECKS;
    public static final String FIREBASE_LOCATION_CARDS = "cards";
    public static final String FIREBASE_URL_CARDS = FIREBASE_ROOT_URL + "/" + FIREBASE_LOCATION_CARDS;

    public static final String KEY_USER_EMAIL = "email";
}
