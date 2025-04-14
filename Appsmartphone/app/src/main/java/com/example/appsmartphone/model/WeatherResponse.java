package com.example.appsmartphone.model;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private Weather[] weather;

    @SerializedName("name")
    private String name;

    public Main getMain() {
        return main;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public String getName() {
        return name;
    }
}
