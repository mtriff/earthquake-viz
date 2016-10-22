// Write your Javascript code.
function loadChart() {
        var rawData = [{
        "theTime": "2016-09-20",
        "Magnitude": 1.0,
        "Tsunami": 0.0,
        "Amount": 24
    },
    {
        "theTime": "2016-09-20",
        "Magnitude": 1.0,
        "Tsunami": 0.0,
        "Amount": 24
    },
    {
        "theTime": "2016-09-20",
        "Magnitude": 2.0,
        "Tsunami": 0.0,
        "Amount": 24
    },
    {
        "theTime": "2016-09-21",
        "Magnitude": 1.0,
        "Tsunami": 0.0,
        "Amount": 24
    }
    ,
    {
        "theTime": "2016-09-22",
        "Magnitude": 1.0,
        "Tsunami": 0.0,
        "Amount": 24
    }
    ];

    var dates = _.groupBy(rawData, "theTime");
    _.forEach(dates, function(value, key) {
        dates[key] = _.countBy(value, "Magnitude");   
    });

    console.log(dates);
    // Format
    // data = [{ "2016-09-20": { 1: 2, 2:1 } }];
    data = _.map(dates, function(value, prop) {
        var newVal = {};
        newVal[prop] = value;
        return newVal;
    });



    renderChart(data);
}

function renderChart(data) {
    // Set graph dimensions
    var margin = { top: 20, right: 20, bottom: 30, left: 40 };
    var height = 350,
        width = 400,
        barWidth = 40,
        barOffset = 20;

    // Set scales
    var maxHeight = 0; 
    _.forEach(data, function(d) {
        var key = Object.keys(d)[0];
        var totalValue = 0;
        _.forEach(Object.keys(d[key]), function(magKey) {
            totalValue += d[key][magKey];
        });
        totalValue = totalValue;
        if (totalValue > maxHeight) maxHeight = totalValue;
    });

    var yScale = d3.scaleLinear()
        .domain([0, maxHeight])
        .range([0, height - margin.top - margin.bottom]);

    var xScale = d3.scaleLinear()
        .domain([0, data.length])
        .range([0, width - margin.left - margin.right]);

    // Create tooltip
    var div = d3.select("body").append("div")
        .attr("class", "tooltip")
        .attr("id", "tooltip")
        .style("opacity", 0);

    // Initialize chart
    var chart = d3.select('.chart');
    chart
        .attr('width', width)
        .attr('height', height);
    
    // Create bars for magnitude 1 earthquakes
    var mag1rects = chart.append('g').attr('class', 'mag1rects');
    mag1rects.selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', 'mag1')
        .attr('width', barWidth)
        .attr('height', function(data) {
            var key = Object.keys(data)[0];
            return yScale(data[key][1]);
        })
        .attr('x', function(data, i) {
            // return margin.left + (0.75 + i) * (barWidth + barOffset);
            return margin.left + xScale(i + 1) - (barWidth / 2);
        })
        .attr('y', function(data, i) {
            var key = Object.keys(data)[0];
            return height - margin.bottom - (yScale(data[key][1]));
        })
        .on("mouseover", function (d) {
            var key = Object.keys(d)[0];
            document.getElementById('tooltip').innerHTML=d[key][1];
            div.transition()
                .style("opacity", 1)
                .style("left", (d3.event.pageX) + "px")
                .style("top", (d3.event.pageY - 28) + "px");
        })
        .on("mouseout", function (d) {
            div.transition()
                .style("opacity", 0);
        });
    
    // Create bars for magnitude 2 earthquakes
    var mag2rects = chart.append('g').attr('class', 'mag2rects');
    mag2rects
        .selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', getMagnitudeFillClass(2))
        .attr('width', barWidth)
        .attr('height', function(data) {
            var key = Object.keys(data)[0];
            if (!data[key][2]) return 0;
            return yScale(data[key][2]);
        })
        .attr('x', function(data, i) {
            // return margin.left + (0.75 + i) * (barWidth + barOffset);
            return margin.left + xScale(i + 1) - (barWidth / 2);
        })
        .attr('y', function(data, i) {
            var key = Object.keys(data)[0];
            if (!data[key][2]) return 0;
            return height - margin.bottom - (yScale(data[key][1])) - (yScale(data[key][2]));
        })
        .on("mouseover", function (d) {
            var key = Object.keys(d)[0];
            document.getElementById('tooltip').innerHTML=d[key][2];
            div.transition()
                .style("opacity", 1)
                .style("left", (d3.event.pageX) + "px")
                .style("top", (d3.event.pageY - 28) + "px");
        })
        .on("mouseout", function (d) {
            div.transition()
                .style("opacity", 0);
        });

    // Add X-axis line
    var minDate = new Date(9999,11,31);
    var maxDate = new Date(1900,0,1);
    var allDates = [];
    _.forEach(data, function(d, key) {
        var currDate = new Date(Object.keys(d)[0]);
        currDate.setDate(currDate.getDate() + 1);
        currDate.setHours(0);
        allDates.push(currDate);
        if (currDate > maxDate) maxDate = currDate;
        if (currDate < minDate) minDate = currDate;
    });

    // Add one lower date so that the tick labels do not begin at the origin
    var minMinusOne = new Date(minDate);
    minMinusOne.setDate(minMinusOne.getDate() - 1);

    var xTimeDomain = [minMinusOne, maxDate];
    var xTimeScale = d3.scaleTime()
        .domain(xTimeDomain)
        .range([margin.left, width - margin.right]);

    var xAxis = d3.axisBottom(xTimeScale)
        .ticks(data.length + 1, "%Y-%m-%d")
        .tickValues(allDates);

    chart.append("g")
        .attr("transform", "translate(0," + (height - margin.bottom) + ")")
        .call(xAxis)
        .selectAll("text")
        .attr("dx", "2em");

    // Add Y-axis line
    var yAxisScale = d3.scaleLinear()
        .domain([maxHeight, 0])
        .range([margin.top, height - margin.bottom]);

    var yAxis = d3.axisLeft(yAxisScale)
        .ticks(maxHeight, "s");

    chart
        .append("g")
        .attr("transform", "translate(" + margin.left + ", 0)")
        .call(yAxis);
}

function getMagnitudeFillClass(magnitude) {
    if (magnitude < 2) return "mag1";
    if (magnitude < 3) return "mag2";
    if (magnitude < 4) return "mag3";
    if (magnitude < 5) return "mag4";
    if (magnitude < 6) return "mag5";
    if (magnitude < 7) return "mag6";
    if (magnitude < 8) return "mag7";
    if (magnitude >= 8) return "mag8plus";
}

window.onload = loadChart;