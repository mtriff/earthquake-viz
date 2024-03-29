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
            ViewData["QuakeData"] = qe.executeMagnitudeOverTypeQuery();
            return View();
        }

        public IActionResult QuakeLocation()
        {
            ViewData["QuakeData"] = qe.executeMagnitudeByLatLongOverTimeQuery(false);
            return View();
        }

        public IActionResult TsunamiCount() 
        {
            ViewData["TsunamiData"] = qe.executeTsunamiOverTimeQuery();
            return View();
        }

        public IActionResult TsunamiLocation()
        {
            ViewData["TsunamiData"] = qe.executeMagnitudeByLatLongOverTimeQuery(true);
            return View();
        }
    }
}
