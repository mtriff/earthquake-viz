package com.mtriff.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.mtriff.services.DatabaseAccessObject;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
@Path("/{a:Home|Index|}")
public class IndexResource {
	
	DatabaseAccessObject dao;
	
	@Context ServletContext servletContext;
	
	public IndexResource() {
		dao = new DatabaseAccessObject();
	}
	
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     * @throws IOException 
     * @throws ParseException 
     * @throws MalformedTemplateNameException 
     * @throws TemplateNotFoundException 
     * @throws TemplateException 
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndex() {
    	Logger.getAnonymousLogger().info("Request for index");
        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("Title", "Home Page");
    	return FreemakerConfig.getRenderedTemplate(servletContext, "Index", dataModel);
    }
    
}
