package com.mtriff.resources;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtriff.models.MagnitudeAggregateData;
import com.mtriff.services.DatabaseAccessObject;

@Path("Magnitude")
public class MagnitudeResource {
	
	DatabaseAccessObject dao;
	ObjectMapper objectMapper;

	public MagnitudeResource() {
		dao = DatabaseAccessObject.getDAO();
	}
	
	private ObjectMapper getObjectMapper() {
		if (objectMapper == null) objectMapper = new ObjectMapper();
		return objectMapper;
	}
	
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndex() {
        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("Title", "Global Earthquakes by Magnitude");
        Iterator<MagnitudeAggregateData> quakeData = dao.getQuakeAggregateData(false);
        StringBuffer jsonQuakeData = new StringBuffer("{ rows: [");
        int preLoopLength = jsonQuakeData.length();
        while (quakeData.hasNext()) {
	    	MagnitudeAggregateData data = quakeData.next();
	    	if (jsonQuakeData.length() > preLoopLength) {
	    		jsonQuakeData.append(",");
	    	}
	    	try {
				jsonQuakeData.append(getObjectMapper().writeValueAsString(data));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
    	}
        jsonQuakeData.append("] }");
        dataModel.put("QuakeData", jsonQuakeData.toString());
        System.out.println(dataModel.get("QuakeData"));
    	return FreemakerConfig.getRenderedTemplate("Magnitude", dataModel);
    }
}
