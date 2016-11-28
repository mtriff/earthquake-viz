<h2>${Title}.</h2>

<div id="parameters" class="col-md-12">
    <div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">Parameters</h3>
    </div>
    <div class="panel-body">

        <p>Aggregate By</p>

        <div class="btn-group-vertical col-xs-12" role="group" aria-label="aggregate">
            <button type="button" onclick="aggregateBy(this, 'QuakeHour')" class="aggregateBy btn btn-default">By Hour</button>
            <button type="button" onclick="aggregateBy(this, 'QuakeDate')" class="aggregateBy btn btn-default active">By Day</button>
        </div>

        <br>
        <hr>

        <p>Time Range</p>

        <div class="dropdown col-xs-6">
        <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
            Range Start
            <span class="caret"></span>
        </button>
        <ul id="rangestart" class="dropdown-menu timemenu start" aria-labelledby="dropdownMenu1">
        </ul>
        </div>

        <div class="dropdown col-xs-6">
        <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
            Range End
            <span class="caret"></span>
        </button>
        <ul id="rangeend" class="dropdown-menu timemenu" aria-labelledby="dropdownMenu2">
        </ul>
        </div>
        
        <br><br>
        <p>Filter by Continent</p>
        
        <div class="dropdown col-xs-6">
        <button class="btn btn-default dropdown-toggle" type="button" id="continentDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
            No Filter
            <span class="caret"></span>
        </button>
        <ul id="continentmenu" class="dropdown-menu continentmenu" aria-labelledby="continentDropdown">
        	<li onclick="filterContinent(event)"><a href="#">No Filter</a></li>
        	<li onclick="filterContinent(event)"><a href="#">North America</a></li>
        	<li onclick="filterContinent(event)"><a href="#">South America</a></li>
        	<li onclick="filterContinent(event)"><a href="#">Europe</a></li>
        	<li onclick="filterContinent(event)"><a href="#">Africa</a></li>
        	<li onclick="filterContinent(event)"><a href="#">Asia</a></li>
        	<li onclick="filterContinent(event)"><a href="#">Australia</a></li>
        	<li onclick="filterContinent(event)"><a href="#">Oceania</a></li>
        </div>
    </div>
    </div>
</div>

<div class="text-center col-xs-12">
    <svg class="legend"></svg>
</div>
<div class="text-center col-xs-12">
    <svg class="chart"></svg>
</div>

<hr>

<script src="static/js/quakemagnitude.js"></script>
