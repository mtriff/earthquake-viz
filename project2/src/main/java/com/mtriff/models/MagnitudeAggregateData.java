/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtriff.models;

import org.jongo.marshall.jackson.oid.MongoId;

/**
 *
 * @author macdre
 */
public class MagnitudeAggregateData {
    
    @MongoId
    private String id;
    private String myDayOfYear;
    private int myHourOfDay;
    private int magnitude;
    private int count;
    
    
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

    public int getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(int magnitude) {
        this.magnitude = magnitude;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    
    
}
