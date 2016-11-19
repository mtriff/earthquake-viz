package com.mtriff.resources;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtriff.models.DBUser;
import com.mtriff.models.User;
import com.mtriff.services.DatabaseAccessObject;

@Path("/users")
public class UserResource {

	DatabaseAccessObject dao;
	ObjectMapper objectMapper;
	
	private DatabaseAccessObject getDAO() {
		if (dao == null) dao = new DatabaseAccessObject();
		return dao;
	}
	
	private ObjectMapper getObjectMapper() {
		if (objectMapper == null) objectMapper = new ObjectMapper();
		return objectMapper;
	}
	
	@SuppressWarnings("deprecation")
	@POST @Path("create")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(String body) {
		Logger.getAnonymousLogger().info("Received create user request");
		DBUser newUser;
		try {
			newUser = getObjectMapper().readValue(body, DBUser.class);
			newUser.createNewPassword(newUser.getPassword());
			// insert user
			getDAO().createUser(newUser);
			return Response.ok(getObjectMapper().writerWithType(User.class).writeValueAsString(newUser)).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	public String updateUserSettings(User user) {
		// update user
		return null;
	}
}
