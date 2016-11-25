<h2>${Title}.</h2>

<script>var rawData = ${TsunamiData?no_esc};</script>

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

<script src="js/topojson.min.js"></script>
<script src="js/tsunamilocation.js"></script>
