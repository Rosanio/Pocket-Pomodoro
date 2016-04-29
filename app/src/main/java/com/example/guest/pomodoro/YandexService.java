package com.example.guest.pomodoro;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Guest on 4/29/16.
 */
public class YandexService {

    public static void translateText(String text, String language, Callback callback) {
        String API_KEY = Constants.YANDEX_API_KEY;

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.YANDEX_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.YANDEX_KEY_QUERY_PARAMETER, Constants.YANDEX_API_KEY);
        urlBuilder.addQueryParameter(Constants.YANDEX_TEXT_QUERY_PARAMETER, text);
        urlBuilder.addQueryParameter(Constants.YANDEX_LANGUAGE_QUERY_PARAMETER, language);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public ArrayList<QA> processResults(ArrayList<QA> qas, String question, Response response) {
        ArrayList<QA> newQas = qas;

        try {
            String jsonData = response.body().string();
            if(response.isSuccessful()) {
                JSONObject yandexJSON = new JSONObject(jsonData);
                String languageTranslation = yandexJSON.getString("lang");
                String translatedText = yandexJSON.getJSONArray("text").get(0).toString();
                QA qa  = new QA(question, translatedText);
                newQas.add(qa);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newQas;
    }
}
