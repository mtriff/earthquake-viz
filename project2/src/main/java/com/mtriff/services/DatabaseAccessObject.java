package com.mtriff.services;

import java.util.List;
import java.util.logging.Logger;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mtriff.models.DBUser;
import com.mtriff.models.MagnitudeLocation;
import java.util.Iterator;
import static org.mongodb.morphia.aggregation.Projection.expression;
import static org.mongodb.morphia.aggregation.Projection.projection;
import org.mongodb.morphia.query.CriteriaContainer;
import org.mongodb.morphia.query.Query;

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
	
	public Iterator<MagnitudeLocation> getQuakeData() {
            Logger.getAnonymousLogger()
                    .info("Getting magnitude by location data");
            Query<Object> query = datastore.getQueryFactory()
                    .createQuery(datastore);
            query.and(query.criteria("properties.mag").exists(),
                    query.criteria("properties.mag").greaterThanOrEq(0.0));
            Iterator<MagnitudeLocation> magLocData = null;
            
            try
            {
                magLocData = datastore
                        .createAggregation(MagnitudeLocation.class)
                        .match(query.field("properties.mag").greaterThanOrEq(0.0))
                        .project(
//                                projection("myDayOfYear", expression("$dateToString",
//                                        new BasicDBObject("format", "%Y-%m-%d").
//                                                append("date", projection("properties.time")))))
//                                projection("myHourOfDay", expression("$hour", projection("properties.time"))),
                                projection("magnitude", "properties.mag"))
//                                projection("Latitude", expression("$arrayAtElem", 
//                                        new BasicDBObject("$geometry.coordinates", 1))),
//                                projection("Longitude", expression("$arrayAtElem", 
//                                        new BasicDBObject("$geometry.coordinates", 0))))
                        .aggregate(MagnitudeLocation.class);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return magLocData;
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
