package com.mtriff.services;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mtriff.models.BarChartSetting;
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
	
	public String getUser() {
		// Test for now, should get user from database
		User user = new User();
		user.setEmail("matt@gmail.com");
		BarChartSetting earthquakeMagnitude = new BarChartSetting();
		earthquakeMagnitude.setAggregateBy("By Day");
		user.setEarthquakeMagnitudeSettings(earthquakeMagnitude);
		try {
			return objectMapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean createUser(String userStr) {
		return false;
	}
	
}
