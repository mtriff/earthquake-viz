using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using EarthquakeViz.Services;

namespace EarthquakeViz.Controllers
{
    public class HomeController : Controller
    {
        QueryExecutor qe;
        public HomeController() {
            this.qe = new QueryExecutor();
        }

        public IActionResult Index()
        {
            return View();
        }

        public IActionResult About()
        {
            ViewData["Message"] = "Your application description page.";

            return View();
        }

        public IActionResult Contact()
        {
            ViewData["Message"] = "Your contact page.";
            return View();
        }

        public IActionResult Error()
        {
            return View();
        }

        public IActionResult Magnitude()
        {
            // MagnitudeOverTime mot = 
            ViewData["QuakeData"] = qe.executeMagnitudeOverTypeQuery();
            // Console.WriteLine(qe.executeMagnitudeOverTypeQuery());
            return View();
        }
    }
}
