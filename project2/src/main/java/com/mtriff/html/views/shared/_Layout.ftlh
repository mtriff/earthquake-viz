<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${Title} - EarthquakeViz</title>

    <link rel="stylesheet" href="https://ajax.aspnetcdn.com/ajax/bootstrap/3.3.6/css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/site.css" />

    <script src="https://ajax.aspnetcdn.com/ajax/jquery/jquery-2.2.3.min.js"></script>
    <script src="https://ajax.aspnetcdn.com/ajax/bootstrap/3.3.6/bootstrap.min.js"></script>
    <script src="js/d3.min.js"></script>
    <script src="js/lodash.js"></script>
    <script src="js/quakecommon.js"></script>
</head>
<body>
    <div class="navbar navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a asp-area="" asp-controller="Home" asp-action="Index" class="navbar-brand">EarthquakeViz</a>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li><a href="Index" class="navbar-quake-custom">Home</a></li>
                    <li><a href="Magnitude" class="navbar-quake-custom">Earthquake Magnitudes</a></li>
                    <li><a href="QuakeLocation" class="navbar-quake-custom">Earthquake Locations</a></li>
                    <li><a href="TsunamiCount" class="navbar-quake-custom">Tsunami Occurrences</a></li>
                    <li><a href="TsunamiLocation" class="navbar-quake-custom">Tsunami Locations</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="container body-content">
        <#include RenderBody>
        <hr />
    </div>


</body>
</html>
