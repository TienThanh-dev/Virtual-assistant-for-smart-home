package com.example.appsmartphone.model;

public class Database {
    double gas;
    double humidity;
    String ip;
    double toxicGas;
    double temp;
    boolean livingRoomLight;
    boolean masterBedroomLight;
    boolean smallBedroomLight;
    boolean masterBathroomLight;
    boolean publicBathroomLight;
    boolean ventilation;
    String statusDoor;
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
    public double getTemp() {
        return temp;
    }
    public void setTemp(double temp) {
        this.temp = temp;
    }
    public double getToxicGas() {
        return toxicGas;
    }
    public void setToxicGas(double toxicGas) {
        this.toxicGas = toxicGas;
    }
    public boolean getLivingRoomLight() { return livingRoomLight; }
    public void setLivingRoomLight(boolean livingRoomLight) {
        this.livingRoomLight = livingRoomLight;
    }
    public boolean getMasterBedroomLight() {
        return masterBedroomLight;
    }
    public void setMasterBedroomLight(boolean masterBedroomLight) {
        this.masterBedroomLight = masterBedroomLight;
    }
    public boolean getSmallBedroomLight() {
        return smallBedroomLight;
    }
    public void setSmallBedroomLight(boolean smallBedroomLight) {
        this.smallBedroomLight = smallBedroomLight;
    }
    public boolean getMasterBathroomLight() {
        return masterBathroomLight;
    }
    public void setMasterBathroomLight(boolean masterBathroomLight) {
        this.masterBathroomLight = masterBathroomLight;
    }
    public boolean getPublicBathroomLight() {
        return publicBathroomLight;
    }
    public void setPublicBathroomLight(boolean publicBathroomLight) {
        this.publicBathroomLight = publicBathroomLight;
    }
    public boolean getVentilation() {
        return ventilation;
    }
    public void setVentilation(boolean ventilation) {
        this.ventilation = ventilation;
    }
    public String getStatusDoor() {
        return statusDoor;
    }
    public void setStatusDoor(String statusDoor) {
        this.statusDoor = statusDoor;
    }

}
