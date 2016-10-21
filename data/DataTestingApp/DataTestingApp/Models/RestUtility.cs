using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Net;
using System.IO;
using System.Runtime.Serialization.Json;

namespace DataTestingApp.Models
{

    public class RestUtility
    {
        public static string DRILL_URL = "http://159.203.1.85:8047/query.json";
        public static string QUERY_TYPE = "SQL";
        private Uri drillRequest = new Uri(DRILL_URL);

        public void GetPOSTResponse(string data, Action<HttpWebResponse> callback)
        {
            HttpWebRequest request = (HttpWebRequest)HttpWebRequest.Create(drillRequest);

            request.Method = "POST";
            request.ContentType = "application/json;charset=utf-8";

            System.Text.UTF8Encoding encoding = new System.Text.UTF8Encoding();
            byte[] bytes = encoding.GetBytes(data);

            request.ContentLength = bytes.Length;

            using (Stream requestStream = request.GetRequestStream())
            {
                // Send the data.
                requestStream.Write(bytes, 0, bytes.Length);
            }

            try
            {
                request.BeginGetResponse((x) =>
                {
                    using (HttpWebResponse response = (HttpWebResponse)request.EndGetResponse(x))
                    {
                        if (callback != null)
                        {
                            callback(response);
                        }
                    }
                }, null);
            }
            catch (WebException ex)
            {
                var resp = new StreamReader(ex.Response.GetResponseStream()).ReadToEnd();
                System.Diagnostics.Debug.WriteLine(resp);
            }
        }

        public string getJsonString(string query)
        {
            DrillPostQuery dpq = new DrillPostQuery();
            dpq.queryType = RestUtility.QUERY_TYPE;
            dpq.query = query;
            var javaScriptSerializer = new System.Web.Script.Serialization.JavaScriptSerializer();
            string jsonString = javaScriptSerializer.Serialize(dpq);
            return jsonString;
        }
    }
}