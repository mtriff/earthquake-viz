package com.mtriff.services;

import java.util.List;
import java.util.logging.Logger;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mtriff.models.DBUser;

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
	
	public DBUser getUser(String email) {
		List<DBUser> userList = getUserList(email);
		if (userList.isEmpty()) {
			return null;
		}
		DBUser user = userList.get(0);
		return user;
//		return objectMapper.writeValueAsString(user);
	}
	
	private List<DBUser> getUserList(String email) {
		Logger.getAnonymousLogger().info("Getting user with email: " + email);
		List<DBUser> userList = datastore.createQuery(DBUser.class)
											.field("email").equalIgnoreCase(email)
											.asList();
		return userList;
	}
	
	public void saveUser(DBUser user) {
		Logger.getAnonymousLogger().info("Saving user: " + user.getEmail());
		datastore.save(user);
	}
	
}
