using System.Runtime.Serialization.Json;
using Newtonsoft.Json;
using System.IO;

namespace DataTestingApp.Models
{
    public class QueryExecutor
    {
        private RestUtility rs;

        public QueryExecutor()
        {
            this.rs = new Models.RestUtility();
        }

        public MagnitudeOverTime executeMagnitudeOverTimeQuery()
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
            rs.GetPOSTResponse(jsonString, (x) =>
            {
                StreamReader sr = new StreamReader(x.GetResponseStream());
                ms = JsonConvert.DeserializeObject<MagnitudeOverTime>(sr.ReadToEnd());
            });
            return ms;
        }

        public TsunamiOverTime executeTsunamiOverTimeQuery()
        {
            TsunamiOverTime ms = null;

            string query = @"SELECT TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'yyyy-MM-dd') as QuakeDate, 
                    TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'HH') as QuakeHour, 
                    q.f.properties.tsunami as Tsunami, COUNT(q.f.properties.tsunami) as Amount
                    FROM(
                        SELECT flatten(features) AS f
                        FROM dfs.root.`home/ross/all_month.geojson`
                    ) AS q
                    WHERE q.f.properties.mag IS NOT NULL AND q.f.properties.mag > 0
                    GROUP BY TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'yyyy-MM-dd'), 
                    TO_CHAR(TO_DATE(CAST(q.f.properties.`time` AS BIGINT)), 'HH'), 
                    q.f.properties.tsunami
                    ORDER BY QuakeDate ASC, QuakeHour ASC, Tsunami ASC";

            string jsonString = rs.getJsonString(query);
            rs.GetPOSTResponse(jsonString, (x) =>
            {
                StreamReader sr = new StreamReader(x.GetResponseStream());
                ms = JsonConvert.DeserializeObject<TsunamiOverTime>(sr.ReadToEnd());
            });
            return ms;
        }

        public MagnitudeByLatLongOverTime executeMagnitudeByLatLongOverTimeQuery()
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
                    WHERE q.f.properties.mag IS NOT NULL AND q.f.properties.mag > 0.0
                    ORDER BY QuakeDate ASC, QuakeHour ASC";

            string jsonString = rs.getJsonString(query);
            rs.GetPOSTResponse(jsonString, (x) =>
            {
                StreamReader sr = new StreamReader(x.GetResponseStream());
                ms = JsonConvert.DeserializeObject<MagnitudeByLatLongOverTime>(sr.ReadToEnd());
            });
            return ms;
        }

        public MagnitudeByLatLongOverTime executeTsunamiOnlyMagnitudeByLatLongOverTimeQuery()
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
                    WHERE q.f.properties.mag IS NOT NULL AND q.f.properties.mag > 0.0 AND q.f.properties.tsunami = 1
                    ORDER BY QuakeDate ASC, QuakeHour ASC";

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