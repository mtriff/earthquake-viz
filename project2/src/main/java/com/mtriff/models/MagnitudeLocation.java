/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtriff.models;

import org.jongo.marshall.jackson.oid.MongoId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getDate() {
        return myDayOfYear;
    }

    public void setDate(String myDayOfYear) {
        this.myDayOfYear = myDayOfYear;
    }

    public int getHour() {
        return myHourOfDay;
    }

    public void setHour(int myHourOfDay) {
        this.myHourOfDay = myHourOfDay;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
   
    
}
