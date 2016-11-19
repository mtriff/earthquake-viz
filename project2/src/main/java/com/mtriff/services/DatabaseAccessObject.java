package com.mtriff.services;

import java.util.List;
import java.util.logging.Logger;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mtriff.models.DBUser;
import com.mtriff.models.User;

public class DatabaseAccessObject {
	
	private static DatabaseAccessObject dao;
	
	private Datastore datastore;
	private ObjectMapper objectMapper;
	
	public DatabaseAccessObject() {
		MongoClient mongo = new MongoClient();
		Morphia morphia = new Morphia();
		morphia.mapPackage("com.mtriff.models");
		datastore = morphia.createDatastore(mongo, "earthquakeviz");
		
		objectMapper = new ObjectMapper();
	}
	
	public static DatabaseAccessObject getDAO() {
		if (dao == null) dao = new DatabaseAccessObject();
		return dao;
	}
	
	public String getQuakeData() {
		return "{}";
	}
	
	public String getTsunamiData() {
		return "{}";
	}
	
	public boolean verifyCredentials(String email, String password) {
		List<DBUser> userList = getUserList(email);
		if (!userList.isEmpty()) {
			return userList.get(0).verifyPassword(password);
		}
		return false;
	}
	
	public String getUser(String email) {
		List<DBUser> userList = getUserList(email);
		if (userList.isEmpty()) {
			return "{}";
		}
		User user = userList.get(0);
		try {
			return objectMapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}
	}
	
	private List<DBUser> getUserList(String email) {
		Logger.getAnonymousLogger().info("Getting user with email: " + email);
		List<DBUser> userList = datastore.createQuery(DBUser.class)
											.field("email").equalIgnoreCase(email)
											.asList();
		return userList;
	}
	
	public void createUser(DBUser user) {
		Logger.getAnonymousLogger().info("Creating user: " + user.getEmail());
		datastore.save(user);
	}
	
}
