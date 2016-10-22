using System;
using System.IO;
using System.Threading.Tasks;
using Newtonsoft.Json;
using EarthquakeViz.Models;

namespace EarthquakeViz.Services
{
    public class QueryExecutor
    {
        private RestUtility rs;

        public QueryExecutor()
        {
            this.rs = new RestUtility();
        }

        public string executeMagnitudeOverTypeQuery()
        {
            MagnitudeOverTime ms =null;
            string query = @"SELECT TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'yyyy-MM-dd') as QuakeDate, 
                TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'HH') as QuakeHour, 
                ROUND(q.f.properties.mag,0) as Magnitude, COUNT(ROUND(q.f.properties.mag,0)) as Amount
                FROM(
                    SELECT flatten(features) AS f
                    FROM dfs.root.`home/ross/all_month.geojson`
                ) AS q
                WHERE q.f.properties.mag IS NOT NULL AND q.f.properties.mag > 0
                GROUP BY TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'yyyy-MM-dd'), 
                TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'HH'), 
                ROUND(q.f.properties.mag, 0)
                ORDER BY QuakeDate ASC, QuakeHour ASC, Magnitude ASC";
            string jsonString = rs.getJsonString(query);
            return rs.GetPOSTResponse(jsonString).Result;
        }
    }
}