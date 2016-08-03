package com.epicodus.pocketpomodoro.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

/**
 * Created by Matt on 8/3/2016.
 */
public class Translation {
    @SerializedName("code")
    @Getter private String code;

    @SerializedName("lang")
    @Getter private String lang;

    @SerializedName("text")
    @Getter private List<String> text;
}
