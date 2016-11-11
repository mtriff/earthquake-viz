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
    
    var dates = _.groupBy(rawData.rows, aggregateBy);
    var datesForOptions = [];
    var newDates = {};
    _.forEach(dates, function(value, key) {
        var keyTime;
        if (aggregateBy.indexOf("Date") != -1) {
            keyTime = new Date(key);
        } else {
            keyTime = new Date(0, 0, 0, key);
        }
        datesForOptions.push(keyTime);
        if (rangeStart) {
            if (keyTime < rangeStart) return;
        }
        if (rangeEnd) {
            if (keyTime > rangeEnd) return;
        }
        newDates[key] = 0;
        _.forEach(value, function(v) {
            newDates[key] += parseInt(v["Amount"]);
        })
    });

    if (!document.getElementById("rangestart").getElementsByTagName("li").length
            || currAggregateBy != aggregateBy) {
        setTimeRangeOptions(datesForOptions);
    }

    data = _.map(newDates, function(value, prop) {
        var newVal = {};
        newVal[prop] = value;
        return newVal;
    }); 
    renderChart(data, aggregateBy, preserveMenuLabel);
}

function renderChart(data, aggregateBy, preserveMenuLabel) {
    currAggregateBy = aggregateBy;
    // Set graph dimensions
    margin = { top: 20, right: 20, bottom: 50, left: 40 };
    height = 400;
    var width = 100 + (data.length * 30); // Max Width (for 30 days) is 1000
    barWidth = 20;
    barOffset = 10;

    // Set scales
    var maxHeight = 0;
    _.forEach(data, function(d) {
        var key = Object.keys(d)[0];
        if (d[key] > maxHeight) maxHeight = d[key];
    });

    yScale = d3.scaleLinear()
        .domain([0, maxHeight])
        .range([0, height - margin.top - margin.bottom]);

    xScale = d3.scaleLinear()
        .domain([0, data.length])
        .range([0, width - margin.left - margin.right]);

    // Create tooltip
    div = d3.select("body").append("div")
        .attr("class", "tooltip")
        .attr("id", "tooltip")
        .style("opacity", 0);

    // Initialize chart
    var chart = d3.select('.chart');
    chart
        .attr('width', width)
        .attr('height', height);
    
    // Create bars for the tsunami counts
    var mag0rects = chart.append('g').attr('class', 'mag0rects');
    var magnitude0 = "0.0";
    mag0rects
        .selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', function(d) { return getTsunamiCountFillClass(d); })
        .attr('width', barWidth)
        .attr('height', function(data) { return getHeight(data, magnitude0); })
        .attr('x', getX)
        .attr('y', function(data, i) { return getY(data, i, magnitude0); })
        .on("mouseover", function(data) { showTooltip(data, magnitude0); })
        .on("mouseout", hideTooltip);

    // Add X-axis line
    var timeFormat = "%Y-%m-%d";
    if (aggregateBy == "QuakeHour") timeFormat = "%H:00";

    var minDate = new Date(9999,11,31);
    var maxDate = new Date(1900,0,1);
    var allDates = [];
    _.forEach(data, function(d, key) {
        if (aggregateBy != "QuakeHour") {
            var currDate = new Date(Object.keys(d)[0]);
        } else {
            var currDate = new Date(0, 0, 0, Object.keys(d)[0]);
        }
        currDate.setDate(currDate.getDate() + 1);
        if (aggregateBy != "QuakeHour") currDate.setHours(0);
        allDates.push(currDate);
        if (currDate > maxDate) maxDate = currDate;
        if (currDate < minDate) minDate = currDate;
    });

    // Add one lower date so that the tick labels do not begin at the origin
    var minMinusOne = new Date(minDate);
    if (aggregateBy != "QuakeHour") {
        minMinusOne.setDate(minMinusOne.getDate() - 1);
    } else {
        minMinusOne.setHours(minMinusOne.getHours() - 1);
    }

    var xTimeDomain = [minMinusOne, maxDate];
    var xTimeScale = d3.scaleTime()
        .domain(xTimeDomain)
        .range([margin.left, width - margin.right]);

    var xAxis = d3.axisBottom(xTimeScale)
        .ticks(data.length + 1, timeFormat)
        .tickValues(allDates);

    chart.append("g")
        .attr("transform", "translate(0," + (height - margin.bottom) + ")")
        .call(xAxis)
        .selectAll("text")
        .attr("transform", "rotate(-45)");

    // Add Y-axis line
    var yAxisScale = d3.scaleLinear()
        .domain([maxHeight, 0])
        .range([margin.top, height - margin.bottom]);

    var yAxis = d3.axisLeft(yAxisScale)
        .ticks(maxHeight / 50, "s");

    chart
        .append("g")
        .attr("transform", "translate(" + margin.left + ", 0)")
        .call(yAxis);
    
    // Add legend
    var legend = d3.selectAll(".legend")
        .attr("height", 40)
        .attr("width", 780);
    legend
        .selectAll('text')
        .data(['Amount Legend:'])
        .enter()
        .append('text')
        .attr('y', 25)
        .text(function(d) { return d; })
    legend
        .selectAll('rect')
        .data(["0.0", "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0", "8.0"])
        .enter()
        .append('rect')
        .attr('class', function(d) { return getTsunamiMagnitudeFillClass(d) })
        .attr('x', function(d, i) { return 150 + (i * 70)} )
        .attr('width', '65')
        .attr('height', '20');
    legend
        .selectAll('text')
        .data(['---', '1 - 99', '100 - 149', '150 - 199', '200 - 249', '250 - 299', '300 - 349', '350 - 399', '400 - 449', '450 +'])
        .enter()
        .append('text')
        .attr('x', function(d, i) { return 150 + ((i - 1) * 70)})
        .attr('y', 35)
        .text(function(d) { return d; })
        .style('fill', 'black');
}

function getHeight(data, magnitude) {
    var key = Object.keys(data)[0];
    if (!data[key]) return 0;
    return yScale(data[key]);
}

function getX(data, i) {
    return margin.left + xScale(i + 1) - (barWidth / 2);
}

function getY(data, i, magnitude) {
    var key = Object.keys(data)[0];
    return height - margin.bottom - yScale(parseInt(data[key]));
}

function showTooltip(d, magnitude) {
    var key = Object.keys(d)[0];
    document.getElementById('tooltip').innerHTML=d[key][magnitude];
    if (!d[key][magnitude]) return;
    div.transition()
        .style("opacity", 1)
        .style("left", (d3.event.pageX) + "px")
        .style("top", (d3.event.pageY - 28) + "px");
}

function clearChart() {
    d3.select('.chart')
        .selectAll('g')
        .remove();
    d3.selectAll('.tooltip')
        .remove();
}

function getTsunamiCountFillClass(d) {
    var key = Object.keys(d)[0];
    var countInt = parseInt(d[key]);
    if (countInt < 100) return "tsunami0";
    if (countInt < 150) return "tsunami1";
    if (countInt < 200) return "tsunami2";
    if (countInt < 250) return "tsunami3";
    if (countInt < 300) return "tsunami4";
    if (countInt < 350) return "tsunami5";
    if (countInt < 400) return "tsunami6";
    if (countInt < 450) return "tsunami7";
    if (countInt >= 450) return "tsunami8plus";
}

document.getElementsByTagName("body")[0].onload = loadChart("QuakeDate");