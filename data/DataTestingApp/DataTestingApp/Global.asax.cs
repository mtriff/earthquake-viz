using System;
using System.Net;
using System.IO;
using System.Runtime.Serialization.Json;
using DataTestingApp.Models;

namespace DataTestingApp
{
    public class Global : System.Web.HttpApplication
    {
        protected void Application_Start(object sender, EventArgs e)
        {
            QueryExecutor qe = new QueryExecutor();
            MagnitudeOverTime mot = qe.executeMagnitudeOverTypeQuery();
            System.Diagnostics.Debug.WriteLine(mot.magnitudeTimeSet.Length + " result(s) found.");
        }
    }
}