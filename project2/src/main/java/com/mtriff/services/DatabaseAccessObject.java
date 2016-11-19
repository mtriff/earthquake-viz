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
	
	private Datastore datastore;
	private ObjectMapper objectMapper;
	
	public DatabaseAccessObject() {
		MongoClient mongo = new MongoClient();
		Morphia morphia = new Morphia();
		morphia.mapPackage("com.mtriff.models");
		datastore = morphia.createDatastore(mongo, "earthquakeviz");
		
		objectMapper = new ObjectMapper();
	}
	
	public String getQuakeData() {
		return "{}";
	}
	
	public String getTsunamiData() {
		return "{}";
	}
	
	public String getUser(String email) {
//		// Test for now, should get user from database
//		User user = new User();
//		user.setEmail("matt@gmail.com");
//		BarChartSetting earthquakeMagnitude = new BarChartSetting();
//		earthquakeMagnitude.setAggregateBy("By Day");
//		user.setEarthquakeMagnitudeSettings(earthquakeMagnitude);
//		try {
//			return objectMapper.writeValueAsString(user);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
		Logger.getAnonymousLogger().info("Getting user with email: " + email);
		List<DBUser> userList = datastore.createQuery(DBUser.class)
											.field("email").equalIgnoreCase(email)
											.asList();
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
	
	public void createUser(DBUser user) {
		Logger.getAnonymousLogger().info("Creating user: " + user.getEmail());
		datastore.save(user);
	}
	
}
