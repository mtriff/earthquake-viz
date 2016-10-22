using System;
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

            TsunamiOverTime tot = qe.executeTsunamiOverTypeQuery();
            System.Diagnostics.Debug.WriteLine(tot.tsunamiTimeSet.Length + " result(s) found.");

        }
    }
}