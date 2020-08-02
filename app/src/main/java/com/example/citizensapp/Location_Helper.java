package com.example.citizensapp;

public class Location_Helper {
    private Double Longitude,Latitude;

    public Location_Helper(Double Longitude, Double Latitude) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }
}
