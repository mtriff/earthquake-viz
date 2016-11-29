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
import com.mtriff.services.FreemakerConfig;

@Path("Magnitude")
public class MagnitudeResource {
	
	DatabaseAccessObject dao;
	
	@Context ServletContext servletContext;

	public MagnitudeResource() {
		dao = DatabaseAccessObject.getDAO();
	}

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndex() {
        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("Title", "Global Earthquakes by Magnitude");
    	return FreemakerConfig.getRenderedTemplate(servletContext, "Magnitude", dataModel);
    }
}
