package com.mtriff.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mtriff.services.DatabaseAccessObject;

@Path("QuakeLocation")
public class QuakeLocationResource {
	
	DatabaseAccessObject dao;
	
	public QuakeLocationResource() {
		dao = new DatabaseAccessObject();
	}
	
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndex() {
        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("Title", "Global Earthquakes by Location");
        dataModel.put("QuakeData", dao.getQuakeData(false));
    	return FreemakerConfig.getRenderedTemplate("QuakeLocation", dataModel);
    }
}
