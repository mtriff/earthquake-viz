/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtriff.models;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author macdre
 */
@Entity("monthly")
public class MagnitudeLocation {

    @Id
    private int id;
//    private String date;
//    private int hour;
    private float magnitude;
//    private float latitude;
//    private float longitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public int getHour() {
//        return hour;
//    }
//
//    public void setHour(int hour) {
//        this.hour = hour;
//    }
//
    public float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }
//
//    public float getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(float latitude) {
//        this.latitude = latitude;
//    }
//
//    public float getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(float longitude) {
//        this.longitude = longitude;
//    }
//   
    
}
