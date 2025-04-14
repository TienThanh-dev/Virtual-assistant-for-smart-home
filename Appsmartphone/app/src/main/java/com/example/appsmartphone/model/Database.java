package com.example.appsmartphone.model;

public class Database {
    private double gas;
    private double humidity;
    private String ip;
    private double light;
    private double temp;
    public Database() {}
    public double getGas() {
        return gas;
    }
    public void setGas(double gas) {
        this.gas = gas;
    }
    public double getHumidity() {
        return humidity;
    }
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public double getLight() {
        return light;
    }
    public void setLight(double light) {
        this.light = light;
    }
    public double getTemp() {
        return temp;
    }
    public void setTemp(double temp) {
        this.temp = temp;
    }
}
