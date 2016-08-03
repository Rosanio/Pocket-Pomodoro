package com.epicodus.pocketpomodoro.models;

import com.epicodus.pocketpomodoro.Constants;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Matt on 8/3/2016.
 */
public interface TranslationService {
    @GET("api/v1.5/tr.json/translate?")
    Observable<Translation> translate(@Query(Constants.YANDEX_KEY_QUERY_PARAMETER) String key, @Query(Constants.YANDEX_LANGUAGE_QUERY_PARAMETER) String language, @Query(Constants.YANDEX_TEXT_QUERY_PARAMETER) String text);
}
