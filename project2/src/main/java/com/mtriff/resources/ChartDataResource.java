package com.mtriff.resources;

import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mtriff.services.DatabaseAccessObject;

@Path("/chart-data")
public class ChartDataResource {
	
	@PermitAll
	@GET
	@Path("/quake/magnitude{noop: (/)?}{continent : (continent/[^/]+?)?}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEarthquakeMagnitudeData(@PathParam("continent") String continent) {
		continent = continent.replaceAll("continent/", "");
		Logger.getAnonymousLogger().info("Received Quake Magnitude Data Request with continent: " + continent);
		if (continent.equals("No Filter")) continent = null;
		return Response.ok(DatabaseAccessObject.getDAO().getQuakeAggregateDataAsJSON(false, continent)).build();
	}
	
	@PermitAll
	@GET
	@Path("/quake/location")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEarthquakeLocationData() {
		Logger.getAnonymousLogger().info("Received Quake Location Data Request");
		return Response.ok(DatabaseAccessObject.getDAO().getQuakeLocationDataAsJSON(false)).build();
	}
	
	@PermitAll
	@GET
	@Path("/tsunami/magnitude{noop: (/)?}{continent : (continent/[^/]+?)?}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTsunamiMagnitudeData(@PathParam("continent") String continent) {
		continent = continent.replaceAll("continent/", "");
		Logger.getAnonymousLogger().info("Received Tsunami Magnitude Data Request with continent: " + continent);
		if (continent.equals("No Filter")) continent = null;
		return Response.ok(DatabaseAccessObject.getDAO().getQuakeAggregateDataAsJSON(true, continent)).build();
	}
	
	@PermitAll
	@GET
	@Path("/tsunami/location")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTsunamiLocationData() {
		Logger.getAnonymousLogger().info("Received Tsunami Location Data Request");
		return Response.ok(DatabaseAccessObject.getDAO().getQuakeLocationDataAsJSON(true)).build();
	}
	
	@PermitAll
	@GET
	@Path("/location")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocationMapData() {
		Logger.getAnonymousLogger().info("Received Location Map Data Request");
		return Response.ok(DatabaseAccessObject.getDAO().getLocationDataMapAsJSON()).build();
	}
}
