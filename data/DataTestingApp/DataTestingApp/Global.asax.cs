using System;
using DataTestingApp.Models;

namespace DataTestingApp
{
    public class Global : System.Web.HttpApplication
    {
        protected void Application_Start(object sender, EventArgs e)
        {
            QueryExecutor qe = new QueryExecutor();

            MagnitudeOverTime mot = qe.executeMagnitudeOverTimeQuery();
            System.Diagnostics.Debug.WriteLine(mot.magnitudeTimeSet.Length + " result(s) found.");

            mot = qe.executeMagnitudeOverTimeQuery("2016-10-08",0,"2016-10-10",23);
            System.Diagnostics.Debug.WriteLine(mot.magnitudeTimeSet.Length + " result(s) found.");

            TsunamiOverTime tot = qe.executeTsunamiOverTimeQuery();
            System.Diagnostics.Debug.WriteLine(tot.tsunamiTimeSet.Length + " result(s) found.");

            tot = qe.executeTsunamiOverTimeQuery("2016-10-08", 0, "2016-10-10", 23);
            System.Diagnostics.Debug.WriteLine(tot.tsunamiTimeSet.Length + " result(s) found.");

            MagnitudeByLatLongOverTime molt = qe.executeMagnitudeByLatLongOverTimeQuery(false);
            System.Diagnostics.Debug.WriteLine(molt.magnitudeLatLongTimeSet.Length + " result(s) found.");

            molt = qe.executeMagnitudeByLatLongOverTimeQuery(false, "2016-10-08", 0, "2016-10-10", 23);
            System.Diagnostics.Debug.WriteLine(molt.magnitudeLatLongTimeSet.Length + " result(s) found.");

            MagnitudeByLatLongOverTime tmolt = qe.executeMagnitudeByLatLongOverTimeQuery(true);
            System.Diagnostics.Debug.WriteLine(tmolt.magnitudeLatLongTimeSet.Length + " result(s) found.");

            tmolt = qe.executeMagnitudeByLatLongOverTimeQuery(true, "2016-10-08", 0, "2016-10-10", 23);
            System.Diagnostics.Debug.WriteLine(tmolt.magnitudeLatLongTimeSet.Length + " result(s) found.");
        }
    }
}