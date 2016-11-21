package com.mtriff.services;

import java.util.List;
import java.util.logging.Logger;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mtriff.models.DBUser;
import com.mtriff.models.MagnitudeLocation;
import java.util.Date;
import java.util.Iterator;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import static org.mongodb.morphia.aggregation.Projection.expression;
import static org.mongodb.morphia.aggregation.Projection.projection;
import static org.mongodb.morphia.aggregation.Projection.add;
import org.mongodb.morphia.converters.DateConverter;
import org.mongodb.morphia.query.CriteriaContainer;
import org.mongodb.morphia.query.Query;

public class DatabaseAccessObject {
	
	private static DatabaseAccessObject dao;
	
	private Datastore datastore;
	private ObjectMapper objectMapper;
	
        private MongoCollection monthly;
        
	public DatabaseAccessObject() {
		MongoClient mongo = new MongoClient();
		Morphia morphia = new Morphia();
		morphia.mapPackage("com.mtriff.models");
		datastore = morphia.createDatastore(mongo, "earthquakeviz");
		
		objectMapper = new ObjectMapper();
                
                //mongo.getDB
                DB mb = mongo.getDB("earthquakeviz");
                Jongo jongo = new Jongo(mb);
                monthly = jongo.getCollection("monthly");
                
	}
	
	public static DatabaseAccessObject getDAO() {
		if (dao == null) dao = new DatabaseAccessObject();
		return dao;
	}
	
	public Iterator<MagnitudeLocation> getQuakeData() {
            Logger.getAnonymousLogger()
                    .info("Getting magnitude by location data");
//            Query<Object> query = datastore.getQueryFactory()
//                    .createQuery(datastore);
//            query.and(query.criteria("properties.mag").exists(),
//                    query.criteria("properties.mag").greaterThanOrEq(0.0));
            Iterator<MagnitudeLocation> magLocData = null;
            
            try
            {
                Date theEpoch = new Date(0);
                magLocData = monthly.
                    aggregate("{$match: {\"properties.mag\": {$exists: true, $gt: 0.0}}})")
                        .and("{$project: {_id: 0, \"myDayOfYear\": {\"$dateToString\": {format: \"%Y-%m-%d\", date: {\"$add\": [#, \"$properties.time\"]}}},\"myHourOfDay\": {\"$hour\":{\"$add\": [#, \"$properties.time\"]}}, magnitude: \"$properties.mag\", \"latitude\": {$arrayElemAt: [\"$geometry.coordinates\",1]}, \"longitude\": {$arrayElemAt: [\"$geometry.coordinates\",0]}}}", theEpoch, theEpoch)
                        .and("{$sort: {\"myDayOfYear\": 1}}")
                        .as(MagnitudeLocation.class);
//                magLocData = datastore
//                    .createAggregation(MagnitudeLocation.class)
//                    .match(query)
//                    .project(
//                        projection("myDayOfYear", 
//                            expression("$dateToString",
//                                new BasicDBObject("format", "%Y-%m-%d")
//                                    .append("date", 
//                                        add(new BasicDBObject("date", new Date(0)),
//                                            "$properties.time")))),

//                        projection("myDayOfYear", 
//                            DateConverter.decode("$properties.time")),                                
//
//                            
//                                projection("myDayOfYear", 
//                                        expression("$dateToString",
//                                            new BasicDBObject("format", "%Y-%m-%d")
//                                                .append("date", "$properties.time"))),                                

//                                projection("hour", expression("$hour", add("new Date(0)" + "properties.time"))),
                            
//                        projection("magnitude", "properties.mag"))
//                                projection("Latitude", expression("$arrayAtElem", 
//                                        new BasicDBObject("$geometry.coordinates", 1))),
//                                projection("Longitude", expression("$arrayAtElem", 
//                                        new BasicDBObject("$geometry.coordinates", 0))))
//                    .aggregate(MagnitudeLocation.class);
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
