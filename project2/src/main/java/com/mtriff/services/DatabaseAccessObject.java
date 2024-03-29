package com.mtriff.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.jongo.Aggregate;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mtriff.models.DBUser;
import com.mtriff.models.LatestDate;
import com.mtriff.models.LocationData;
import com.mtriff.models.LocationType;
import com.mtriff.models.MagnitudeAggregateData;
import com.mtriff.models.MagnitudeLocation;
import org.bson.Document;
import org.jongo.MongoCursor;

public class DatabaseAccessObject {

    private static DatabaseAccessObject dao;

    private MongoClient mongo;
    private Datastore datastore;
    private MongoCollection monthly;
    private MongoCollection location;
    private MongoCollection forImport;
    
    private Jongo jongo;
    private ObjectMapper objectMapper;
    
    private HashMap<String, LocationData> locationDataMap;
    private LocationType currentLocationType = null;
    
    
    public DatabaseAccessObject() {
            mongo = new MongoClient();
            Morphia morphia = new Morphia();
            morphia.mapPackage("com.mtriff.models");
            datastore = morphia.createDatastore(mongo, "earthquakeviz");

            @SuppressWarnings("deprecation")
			DB mb = mongo.getDB("earthquakeviz");
            jongo = new Jongo(mb);
            monthly = jongo.getCollection("monthly");
            forImport = jongo.getCollection("forImport");
            
            // Initalize the location map with the default continent fileter
            // type. This will avoid sluggish initial page load
            configureLocationData(LocationType.CONTINENT);
    }

    public static DatabaseAccessObject getDAO() {
            if (dao == null) dao = new DatabaseAccessObject();
            return dao;
    }
    
	private ObjectMapper getObjectMapper() {
		if (objectMapper == null) objectMapper = new ObjectMapper();
		return objectMapper;
	}

