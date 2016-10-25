// Globals
var yScale;
var xScale;
var margin;
var barWidth;
var barOffset;
var div;
var height;
var currAggregateBy;
var data

function loadChart(aggregateBy, preserveMenuLabel) {
    // Convert dates to format for rendering chart
    // data = [{ "2016-09-20": { 1.0: 2, 2.0:1 } }];
    if (currAggregateBy == aggregateBy) {
        var rangeStartText = document.getElementById('dropdownMenu1').innerHTML;
        var rangeEndText = document.getElementById('dropdownMenu2').innerHTML;
        var rangeStart, rangeEnd;
        if (rangeStartText.indexOf("Range") == -1) { 
            // a time has been selected
            if (aggregateBy.indexOf("Hour") == -1) {
                // Aggregate by days
                rangeStart = getDateFromOption(rangeStartText.substring(0, 10));
            } else {
                // Hour
                rangeStart = new Date(0,0,0, parseInt(rangeStartText.substring(0, 5)));
            }
        }
        if (rangeEndText.indexOf("Range") == -1) { 
            // a time has been selected
            if (aggregateBy.indexOf("Hour") == -1) {
                // Aggregate by days
                rangeEnd = getDateFromOption(rangeEndText.substring(0, 10));
            } else {
                // Hour
                rangeEnd = new Date(0,0,0, parseInt(rangeEndText.substring(0, 5)));
            }
        }
    }
    
    var datesForOptions = [];
    var dataToMap = []
    _.forEach(rawData.rows, function(value) {
        var keyTime;
        if (aggregateBy.indexOf("Date") != -1) {
            keyTime = new Date(value[aggregateBy]);
        } else {
            keyTime = new Date(0, 0, 0, value[aggregateBy]);
        }
        if (datesForOptions.map(Number).indexOf(+keyTime) == -1) datesForOptions.push(keyTime);
        if (rangeStart) {
            if (keyTime < rangeStart) return;
        }
        if (rangeEnd) {
            if (keyTime > rangeEnd) return;
        }
        dataToMap.push(value);
    });

    if (!document.getElementById("rangestart").getElementsByTagName("li").length
            || currAggregateBy != aggregateBy) {
        setTimeRangeOptions(datesForOptions);
    }

    renderChart(dataToMap, aggregateBy);
}

function renderChart(data, aggregateBy) {
    currAggregateBy = aggregateBy;
    // Set graph dimensions
    var width = 800;
    height = 400;

    var projection = d3.geoEquirectangular()
        .scale(125)
        .translate([width / 2, height / 2])
        .precision(.1);

    var path = d3.geoPath()
        .projection(projection);

    var graticule = d3.geoGraticule();

    var svg = d3.select(".chart")
        .attr("width", width)
        .attr("height", height);

    function zoomed() {
        var transform = d3.event.transform;
        d3.selectAll('circle')
            .attr("transform", d3.event.transform);
        d3.selectAll('path')
            .attr("transform", d3.event.transform);    
    }

    svg.append("path")
        .datum(graticule)
        .attr("class", "graticule")
        .attr("d", path);

    var g = svg.append("g");
    g.call(d3.zoom()
        .scaleExtent([1 / 2, 4])
        .on("zoom", zoomed));

    d3.json("/data/world-50m.json", function(error, world) {
    if (error) throw error;

    g.insert("path", ".graticule")
        .datum(topojson.feature(world, world.objects.land))
        .attr("class", "land")
        .attr("d", path);

    g.insert("path", ".graticule")
        .datum(topojson.mesh(world, world.objects.countries, function(a, b) { return a !== b; }))
        .attr("class", "boundary")
        .attr("d", path);
    });

    d3.select(self.frameElement).style("height", height + "px");

    // Create tooltip
    div = d3.select("body").append("div")
        .attr("class", "tooltip")
        .attr("id", "tooltip")
        .style("opacity", 0);

    // Plot quake occurrences
    d3.select(".chart")
        .selectAll('circle')
        .data(data)
        .enter()
        .append('circle')
        .attr("class", function(d) { return getMagnitudeFillClass(d["Magnitude"]); })
        .attr("r", 3)
        .attr("cx",  function(d) { return projection([d["Longitude"],d["Latitude"]])[0] })
        .attr("cy",  function(d) { return projection([d["Longitude"],d["Latitude"]])[1] })
        .on("mouseover", function(d) { showTooltip(d); })
        .on("mouseout", hideTooltip);

    // Add legend
    var legend = d3.selectAll(".legend")
        .attr("height", 40)
        .attr("width", 700);
    legend
        .selectAll('text')
        .data(['Magnitude Legend:'])
        .enter()
        .append('text')
        .attr('y', 25)
        .text(function(d) { return d; })
    legend
        .selectAll('rect')
        .data(["0.0", "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0", "8.0"])
        .enter()
        .append('rect')
        .attr('class', function(d) { return getMagnitudeFillClass(d) })
        .attr('x', function(d, i) { return 150 + (i * 60)} )
        .attr('width', '55')
        .attr('height', '20');
    legend
        .selectAll('text')
        .data(['0.0 - 0.9', '0.0 - 0.9', '1.0 - 1.9', '2.0 - 2.9', '3.0 - 3.9', '4.0 - 4.9', '5.0 - 5.9', '6.0 - 6.9', '7.0 - 7.9', '8.0 +'])
        .enter()
        .append('text')
        .attr('x', function(d, i) { return 150 + ((i - 1) * 60)})
        .attr('y', 35)
        .text(function(d) { return d; })
        .style('fill', 'black');
}

function showTooltip(d) {
    var key = Object.keys(d)[0];
    document.getElementById('tooltip').innerHTML=d["Magnitude"];
    if (!d["Magnitude"]) return;
    div.transition()
        .style("opacity", 1)
        .style("left", (d3.event.pageX) + "px")
        .style("top", (d3.event.pageY - 28) + "px");
}

function clearChart() {
    d3.select('.chart')
        .selectAll('circle')
        .remove();
    d3.selectAll('.tooltip')
        .remove();
    d3.selectAll('g')
        .remove();
}

document.getElementsByTagName("body")[0].onload = loadChart("QuakeDate");