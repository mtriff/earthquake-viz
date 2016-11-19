package com.mtriff.resources;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtriff.AuthenticationFilter;
import com.mtriff.models.DBUser;
import com.mtriff.models.User;
import com.mtriff.services.DatabaseAccessObject;

@Path("/users")
public class UserResource {

	DatabaseAccessObject dao;
	ObjectMapper objectMapper;
	
	private ObjectMapper getObjectMapper() {
		if (objectMapper == null) objectMapper = new ObjectMapper();
		return objectMapper;
	}
	
	@SuppressWarnings("deprecation")
	@RolesAllowed("USER")
	@GET @Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginUser(ContainerRequestContext requestContext) {
		Logger.getAnonymousLogger().info("Received successful login request");
		try {
			String[] authValues = AuthenticationFilter.getAuthorization(requestContext);
			if (authValues.length < 2) throw new Exception();
            String username = authValues[0];
			DBUser dbUser = DatabaseAccessObject.getDAO().getUser(username);
			return Response.ok(getObjectMapper().writerWithType(User.class).writeValueAsString(dbUser)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@SuppressWarnings("deprecation")
	@PermitAll
	@POST @Path("create")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(String body) {
		Logger.getAnonymousLogger().info("Received create user request");
		DBUser newUser;
		try {
			newUser = getObjectMapper().readValue(body, DBUser.class);
			newUser.createNewPassword(newUser.getPassword());
			// insert user
			DatabaseAccessObject.getDAO().saveUser(newUser);
			return Response.ok(getObjectMapper().writerWithType(User.class).writeValueAsString(newUser)).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@SuppressWarnings("deprecation")
	@RolesAllowed("USER")
	@PUT @Path("update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUserSettings(User user) {
		DBUser dbUser = DatabaseAccessObject.getDAO().getUser(user.getEmail());
		dbUser.setEarthquakeMagnitudeSettings(user.getEarthquakeMagnitudeSettings());
		dbUser.setEarthquakeLocationSettings(user.getEarthquakeLocationSettings());
		dbUser.setTsunamiOccurrenceSettings(user.getTsunamiOccurrenceSettings());
		dbUser.setTsunamiLocationSettings(user.getTsunamiLocationSettings());
		DatabaseAccessObject.getDAO().saveUser(dbUser);
		try {
			return Response.ok(getObjectMapper().writerWithType(User.class).writeValueAsString(dbUser)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
