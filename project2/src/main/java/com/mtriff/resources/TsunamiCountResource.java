package com.mtriff.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mtriff.services.DatabaseAccessObject;

@Path("TsunamiCount")
public class TsunamiCountResource {
		
	DatabaseAccessObject dao;
	
	public TsunamiCountResource() {
		dao = new DatabaseAccessObject();
	}
	
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndex() {
        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("Title", "Global Tsunami Occurrences");
        dataModel.put("TsunamiData", dao.getQuakeAggregateData(true));
    	return FreemakerConfig.getRenderedTemplate("TsunamiCount", dataModel);
    }
}
