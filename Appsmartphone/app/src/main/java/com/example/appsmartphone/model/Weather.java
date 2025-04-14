package com.example.appsmartphone.model;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("description")
    private String description;

    public String getDescription() {
        return description;
    }
}
