package com.mtriff.controller;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mtriff.services.DatabaseAccessObject;

@Path("Magnitude")
public class MagnitudeResource {
	
	DatabaseAccessObject dao;
	
	public MagnitudeResource() {
		dao = new DatabaseAccessObject();
	}
	
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndex() {
        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("Title", "Global Earthquakes by Magnitude");
        dataModel.put("QuakeData", dao.getQuakeData());
    	return FreemakerConfig.getRenderedTemplate("Magnitude", dataModel);
    }
}
