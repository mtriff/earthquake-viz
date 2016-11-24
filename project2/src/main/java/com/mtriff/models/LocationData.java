/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtriff.models;

import java.util.ArrayList;
import org.geojson.MultiPolygon;
import org.geojson.Geometry;
import org.jongo.marshall.jackson.oid.MongoId;

/**
 *
 * @author macdre
 */
public class LocationData {
    
    @MongoId
    private String id;
    private String locationName;
    private Geometry<MultiPolygon> geometry;
        
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Geometry<MultiPolygon> getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry<MultiPolygon> polygon) {
        this.geometry = polygon;
    }
}
