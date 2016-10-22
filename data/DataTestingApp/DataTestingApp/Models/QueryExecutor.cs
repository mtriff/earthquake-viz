using System.Runtime.Serialization.Json;

namespace DataTestingApp.Models
{
    public class QueryExecutor
    {
        private RestUtility rs;

        public QueryExecutor()
        {
            this.rs = new Models.RestUtility();
        }

        public MagnitudeOverTime executeMagnitudeOverTypeQuery()
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
                DataContractJsonSerializer ser = new DataContractJsonSerializer(typeof(MagnitudeOverTime));
                ms = (ser.ReadObject(x.GetResponseStream()) as MagnitudeOverTime);
            });
            return ms;
        }

        public TsunamiOverTime executeTsunamiOverTypeQuery()
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
                DataContractJsonSerializer ser = new DataContractJsonSerializer(typeof(TsunamiOverTime));
                ms = (ser.ReadObject(x.GetResponseStream()) as TsunamiOverTime);
            });
            return ms;
        }
    }
}