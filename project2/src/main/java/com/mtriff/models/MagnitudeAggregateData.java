/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtriff.models;

import org.jongo.marshall.jackson.oid.MongoId;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;

/**
 *
 * @author macdre
 */
public class MagnitudeAggregateData {
    
    @MongoId
    private String id;
    private String myDayOfYear;
    private int myHourOfDay;
    private double magnitude;
    private int count;
    
    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }
    
    @JsonProperty("QuakeDate")
    public String getDate() {
        return myDayOfYear;
    }

    @JsonProperty("QuakeDate")
    public void setDate(String myDayOfYear) {
        this.myDayOfYear = myDayOfYear;
    }

    @JsonProperty("QuakeHour")
    public int getHour() {
        return myHourOfDay;
    }

    @JsonProperty("QuakeHour")
    public void setHour(int myHourOfDay) {
        this.myHourOfDay = myHourOfDay;
    }

    @JsonProperty("Magnitude")
    public String getMagnitude() {
        return Double.valueOf(magnitude).toString();
    }

    @JsonProperty("Magnitude")
    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    @JsonProperty("Count")
    public int getCount() {
        return count;
    }

    @JsonProperty("Count")
    public void setCount(int count) {
        this.count = count;
    }
    
    
    
}
