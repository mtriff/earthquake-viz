/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtriff.services;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.apache.commons.lang3.SystemUtils;

/**
 *
 * @author macdre
 */

public class DatabaseUpdaterTask implements Runnable {  
    
    private String WGET_WIN = "C:\\wget\\wget64.exe -q";
    private String WGET_LINUX = "wget";
    private String USGS_URL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson";
    private String TEMP_JSON_WIN = "C:\\TEMP\\output.json";
    private String TEMP_JSON_LINUX = "/tmp/output.json";
    private long DOWNLOAD_TIMEOUT = 30;        
        
    private String MONGO_IMPORT_WIN = "\"C:\\Program Files\\MongoDB\\Server\\3.2\\bin\\mongoimport.exe\"";
    private String MONGO_IMPORT_LINUX = "mongoimport";
    private long IMPORT_TIMEOUT = 10;
    
    private String MONGO_WIN = "\"C:\\Program Files\\MongoDB\\Server\\3.2\\bin\\mongo.exe\"";
    private String MONGO_LINUX = "mongo";
    private String SCRIPT_WIN = "C:\\TEMP\\merge.js";
    private String SCRIPT_LINUX = "/home/ross/merge.js";
    private long MONGO_TIMEOUT = 30;
    
    @Override
    public void run() {        
        Logger.getAnonymousLogger().info("Executing Database Updater Task.");
        
        DatabaseAccessObject dao = DatabaseAccessObject.getDAO();
        dao.dropImportTable();
        
        if(!doGetLatestEarthquakeData())
        {
            Logger.getAnonymousLogger()
                    .info("Problem downloading file, aborting");
            return;
        }
        
        if(!doImportLatestEarthquakeData())
        {
            Logger.getAnonymousLogger()
                    .info("Problem importing file into database");
        }        
        
        doMergeLatestEarthquakeData();
    }    

    private boolean doGetLatestEarthquakeData() {
        Logger.getAnonymousLogger().info("Executing Database Updater Task.");
        
        Runtime r = Runtime.getRuntime();
        Process p = null;
        String command = null;
        
        if(SystemUtils.IS_OS_WINDOWS)
        {
            command = WGET_WIN + " " + USGS_URL + " -O " + TEMP_JSON_WIN;
        }
        else if(SystemUtils.IS_OS_LINUX)
        {
            command = WGET_LINUX + " " + USGS_URL + " -O " + TEMP_JSON_LINUX;
        }
        if(command != null)
        {
            try
            {
                Logger.getAnonymousLogger().info("Executing cmd: \"" + command + "\"");
                p = r.exec(command);
                
                // exhaust input stream
                BufferedInputStream in = new BufferedInputStream(p.getInputStream());
                byte[] bytes = new byte[4096];
                while (in.read(bytes) != -1) {}
                
                // exhaust error stream
                in = new BufferedInputStream(p.getErrorStream());
                bytes = new byte[4096];
                while (in.read(bytes) != -1) {}
        
                boolean result = p.waitFor(DOWNLOAD_TIMEOUT, TimeUnit.SECONDS);
                p.destroy();
                return result;
            } 
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean doImportLatestEarthquakeData() {
        Logger.getAnonymousLogger().info("Executing Database Updater Task.");
        
        Runtime r = Runtime.getRuntime();
        Process p = null;
        String command = null;
        
        if(SystemUtils.IS_OS_WINDOWS)
        {
            command = MONGO_IMPORT_WIN + " --db earthquakeviz -c forImport " 
                    + TEMP_JSON_WIN;
        }
        else if(SystemUtils.IS_OS_LINUX)
        {
            command = MONGO_IMPORT_LINUX + " --db earthquakeviz -c forImport " 
                    + TEMP_JSON_LINUX;
        }
        if(command != null)
        {
            try
            {
                Logger.getAnonymousLogger().info("Executing cmd: \"" + command + "\"");
                p = r.exec(command);
                
                // exhaust input stream
                BufferedInputStream in = new BufferedInputStream(p.getInputStream());
                byte[] bytes = new byte[4096];
                while (in.read(bytes) != -1) {}
                
                // exhaust error stream
                in = new BufferedInputStream(p.getErrorStream());
                bytes = new byte[4096];
                while (in.read(bytes) != -1) {}
                
                boolean result = p.waitFor(IMPORT_TIMEOUT, TimeUnit.SECONDS);
                p.destroy();
                return result;
            } 
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void doMergeLatestEarthquakeData() {
        Logger.getAnonymousLogger().info("Merging the latest earthquake data");
        DatabaseAccessObject dao = DatabaseAccessObject.getDAO();
        long timeCompare = dao.getMostRecentEntryDate();        
        dao.mergeLatestEarthquakeData(timeCompare);
    }
}
