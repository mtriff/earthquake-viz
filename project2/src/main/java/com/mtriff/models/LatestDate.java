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
public class LatestDate {
    @MongoId
    private String id;
    private String theTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTheTime() {
        return theTime;
    }

    public void setTheTime(String theTime) {
        this.theTime = theTime;
    }
    
    
}
