package com.example.appsmartphone.model;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    private double temp;

    public double getTemp() {
        return temp;
    }
}
