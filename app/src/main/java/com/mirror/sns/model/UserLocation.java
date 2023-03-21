package com.mirror.sns.model;

public class UserLocation {
    private String uid;
    private double latitude;
    private double longitude;

    public UserLocation() {}

    public UserLocation(String uid, double latitude, double longitude) {
        this.uid =uid;
        this.latitude =latitude;
        this.longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
