using System;
using System.Net.Http;
using System.Net.Http.Headers;
using System.IO;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace DataTestingApp.Models
{

    public class RestUtility
    {
        public static string DRILL_URL = "http://159.203.1.85:8047/query.json";
        public static string QUERY_TYPE = "SQL";
        private Uri drillRequest = new Uri(DRILL_URL);

        public async Task<string> GetPOSTResponse(string data)
        {
            HttpClient client = new HttpClient();
            client.BaseAddress = drillRequest;

            System.Text.UTF8Encoding encoding = new System.Text.UTF8Encoding();
            byte[] bytes = encoding.GetBytes(data);
            ByteArrayContent byteContent = new ByteArrayContent(bytes);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");

            HttpResponseMessage response = await client.PostAsync("", byteContent);
            Stream stream = await response.Content.ReadAsStreamAsync();
            StreamReader sr = new StreamReader(stream);
            return sr.ReadToEnd();
        }

        public string getJsonString(string query)
        {
            DrillPostQuery dpq = new DrillPostQuery();
            dpq.queryType = RestUtility.QUERY_TYPE;
            dpq.query = query;
            string jsonString = JsonConvert.SerializeObject(dpq);
            return jsonString;
        }
    }
}