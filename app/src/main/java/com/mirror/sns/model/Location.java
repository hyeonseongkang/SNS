package com.mirror.sns.model;

public class Location {
    private String uid;
    private double latitude;
    private double longitude;

    public Location() {}

    public Location(String uid, double latitude, double longitude) {
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
}
