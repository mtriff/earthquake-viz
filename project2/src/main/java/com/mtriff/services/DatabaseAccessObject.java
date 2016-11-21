package com.mtriff.services;

import java.util.List;
import java.util.logging.Logger;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mtriff.models.DBUser;
import com.mtriff.models.MagnitudeAggregateData;
import com.mtriff.models.MagnitudeLocation;
import java.util.Date;
import java.util.Iterator;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

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

            DB mb = mongo.getDB("earthquakeviz");
            Jongo jongo = new Jongo(mb);
            monthly = jongo.getCollection("monthly");

    }

    public static DatabaseAccessObject getDAO() {
            if (dao == null) dao = new DatabaseAccessObject();
            return dao;
    }

    public Iterator<MagnitudeLocation> getQuakeData(boolean onlyTsunamis) {

        Logger.getAnonymousLogger()
                .info("Getting magnitude by location data");
        Iterator<MagnitudeLocation> magLocData = null;

        String tsunamiString = "";
        if(onlyTsunamis)
        {
            tsunamiString = ", \"properties.tsunami\": {$eq: 1}";
        }

        try
        {
            Date theEpoch = new Date(0);
            magLocData = monthly.
                aggregate("{$match: {\"properties.mag\": "
                        + "{$exists: true, $gt: 0.0}#}})", tsunamiString)
                    .and("{$project: {_id: 0, "
                            + "\"myDayOfYear\": "
                                + "{\"$dateToString\": {format: "
                                    + "\"%Y-%m-%d\", "
                                    + "date: {\"$add\": "
                                        + "[#, \"$properties.time\"]}}},"
                            + "\"myHourOfDay\": "
                                + "{\"$hour\":{\"$add\": "
                                    + "[#, \"$properties.time\"]}},"
                            + "magnitude: \"$properties.mag\", "
                            + "\"latitude\": {$arrayElemAt: "
                                + "[\"$geometry.coordinates\",1]}, "
                            + "\"longitude\": {$arrayElemAt: "
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

        Logger.getAnonymousLogger()
                .info("Getting magnitude aggregate data");
        Iterator<MagnitudeAggregateData> magLocData = null;

        String tsunamiString = "";
        if(onlyTsunamis)
        {
            tsunamiString = ", \"properties.tsunami\": {$eq: 1}";
        }

        try
        {
            Date theEpoch = new Date(0);
            magLocData = monthly.
                aggregate("{$match: {\"properties.mag\": "
                        + "{$exists: true, $gt: 0.0}#}}", tsunamiString)
                    .and("{$project: {_id: 0, "
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
                        + "\"magnitude\": \"$_id.magnitude\", "
                        + "count: \"$count\"}}")
                    .as(MagnitudeAggregateData.class);
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
