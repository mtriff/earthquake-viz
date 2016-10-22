﻿using Newtonsoft.Json;
using System.IO;
using System;

namespace DataTestingApp.Models
{
    public class QueryExecutor
    {
        private RestUtility rs;

        private string EPOCH_TIME = string.Format("{0:yyyy-MM-dd}", new DateTime(1970, 1, 1));
        public QueryExecutor()
        {
            this.rs = new Models.RestUtility();
        }

        public MagnitudeOverTime executeMagnitudeOverTimeQuery()
        {            
            return executeMagnitudeOverTimeQuery(EPOCH_TIME, 0, 
                string.Format("{0:yyyy-MM-dd}", DateTime.Now), 23);
        }

        public MagnitudeOverTime executeMagnitudeOverTimeQuery(string fromDate, int fromHour, 
            string toDate, int toHour)
        {
            MagnitudeOverTime ms =null;

            string query = @"SELECT TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'yyyy-MM-dd') as QuakeDate, 
                TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'HH') as QuakeHour, 
                ROUND(q.f.properties.mag,0) as Magnitude, COUNT(ROUND(q.f.properties.mag,0)) as Amount
                FROM(
                    SELECT flatten(features) AS f
                    FROM dfs.root.`home/ross/all_month.geojson`
                ) AS q
                WHERE q.f.properties.mag IS NOT NULL AND q.f.properties.mag > 0 ";
            query += @"AND UNIX_TIMESTAMP('{0} {1}:00:00', 'yyyy-MM-dd HH:mm:SS') 
                <= ((q.f.properties.`time`)/ 1000) AND ((q.f.properties.`time`) / 1000) 
                <= UNIX_TIMESTAMP('{2} {3}:00:00', 'yyyy-MM-dd HH:mm:SS') ";
            query = string.Format(query, fromDate, fromHour, toDate, toHour);
            query += @"GROUP BY TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'yyyy-MM-dd'), 
                TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'HH'), 
                ROUND(q.f.properties.mag, 0)ORDER BY QuakeDate ASC, QuakeHour ASC, Magnitude ASC";

            string jsonString = rs.getJsonString(query);
            rs.GetPOSTResponse(jsonString, (x) =>
            {
                StreamReader sr = new StreamReader(x.GetResponseStream());
                ms = JsonConvert.DeserializeObject<MagnitudeOverTime>(sr.ReadToEnd());
            });
            return ms;
        }

        public TsunamiOverTime executeTsunamiOverTimeQuery()
        {
            return executeTsunamiOverTimeQuery(EPOCH_TIME, 0,
                string.Format("{0:yyyy-MM-dd}", DateTime.Now), 23);
        }

        public TsunamiOverTime executeTsunamiOverTimeQuery(string fromDate, int fromHour,
            string toDate, int toHour)
        {
            TsunamiOverTime ms = null;

            string query = @"SELECT TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'yyyy-MM-dd') as QuakeDate, 
                    TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'HH') as QuakeHour, 
                    q.f.properties.tsunami as Tsunami, COUNT(q.f.properties.tsunami) as Amount
                    FROM(
                        SELECT flatten(features) AS f
                        FROM dfs.root.`home/ross/all_month.geojson`
                    ) AS q
                    WHERE q.f.properties.mag IS NOT NULL AND q.f.properties.mag > 0 ";
            query += @"AND UNIX_TIMESTAMP('{0} {1}:00:00', 'yyyy-MM-dd HH:mm:SS') 
                <= ((q.f.properties.`time`)/ 1000) AND ((q.f.properties.`time`) / 1000) 
                <= UNIX_TIMESTAMP('{2} {3}:00:00', 'yyyy-MM-dd HH:mm:SS') ";
            query = string.Format(query, fromDate, fromHour, toDate, toHour);

            query += @"GROUP BY TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'yyyy-MM-dd'), 
                    TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'HH'), 
                    q.f.properties.tsunami ORDER BY QuakeDate ASC, QuakeHour ASC, Tsunami ASC";

            string jsonString = rs.getJsonString(query);
            rs.GetPOSTResponse(jsonString, (x) =>
            {
                StreamReader sr = new StreamReader(x.GetResponseStream());
                ms = JsonConvert.DeserializeObject<TsunamiOverTime>(sr.ReadToEnd());
            });
            return ms;
        }

        public MagnitudeByLatLongOverTime executeMagnitudeByLatLongOverTimeQuery(bool onlyTsunamis)
        {
            return executeMagnitudeByLatLongOverTimeQuery(onlyTsunamis, EPOCH_TIME, 0,
                string.Format("{0:yyyy-MM-dd}", DateTime.Now), 23);
        }

        public MagnitudeByLatLongOverTime executeMagnitudeByLatLongOverTimeQuery(bool onlyTsunamis, 
            string fromDate, int fromHour, string toDate, int toHour)
        {
            MagnitudeByLatLongOverTime ms = null;

            string query = @"SELECT TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'yyyy-MM-dd') as QuakeDate, 
                    TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'HH') as QuakeHour, 
                    q.f.geometry.coordinates[1] as Latitude, q.f.geometry.coordinates[0] as Longitude,
                    q.f.properties.mag as Magnitude
                    FROM(
                        SELECT flatten(features) AS f
                        FROM dfs.root.`home/ross/all_month.geojson`
                    ) AS q
                    WHERE q.f.properties.mag IS NOT NULL AND q.f.properties.mag > 0.0 ";
            // Add restriction for only tsunamis if requested
            if (onlyTsunamis)
            {
                query += "AND q.f.properties.tsunami = 1 ";
            }
            query += @"AND UNIX_TIMESTAMP('{0} {1}:00:00', 'yyyy-MM-dd HH:mm:SS') 
                <= ((q.f.properties.`time`)/ 1000) AND ((q.f.properties.`time`) / 1000) 
                <= UNIX_TIMESTAMP('{2} {3}:00:00', 'yyyy-MM-dd HH:mm:SS') ";
            query = string.Format(query, fromDate, fromHour, toDate, toHour);

            // Add order by for sorting
            query += "ORDER BY QuakeDate ASC, QuakeHour ASC";
            
            string jsonString = rs.getJsonString(query);
            rs.GetPOSTResponse(jsonString, (x) =>
            {
                StreamReader sr = new StreamReader(x.GetResponseStream());
                ms = JsonConvert.DeserializeObject<MagnitudeByLatLongOverTime>(sr.ReadToEnd());
            });
            return ms;
        }
    }
}