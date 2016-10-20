// Write your Javascript code.
function loadChart() {
    var data = [4, 8, 15, 16, 23, 40];
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
    }];

    var dates = _.groupBy(rawData, "theTime");
    _.forEach(dates, function(value, key) {
        dates[key] = _.countBy(value, "Magnitude");   
    });

    console.log(dates);
    // Format
    // { "2016-09-20": { 1: 2, 1:1 } }


    var margin = { top: 20, right: 20, bottom: 30, left: 40 };

    //  the size of the overall svg element
    var height = 200,
        width = 720,
        //  the width of each bar and the offset between each bar
        barWidth = 40,
        barOffset = 20;

    var yScale = d3.scaleLinear()
        .domain([0, d3.max(data)])
        .range([0, height]);

    var xScale = d3.scaleOrdinal()
        .domain([0, data.length])
        .range([0, data.length * (barWidth + barOffset)]);

    var div = d3.select("body").append("div")
        .attr("class", "tooltip")
        .style("opacity", 0);

    var chart = d3.select('.chart');

    chart
        .attr('width', width)
        .attr('height', height)
        .selectAll('rect').data(data)
        .enter().append('rect')
        .attr('width', barWidth)
        .attr('height', function (data) {
            return yScale(data);
        })
        .attr('x', function (data, i) {
            return margin.left + i * (barWidth + barOffset);
        })
        .attr('y', function (data) {
            return height - yScale(data) - margin.bottom;
        })
        .on("mouseover", function (d) {
            div.transition()
                .duration(200)
                .style("opacity", .9);
            div.html(d)
                .style("left", (d3.event.pageX) + "px")
                .style("top", (d3.event.pageY - 28) + "px");
        })
        .on("mouseout", function (d) {
            div.transition()
                .duration(500)
                .style("opacity", 0);
        });;

    var yAxis = d3.axisLeft(yScale)
        .ticks(5, "s");

    var g = chart
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    g
        .append("g")
        .attr("transform", "translate(" + margin.left + ", -" + margin.bottom + ")")
        .call(yAxis);



    var xAxis = d3.axisBottom(xScale)
        .ticks(data.length, "s");
    console.log(height);
    g
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + (height - margin.bottom) + ")")
        .call(xAxis);



}

window.onload = loadChart;