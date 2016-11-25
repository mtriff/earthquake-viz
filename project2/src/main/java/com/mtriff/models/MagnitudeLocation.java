/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtriff.models;

import org.jongo.marshall.jackson.oid.MongoId;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author macdre
 */
//@Entity("monthly")
public class MagnitudeLocation {

//    @Id
    @MongoId
    private String id;
    private String myDayOfYear;
    private int myHourOfDay;
    private float magnitude;
    private float latitude;
    private float longitude;

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }
    
    @JsonProperty("Date")
    public String getDate() {
        return myDayOfYear;
    }

    @JsonProperty("Date")
    public void setDate(String myDayOfYear) {
        this.myDayOfYear = myDayOfYear;
    }

    @JsonProperty("Hour")
    public int getHour() {
        return myHourOfDay;
    }

    @JsonProperty("Hour")
    public void setHour(int myHourOfDay) {
        this.myHourOfDay = myHourOfDay;
    }

    @JsonProperty("Magnitude")
    public float getMagnitude() {
        return magnitude;
    }

    @JsonProperty("Magnitude")
    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    @JsonProperty("Latitude")
    public float getLatitude() {
        return latitude;
    }

    @JsonProperty("Latitude")
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("Longitude")
    public float getLongitude() {
        return longitude;
    }

    @JsonProperty("Longitude")
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
   
    
}
