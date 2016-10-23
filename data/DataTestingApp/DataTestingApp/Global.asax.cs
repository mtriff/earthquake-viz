using System;
using DataTestingApp.Models;
using Newtonsoft.Json;

namespace DataTestingApp
{
    public class Global : System.Web.HttpApplication
    {
        protected void Application_Start(object sender, EventArgs e)
        {
            QueryExecutor qe = new QueryExecutor();

            MagnitudeOverTime mot = JsonConvert.DeserializeObject<MagnitudeOverTime>(
                qe.executeMagnitudeOverTimeQuery());
            System.Diagnostics.Debug.WriteLine(mot.magnitudeTimeSet.Length + " result(s) found.");

            mot = JsonConvert.DeserializeObject<MagnitudeOverTime>(
                qe.executeMagnitudeOverTimeQuery("2016-10-08",0,"2016-10-10",23));
            System.Diagnostics.Debug.WriteLine(mot.magnitudeTimeSet.Length + " result(s) found.");

            TsunamiOverTime tot = JsonConvert.DeserializeObject<TsunamiOverTime>(
                qe.executeTsunamiOverTimeQuery());
            System.Diagnostics.Debug.WriteLine(tot.tsunamiTimeSet.Length + " result(s) found.");

            tot = JsonConvert.DeserializeObject<TsunamiOverTime>(
                qe.executeTsunamiOverTimeQuery("2016-10-08", 0, "2016-10-10", 23));
            System.Diagnostics.Debug.WriteLine(tot.tsunamiTimeSet.Length + " result(s) found.");

            MagnitudeByLatLongOverTime molt = JsonConvert.DeserializeObject<MagnitudeByLatLongOverTime>(
                qe.executeMagnitudeByLatLongOverTimeQuery(false));
            System.Diagnostics.Debug.WriteLine(molt.magnitudeLatLongTimeSet.Length + " result(s) found.");

            molt = JsonConvert.DeserializeObject<MagnitudeByLatLongOverTime>(
                qe.executeMagnitudeByLatLongOverTimeQuery(false, "2016-10-08", 0, "2016-10-10", 23));
            System.Diagnostics.Debug.WriteLine(molt.magnitudeLatLongTimeSet.Length + " result(s) found.");

            MagnitudeByLatLongOverTime tmolt = JsonConvert.DeserializeObject<MagnitudeByLatLongOverTime>(
                qe.executeMagnitudeByLatLongOverTimeQuery(true));
            System.Diagnostics.Debug.WriteLine(tmolt.magnitudeLatLongTimeSet.Length + " result(s) found.");

            tmolt = JsonConvert.DeserializeObject<MagnitudeByLatLongOverTime>(
                qe.executeMagnitudeByLatLongOverTimeQuery(true, "2016-10-08", 0, "2016-10-10", 23));
            System.Diagnostics.Debug.WriteLine(tmolt.magnitudeLatLongTimeSet.Length + " result(s) found.");
        }
    }
}