    public String getQuakeAggregateDataAsJSON(boolean onlyTsunamis, String locationFilter) {
        Iterator<MagnitudeAggregateData> quakeData = dao.getQuakeAggregateData(onlyTsunamis, locationFilter);
        StringBuffer jsonQuakeData = new StringBuffer("{ \"rows\": [");
        int preLoopLength = jsonQuakeData.length();
        while (quakeData.hasNext()) {
	    	MagnitudeAggregateData data = quakeData.next();
	    	if (jsonQuakeData.length() > preLoopLength) {
	    		jsonQuakeData.append(",");
	    	}
	    	try {
				jsonQuakeData.append(getObjectMapper().writeValueAsString(data));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
    	}
        jsonQuakeData.append("] }");
        return jsonQuakeData.toString();
    }

    public String getQuakeLocationDataAsJSON(boolean onlyTsunamis) {
        Iterator<MagnitudeLocation> quakeData = dao.getQuakeData(onlyTsunamis);
        StringBuffer jsonQuakeData = new StringBuffer("{ \"rows\": [");
        int preLoopLength = jsonQuakeData.length();
        while (quakeData.hasNext()) {
        	MagnitudeLocation data = quakeData.next();
	    	if (jsonQuakeData.length() > preLoopLength) {
	    		jsonQuakeData.append(",");
	    	}
	    	try {
				jsonQuakeData.append(getObjectMapper().writeValueAsString(data));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
    	}
        jsonQuakeData.append("] }");
        return jsonQuakeData.toString();
    }
        
    public Iterator<MagnitudeLocation> getQuakeData(boolean onlyTsunamis) {
        return getQuakeData(onlyTsunamis, "");
    }
    
    public Iterator<MagnitudeLocation> getQuakeData(boolean onlyTsunamis,
            String continentFilter) {

        Logger.getAnonymousLogger().info("Getting magnitude by location data");
        Iterator<MagnitudeLocation> magLocData = null;
        
        LocationData la = null;
       
        if(!continentFilter.isEmpty()) 
        {
            la = locationDataMap.get(continentFilter);            
        }

        try
        {
            Date theEpoch = new Date(0);
            Aggregate a;
            a = monthly.aggregate("{$match: {\"properties.mag\": "
                + "{$exists: true, $gt: 0.0}#}})", 
                    getTsunamiString(onlyTsunamis));        
            
            if(la != null)
            {
                String json = new ObjectMapper()
                        .writeValueAsString(la.getGeometry());
                a = a.and("{$match: {\"geometry\": "
                        + "{$geoIntersects: {\"$geometry\": "
                        + json + "}}}}");        
            }
          
            magLocData = a.and("{$project: {_id: 0, "
                    + "\"myDayOfYear\": "
                        + "{\"$dateToString\": {format: "
                            + "\"%Y-%m-%d\", "
                            + "date: {\"$add\": [#, \"$properties.time\"]}}},"
                    + "\"myHourOfDay\": "
                        + "{\"$hour\":{\"$add\": [#, \"$properties.time\"]}},"
                    + "Magnitude: \"$properties.mag\", "
                    + "\"Latitude\": {$arrayElemAt: "
                        + "[\"$geometry.coordinates\",1]}, "
                    + "\"Longitude\": {$arrayElemAt: "
                        + "[\"$geometry.coordinates\",0]}}}", 
                    theEpoch, theEpoch)
                .and("{$sort: {\"myDayOfYear\": 1}}")
                .as(MagnitudeLocation.class);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return magLocData;
    }

    public Iterator<MagnitudeAggregateData> getQuakeAggregateData(
            boolean onlyTsunamis) {
        return getQuakeAggregateData(onlyTsunamis, "");
    }
    
    public Iterator<MagnitudeAggregateData> getQuakeAggregateData(
            boolean onlyTsunamis, String continentFilter) {

        Logger.getAnonymousLogger().info("Getting magnitude aggregate data");
        Iterator<MagnitudeAggregateData> magLocData = null;

        LocationData la = null;
       
        if(continentFilter != null && !continentFilter.isEmpty()) 
        {
            la = locationDataMap.get(continentFilter);            
        }
        
        try
        {
            Date theEpoch = new Date(0);
            Aggregate a;
            a = monthly.
                aggregate("{$match: {\"properties.mag\": "
                    + "{$exists: true, $gt: 0.0}#}}", 
                    getTsunamiString(onlyTsunamis));
            
            if(la != null)
            {
                String json = new ObjectMapper()
                    .writeValueAsString(la.getGeometry());
                a = a.and("{$match: {\"geometry\": "
                    + "{$geoIntersects: {\"$geometry\": "
                    + json + "}}}}");        
            }
            
            magLocData = a.and("{$project: {_id: 0, "
                + "\"myDayOfYear\": {\"$dateToString\": "
                    + "{format: \"%Y-%m-%d\", date: "
                        + "{\"$add\": [#, \"$properties.time\"]}}},"
                + "\"myHourOfDay\": {\"$hour\":{\"$add\": "
                    + "[#, \"$properties.time\"]}}, "
                    + "\"properties.mag\": 1}}", theEpoch, theEpoch)
                .and("{$group: "
                    + "{_id: "
                        + "{\"myDayOfYear\": \"$myDayOfYear\", "
                        + "\"myHourOfDay\": \"$myHourOfDay\", "
                        + "magnitude:{$trunc: \"$properties.mag\"}}, "
                    + "count: {$sum: 1}}}")
                .and("{$sort: {\"_id\": 1}}")
                .and("{$project: "
                    + "{_id: 0, "
                    + "\"myDayOfYear\": \"$_id.myDayOfYear\", "
                    + "\"myHourOfDay\": \"$_id.myHourOfDay\", "
                    + "\"Magnitude\": \"$_id.magnitude\", "
                    + "Amount: \"$count\"}}")
                .as(MagnitudeAggregateData.class);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return magLocData;
    }

    public void configureLocationData(LocationType lt) {
        // Only initialize the location type if it is either not initialized
        // Or if the type is changing.
        if (currentLocationType == null || currentLocationType != lt)
        {
            currentLocationType = lt;
            
            Logger.getAnonymousLogger().info("Getting location data");
            Iterator<LocationData> lai = null;
            try
            {
                if (currentLocationType == LocationType.CONTINENT)
                {
                    location = jongo.getCollection("continents"); 
                    
                    lai = location.
                        aggregate("{$unwind: \"$features\"}")
                        .and("{$project: "
                            + "{locationName: \"$features.properties.CONTINENT\","
                            + "geometry: \"$features.geometry\"}}")
                            .as(LocationData.class); 
                }
                else
                {
                    // TODO: Add support for plates here
                }
                
                if(lai != null)
                {
                    locationDataMap = new HashMap<String, LocationData>();
                    LocationData ld;
                    while(lai.hasNext())
                    {
                        ld = lai.next();
                        locationDataMap.put(ld.getLocationName(), ld);
                    }
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private String getTsunamiString(boolean onlyTsunamis) {
        if(onlyTsunamis)
        {
            return ", \"properties.tsunami\": {$eq: 1}";
        }
        return "";
    }

    public HashMap<String, LocationData> getLocationDataMap() {
        return locationDataMap;
    }
    
    public void dropImportTable()
    {
        Logger.getAnonymousLogger().info("Dropping import table.");
        
        forImport.drop();
    }
    
    public long getMostRecentEntryDate()
    {
        Logger.getAnonymousLogger().info("Getting most recent date");
        long value = 0;
        Iterator<LatestDate> timeCompare = null;
        try{
            timeCompare = monthly.aggregate("{$project:{\"theTime\": \"$properties.time\"}}")
                    .and("{$sort: {\"theTime\":-1}}")
                    .and("{$limit:1}")
                    .as(LatestDate.class);
        }
        catch(Exception e)
        {
            Logger.getAnonymousLogger().info(e.getMessage());
        }
        
        if(timeCompare != null && timeCompare.hasNext())
        {
            value = Long.parseLong(timeCompare.next().getTheTime());            
        }
        return value;
    }
    
    public void mergeLatestEarthquakeData(long latestDate) {
        Logger.getAnonymousLogger().info("Performing mongo insert.");
    
        Iterator<Document> a = forImport
            .aggregate("{$unwind: \"$features\"}")
            .and("{$project: {_id: 0, \"type\": \"$features.type\", "
                    + "\"properties\": \"$features.properties\", "
                    + "\"geometry\": \"$features.geometry\"}}")
            .and("{$match: {\"properties.time\":{$gt: " + latestDate + "}}}")
            .as(Document.class);

        Logger.getAnonymousLogger().info("Aggregation complete");
        if(a.hasNext())
        {
            List<Document> docList = Lists.newArrayList(a);

            mongo.getDatabase("earthquakeviz").getCollection("monthly")
                    .insertMany(docList);
       
            Logger.getAnonymousLogger().info("Insert complete");
        }
        else
        {
            Logger.getAnonymousLogger().info("Nothing to merge");
        }
    }
        
    public String getLocationDataMapAsJSON() {
        try {
        	Logger.getAnonymousLogger().info("Location data map is size: " + locationDataMap.values().size());
        	Logger.getAnonymousLogger().info(locationDataMap.toString());
			return getObjectMapper().writeValueAsString(locationDataMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return null;
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
