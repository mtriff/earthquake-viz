/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtriff.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author macdre
 */
@WebListener
public class DatabaseUpdateScheduler implements ServletContextListener{

private ScheduledExecutorService scheduler;
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        Logger.getAnonymousLogger().info("Setting up the scheduled task.");
        
        scheduler = Executors.newSingleThreadScheduledExecutor();
        
        // We want this to run at midnight every night, so we need to figure out
        // what time it is currently, then provide the offset to midnight in ms,
        // then execute every day after that day = 60 sec * 60 min * 24 hour
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime midnight = currentTime
                .plusDays(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0);
        long msToMidnight = midnight.toInstant(ZoneOffset.UTC).toEpochMilli()
                - currentTime.toInstant(ZoneOffset.UTC).toEpochMilli();        
        scheduler.scheduleAtFixedRate(new DatabaseUpdaterTask(), msToMidnight, 
                TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        Logger.getAnonymousLogger().info(currentTime.toString());
        Logger.getAnonymousLogger().info(midnight.toString());
        Logger.getAnonymousLogger().info("Scheduled for: " + msToMidnight + " ms from now.");        
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
}
