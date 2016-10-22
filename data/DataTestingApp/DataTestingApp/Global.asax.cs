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

            TsunamiOverTime tot = qe.executeTsunamiOverTimeQuery();
            System.Diagnostics.Debug.WriteLine(tot.tsunamiTimeSet.Length + " result(s) found.");

            MagnitudeByLatLongOverTime molt = qe.executeMagnitudeByLatLongOverTimeQuery();
            System.Diagnostics.Debug.WriteLine(molt.magnitudeLatLongTimeSet.Length + " result(s) found.");

        }
    }
}