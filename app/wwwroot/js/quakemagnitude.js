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
        newDates[key] = _.countBy(value, "Magnitude");   
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
        var totalValue = 0;
        _.forEach(Object.keys(d[key]), function(magKey) {
            totalValue += d[key][magKey];
        });
        totalValue = totalValue;
        if (totalValue > maxHeight) maxHeight = totalValue;
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
    
    // // Create bars for magnitude 0 earthquakes
    var mag0rects = chart.append('g').attr('class', 'mag0rects');
    var magnitude0 = "0.0";
    mag0rects
        .selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', getMagnitudeFillClass(magnitude0))
        .attr('width', barWidth)
        .attr('height', function(data) { return getHeight(data, magnitude0); })
        .attr('x', getX)
        .attr('y', function(data, i) { return getY(data, i, magnitude0); })
        .on("mouseover", function(data) { showTooltip(data, magnitude0); })
        .on("mouseout", hideTooltip);

    // Create bars for magnitude 1 earthquakes
    var mag1rects = chart.append('g').attr('class', 'mag1rects');
    var magnitude1 = "1.0";
    mag1rects
        .selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', getMagnitudeFillClass(magnitude1))
        .attr('width', barWidth)
        .attr('height', function(data) { return getHeight(data, magnitude1); })
        .attr('x', getX)
        .attr('y', function(data, i) { return getY(data, i, magnitude1); })
        .on("mouseover", function(data) { showTooltip(data, magnitude1); })
        .on("mouseout", hideTooltip);
    
    // Create bars for magnitude 2 earthquakes
    var mag2rects = chart.append('g').attr('class', 'mag2rects');
    var magnitude2 = "2.0";
    mag2rects
        .selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', getMagnitudeFillClass(magnitude2))
        .attr('width', barWidth)
        .attr('height', function(data) { return getHeight(data, magnitude2); })
        .attr('x', getX)
        .attr('y', function(data, i) { return getY(data, i, magnitude2); })
        .on("mouseover", function(data) { showTooltip(data, magnitude2); })
        .on("mouseout", hideTooltip);

    // Create bars for magnitude 3 earthquakes
    var mag3rects = chart.append('g').attr('class', 'mag3rects');
    var magnitude3 = "3.0";
    mag3rects
        .selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', getMagnitudeFillClass(magnitude3))
        .attr('width', barWidth)
        .attr('height', function(data) { return getHeight(data, magnitude3); })
        .attr('x', getX)
        .attr('y', function(data, i) { return getY(data, i, magnitude3); })
        .on("mouseover", function(data) { showTooltip(data, magnitude3); })
        .on("mouseout", hideTooltip);

    // Create bars for magnitude 4 earthquakes
    var mag4rects = chart.append('g').attr('class', 'mag4rects');
    var magnitude4 = "4.0";
    mag4rects
        .selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', getMagnitudeFillClass(magnitude4))
        .attr('width', barWidth)
        .attr('height', function(data) { return getHeight(data, magnitude4); })
        .attr('x', getX)
        .attr('y', function(data, i) { return getY(data, i, magnitude4); })
        .on("mouseover", function(data) { showTooltip(data, magnitude4); })
        .on("mouseout", hideTooltip);

    // Create bars for magnitude 5 earthquakes
    var mag5rects = chart.append('g').attr('class', 'mag5rects');
    var magnitude5 = "5.0";
    mag5rects
        .selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', getMagnitudeFillClass(magnitude5))
        .attr('width', barWidth)
        .attr('height', function(data) { return getHeight(data, magnitude5); })
        .attr('x', getX)
        .attr('y', function(data, i) { return getY(data, i, magnitude5); })
        .on("mouseover", function(data) { showTooltip(data, magnitude5); })
        .on("mouseout", hideTooltip);

    // Create bars for magnitude 6 earthquakes
    var mag6rects = chart.append('g').attr('class', 'mag6rects');
    var magnitude6 = "6.0";
    mag6rects
        .selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', getMagnitudeFillClass(magnitude6))
        .attr('width', barWidth)
        .attr('height', function(data) { return getHeight(data, magnitude6); })
        .attr('x', getX)
        .attr('y', function(data, i) { return getY(data, i, magnitude6); })
        .on("mouseover", function(data) { showTooltip(data, magnitude6); })
        .on("mouseout", hideTooltip);

    // Create bars for magnitude 7 earthquakes
    var mag7rects = chart.append('g').attr('class', 'mag7rects');
    var magnitude7 = "7.0";
    mag7rects
        .selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', getMagnitudeFillClass(magnitude7))
        .attr('width', barWidth)
        .attr('height', function(data) { return getHeight(data, magnitude7); })
        .attr('x', getX)
        .attr('y', function(data, i) { return getY(data, i, magnitude7); })
        .on("mouseover", function(data) { showTooltip(data, magnitude7); })
        .on("mouseout", hideTooltip);

    // Create bars for magnitude 8 earthquakes
    var mag8rects = chart.append('g').attr('class', 'mag8rects');
    var magnitude8 = "8.0";
    mag8rects
        .selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', getMagnitudeFillClass(magnitude8))
        .attr('width', barWidth)
        .attr('height', function(data) { return getHeight(data, magnitude8); })
        .attr('x', getX)
        .attr('y', function(data, i) { return getY(data, i, magnitude8); })
        .on("mouseover", function(data) { showTooltip(data, magnitude8); })
        .on("mouseout", hideTooltip);

    // Create bars for magnitude 9 earthquakes
    var mag9rects = chart.append('g').attr('class', 'mag9rects');
    var magnitude9 = "9.0";
    mag9rects
        .selectAll('rect')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', getMagnitudeFillClass(magnitude9))
        .attr('width', barWidth)
        .attr('height', function(data) { return getHeight(data, magnitude9); })
        .attr('x', getX)
        .attr('y', function(data, i) { return getY(data, i, magnitude9); })
        .on("mouseover", function(data) { showTooltip(data, magnitude9); })
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
        .ticks(maxHeight / 10, "s");

    chart
        .append("g")
        .attr("transform", "translate(" + margin.left + ", 0)")
        .call(yAxis);
}

