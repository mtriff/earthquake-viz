

// zoom and pan
// var zoom = d3.zoom()
//     .on("zoom",function() {
//         g.attr("transform","translate("+ 
//             d3.event.translate.join(",")+")scale("+d3.event.scale+")");
//         g.selectAll("path")  
//             .attr("d", path.projection(projection)); 
//   });

// var zoom = d3.zoom()
//     .scaleExtent([1, 40])
//     .translateExtent([[-100, -100], [width + 90, height + 100]])
//     .on("zoom", zoomed);

// function zoomed() {
//   view.attr("transform", d3.event.transform);
//   gX.call(xAxis.scale(d3.event.transform.rescaleX(x)));
//   gY.call(yAxis.scale(d3.event.transform.rescaleY(y)));
// }

// svg.call(d3.zoom().on("zoom", function () {
//         svg.attr("transform", d3.event.transform)
// }));




// d3.select(".chart")
//     .append('circle')
//     .attr("class", "mag7")
//     .attr("r", 4)
//     .attr("transform", function() {return "translate(" + projection([117.6071,255.636]) + ")";});;
// document.getElementsByTagName("body")[0].onload = loadChart("QuakeDate");


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
    height = 500;

    var projection = d3.geoEquirectangular()
        .scale(125)
        .translate([width / 2, height / 2])
        .precision(.1);

    var path = d3.geoPath()
        .projection(projection);

    var graticule = d3.geoGraticule();

    // var zoom = d3.zoom()
    //     .translate([width / 2, height / 2])
    //     .scale(scale0)
    //     .scaleExtent([scale0, 8 * scale0])
    //     .on("zoom", zoomed); 

    var svg = d3.select(".chart")
        .attr("width", width)
        .attr("height", height);

    function zoomed() {
        var transform = d3.event.transform;
        d3.selectAll('circle')
            .attr("transform", d3.event.transform);  //"translate(" + .join(',') + ")"); //function(d) { 
                // var proj = projection(transform.applyX(d["Longitude"]), transform.applyY(d["Latitude"]))
                // var proj = transform(projection(d["Longitude"], d["Latitude"]));
                // return "translate(" + transform.apply(projection(d["Longitude"], d["Latitude"])) + ")"; });
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

    // Plot quake occurrences
    d3.select(".chart")
        .selectAll('circle')
        .data(data)
        .enter()
        .append('circle')
        .attr("class", function(d) { return getMagnitudeFillClass(d["Magnitude"]); })
        .attr("r", 4)
        .attr("cx",  function(d) { return projection([d["Longitude"],d["Latitude"]])[0] })
        .attr("cy",  function(d) { return projection([d["Longitude"],d["Latitude"]])[1] })
        .click();

    document.getElementsByClassName('chart')[0].click();
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
        .selectAll('circle')
        .remove();
    d3.selectAll('.tooltip')
        .remove();
    d3.selectAll('g')
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