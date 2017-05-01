package com.yakkertrakker.www.yakkertrakker.model;

/**
 * Created by ryanpetit787 on 4/11/17.
 */

public class Location {
    private float lon;
    private float lat;
    private String country;
    private String city;
    private long lastupdate;

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