function getMagnitudeFillClass(magnitude) {
    var magnitudeInt = parseInt(magnitude);
    if (magnitudeInt < 1) return "mag0";
    if (magnitudeInt < 2) return "mag1";
    if (magnitudeInt < 3) return "mag2";
    if (magnitudeInt < 4) return "mag3";
    if (magnitudeInt < 5) return "mag4";
    if (magnitudeInt < 6) return "mag5";
    if (magnitudeInt < 7) return "mag6";
    if (magnitudeInt < 8) return "mag7";
    if (magnitudeInt >= 8) return "mag8plus";
}

function getHeight(data, magnitude) {
    var key = Object.keys(data)[0];
    if (!data[key][magnitude]) return 0;
    return yScale(data[key][magnitude]);
}

function getX(data, i) {
    return margin.left + xScale(i + 1) - (barWidth / 2);
}

function getY(data, i, magnitude) {
    var key = Object.keys(data)[0];
    if (!data[key][magnitude]) return 0;
    var yValue = height - margin.bottom;
    var magnitudeInt = parseInt(magnitude);
    while (magnitudeInt >= 0) {
        if (data[key][(magnitudeInt + ".0")]) yValue -= (yScale(data[key][(magnitudeInt + ".0")]));
        magnitudeInt--;
    } 
    return yValue;
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

function hideTooltip(d) {
    div.transition()
        .style("opacity", 0);
}

function clearChart() {
    d3.select('.chart')
        .selectAll('g')
        .remove();
    d3.selectAll('.tooltip')
        .remove();
}

function aggregateBy(caller, parameter) {
    var btns = document.getElementsByClassName("aggregateBy");
    _.forEach(btns, function(value) {
        value.className = value.className.replace(/\bactive\b/,'');
    });
    caller.className = caller.className + " active";
    clearChart();
    loadChart(parameter);
}

function setTimeRangeOptions(allOptions, menu, preserveMenuLabel) {
    var menus;
    if (menu) {
        menus = [menu];
    } else {
        menus = document.getElementsByClassName("timemenu");
    }
    _.forEach(menus, function(menu) {
        // Set default button label
        if (!preserveMenuLabel) {
            var menuBtn = menu.parentElement.getElementsByClassName("btn")[0];        
            menuBtn.innerHTML = "Range " + _.capitalize(menu.id.substring(5)) + "<span class='caret'></span>";
        }
        // Set options
        menu.innerHTML = "";
        allOptions.sort(function(a,b) { 
            return new Date(a).getTime() - new Date(b).getTime() 
        });
        _.forEach(allOptions, function(option) {
            var li = document.createElement("li");
            var a = document.createElement("a");
            a.className = "timeOption";
            a.setAttribute("href", "#");
            var optionText;
            if (option.getYear() > 1) {
                // Using days
                optionText = option.toISOString().substring(0, 10);
            } else {
                optionText = formatAMPM(option);
            }
            a.appendChild(document.createTextNode(optionText));
            li.appendChild(a);
            menu.appendChild(li);
        });
    });
    setOptionsChangeText(allOptions);
}

function formatAMPM(date) {
  var hours = date.getHours();
  if (hours < 10) hours = "0" + hours;
  var strTime = hours + ':00';
  return strTime;
}

function setOptionsChangeText(allOptions) {
    var options = document.getElementsByClassName("timeOption");
    _.forEach(options, function(option) {
        option.onclick =  function(e) {
            // Change options for the other menu
            var timeSelected = getDateFromOption(e.target.innerHTML); // Split to handle both hours and dates
            var otherList;
            if (e.target.parentElement.parentElement.id == "rangestart") {
                otherList = document.getElementById("rangeend");
            } else if (e.target.parentElement.parentElement.id == "rangeend") {
                otherList = document.getElementById("rangestart");
            } else {
                return;
            }
            setTimeRangeOptions(allOptions, otherList, true);
            var otherOptions = otherList.getElementsByTagName("li");
            var toRemove = [];
            _.forEach(otherOptions, function(otherOption) {
                var otherOptionText = otherOption.getElementsByTagName("a")[0].innerHTML;
                var otherTime = getDateFromOption(otherOptionText);
                if (otherList.id == "rangeend" && otherTime.getTime() < timeSelected.getTime()) {
                    toRemove.push(otherOption);
                } else if (otherList.id == "rangestart" && otherTime.getTime() > timeSelected.getTime()) {
                    toRemove.push(otherOption);  
                }
            });
            _.forEach(toRemove, function(otherOption) {
                otherList.removeChild(otherOption);
            });
            // Make the selection visibly persist
            var menuBtn = e.target.parentElement.parentElement.parentElement.getElementsByClassName("btn")[0];
            menuBtn.innerHTML = e.target.innerHTML + "<span class='caret'></span>";
            // Re-render chart
            clearChart();
            loadChart(currAggregateBy, true);
        }
    });
}

function getDateFromOption(timeStr) {
    var time;
    if (timeStr.indexOf(":") != -1) { // Hour
        time = new Date("2000-01-" + (parseInt(timeStr.split(":")[0]) + 1));
    } else {
        time = new Date(timeStr);
    }
    return time;
}

function removeOutOfRange() {
    var chart = d3.select('.chart'); 
    chart
        .data(data)
        .selectAll('.mag0rects')
        .selectAll('rect')
        .data(function(d, i) {
            if (i == 0) return d;
        })
        .transition()
        .remove();
}

document.getElementsByTagName("body")[0].onload = loadChart("QuakeDate");