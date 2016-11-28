package com.mtriff.resources;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.mtriff.services.DatabaseAccessObject;

@Path("TsunamiLocation")
public class TsunamiLocationResource {
	
	DatabaseAccessObject dao;
	
	@Context ServletContext servletContext;
	
	public TsunamiLocationResource() {
		dao = new DatabaseAccessObject();
	}
	
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndex() {
        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("Title", "Global Potential Tsunamis in Oceanic Regions");
    	return FreemakerConfig.getRenderedTemplate(servletContext, "TsunamiLocation", dataModel);
    }
}

