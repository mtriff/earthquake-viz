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

function getTsunamiMagnitudeFillClass(magnitude) {
    var magnitudeInt = parseInt(magnitude);
    if (magnitudeInt < 1) return "tsunami0";
    if (magnitudeInt < 2) return "tsunami1";
    if (magnitudeInt < 3) return "tsunami2";
    if (magnitudeInt < 4) return "tsunami3";
    if (magnitudeInt < 5) return "tsunami4";
    if (magnitudeInt < 6) return "tsunami5";
    if (magnitudeInt < 7) return "tsunami6";
    if (magnitudeInt < 8) return "tsunami7";
    if (magnitudeInt >= 8) return "tsunami8plus";
}

function hideTooltip(d) {
    div.transition()
        .style("opacity", 0);
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
            // Check if this was called while loading the user settings
            var dummyError = new Error();
            if (dummyError.stack.indexOf("useraccounts.js") >= 0) {
            	if (e.target.parentElement.parentElement.id == "rangestart") {
            		loadRangeEnd();
            	}
            }
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