function showRegisterForm(e) {
	if (e) {
		e.stopPropagation();
	}
	document.getElementById("register-div").style.display = "block";
	document.getElementById("logout-div").style.display = "none";
	document.getElementById("login-div").style.display = "none";
}

function showLoginForm(e) {
	if (e) {
		e.stopPropagation();
	}	
	document.getElementById("register-div").style.display = "none";
	document.getElementById("logout-div").style.display = "none";
	document.getElementById("login-div").style.display = "block";
}

function showLogoutForm(e) {
	if (e) {
		e.stopPropagation();
	}	
	document.getElementById("register-div").style.display = "none";
	document.getElementById("logout-div").style.display = "block";
	document.getElementById("login-div").style.display = "none";
}

function registerUser() {
	var name = document.getElementById("name").value;
	var email = document.getElementById("email-register").value;
	var password = document.getElementById("password-register").value;
	var newUser = {
			name: name,
			email: email,
			password: password
	};
	var auth = "Basic " + btoa(email + ":" + password);
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: "users/create",
		dataType: "json",
		data: JSON.stringify(newUser),
		success: function(result) {
			$.notify({
				// options
				message: 'Welcome, ' + result.name + '. You have been logged in.' 
			},{
				// settings
				type: 'success'
			});
			console.log(result);
			setUserSession(result);
			sessionStorage.setItem("authorization", auth);
			displayUsername();
			$(".dropdown-toggle").dropdown("toggle");
			showLogoutForm();
		},
		error: function(err) {
			$.notify({
				// options
				message: 'There was an issue registering your account. Please check the details and try again.' 
			},{
				// settings
				type: 'warning'
			});
			console.log(err);
		}
	});
}

function loginUser() {
	var email = document.getElementById("email-login").value;
	var password = document.getElementById("password-login").value;
	var auth = "Basic " + btoa(email + ":" + password);
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "users/login",
		headers: {
			  "Authorization": auth
		},
		success: function(result) {
			$.notify({
				// options
				message: 'Welcome back, ' + result.name + '!' 
			},{
				// settings
				type: 'success'
			});
			setUserSession(result);
			sessionStorage.setItem("authorization", auth);
			displayUsername();
			$(".dropdown-toggle").dropdown("toggle");
			showLogoutForm();
		},
		error: function(err) {
			$.notify({
				// options
				message: 'Your login credentials were not correct.' 
			},{
				// settings
				type: 'warning'
			});
			console.err(err);
		}
	});
}

function saveUser() {
	$.ajax({
		type: "PUT",
		contentType: "application/json",
		url: "users/update",
		dataType: "json",
		data: sessionStorage.getItem("user"),
		headers: {
			  "Authorization": sessionStorage.getItem("authorization")
		},
		success: function(result) {
			setUserSession(result);
			displayUsername();
		},
		error: function(err) {
			$.notify({
				// options
				message: 'There was an error saving your settings.' 
			},{
				// settings
				type: 'danger'
			});
			console.log(err);
		}
	});
}

function logoutUser() {
	sessionStorage.removeItem("user");
	displayUsername();
	$(".dropdown-toggle").dropdown("toggle");
	showLoginForm();
	$.notify({
		// options
		message: 'You have been signed out.' 
	},{
		// settings
		type: 'success'
	});
}

function displayUsername() {
	var user = getUserSession();
	if (user) {
		user = JSON.parse(user);
		var username = user.name;
		document.getElementById("user-account-dropdown").innerHTML = "Hi, " + username;
	} else {
		document.getElementById("user-account-dropdown").innerHTML = "Login";
	}
}

function setUserSession(user) {
	sessionStorage.setItem("user", JSON.stringify(user));
}

function getUserSession() {
	return sessionStorage.getItem("user");
}

function loadUserSettings() {
	if (getUserSession()) {
		displayUsername();
		showLogoutForm();	
	}
}

function saveUserSettings(settings) {
	var settings = {};
	settings.aggregateBy = currAggregateBy;
	settings.timeRangeStart = getDateFromOption(document.getElementById('dropdownMenu1').innerHTML.substring(0, 10));
	settings.timeRangeEnd = getDateFromOption(document.getElementById('dropdownMenu2').innerHTML.substring(0, 10));
	var user = JSON.parse(getUserSession());
	if (location.pathname == "/earthquake-viz/Magnitude") {
		var continentDropdown = document.getElementById("continentDropdown").innerHTML;
		var continentFilter = continentDropdown.replace("<span class=\"caret\"></span>", "").trim();
		if (continentFilter != "No Filter") {
			settings.location = continentFilter; 
		} else {
			settings.location = null;
		}
		user.earthquakeMagnitudeSettings = settings;
	} else if (location.pathname == "/earthquake-viz/QuakeLocation") {
		var magnitudeDropdown = document.getElementById("magnitudeDropdown").innerHTML;
		var magnitudeFilter = magnitudeDropdown.replace("<span class=\"caret\"></span>", "").trim();
		if (magnitudeFilter != "No Filter") {
			settings.magnitude = magnitudeFilter;
		} else {
			settings.magnitude = null;
		}
		user.earthquakeLocationSettings = settings;
	} else if (location.pathname == "/earthquake-viz/TsunamiCount") {
		var continentDropdown = document.getElementById("continentDropdown").innerHTML;
		var continentFilter = continentDropdown.replace("<span class=\"caret\"></span>", "").trim();
		if (continentFilter != "No Filter") {
			settings.location = continentFilter; 
		} else {
			settings.location = null;
		}
		user.tsunamiOccurrenceSettings = settings;
	} else if (location.pathname == "/earthquake-viz/TsunamiLocation") {
		var magnitudeDropdown = document.getElementById("magnitudeDropdown").innerHTML;
		var magnitudeFilter = magnitudeDropdown.replace("<span class=\"caret\"></span>", "").trim();
		if (magnitudeFilter != "No Filter") {
			settings.magnitude = magnitudeFilter;
		} else {
			settings.magnitude = null;
		}
		user.tsunamiLocationSettings = settings;
	}
	setUserSession(user);
	saveUser(user);
}

function loadChartSettings() {
	var settings = getCurrentChartSettings();
    if (!settings) return;
    if (settings.aggregateBy == "QuakeHour") {
    	document.querySelector(".aggregateBy:not(active)").click();
    }
    if (settings.timeRangeStart) {
    	var startOptions = document.getElementById("rangestart").getElementsByClassName("timeOption");
    	_.forEach(startOptions, function(option) {
    		if (option && option.innerHTML == getOptionFromDateString(settings.timeRangeStart)) {
				option.click();
    		}
    	});
    	// loadRangeEnd() will be called by the option click after it loads the 
    	//  updated options for rangeEnd
    } else if (settings.timeRangeEnd) {
    	loadRangeEnd();
    }
    if (settings.location) {
    	var continentDropdown = document.getElementById("continentDropdown").innerHTML;
    	if (continentDropdown.indexOf(settings.location) == -1) {
        	var locationOptions = document.getElementById("continentmenu").getElementsByTagName("li");
        	_.forEach(locationOptions, function(option) {
        		var anchor = option.getElementsByTagName("a")[0];
        		if (anchor && anchor.innerHTML == settings.location) {
        			anchor.click();
        		}
        	});
    	}
    }
    if (settings.magnitude) {
    	var magnitudeDropdown = document.getElementById("magnitudeDropdown").innerHTML;
    	if (magnitudeDropdown.indexOf(settings.magnitude) == -1) {
        	var magnitudeOptions = document.getElementById("magnitudemenu").getElementsByTagName("li");
        	_.forEach(magnitudeOptions, function(option) {
        		var anchor = option.getElementsByTagName("a")[0];
        		if (anchor && anchor.innerHTML == settings.magnitude) {
        			anchor.click();
        		}
        	});
    	}
    }
}

function getCurrentChartSettings() {
	var settings = null;
	var user = JSON.parse(getUserSession());
	if (!user) return;
    if (location.pathname == "/earthquake-viz/Magnitude") {
    	settings = user.earthquakeMagnitudeSettings;
    } else if (location.pathname == "/earthquake-viz/QuakeLocation") {
    	settings = user.earthquakeLocationSettings;
    } else if (location.pathname == "/earthquake-viz/TsunamiCount") {
    	settings = user.tsunamiOccurrenceSettings;
    } else if (location.pathname == "/earthquake-viz/TsunamiLocation") {
    	settings = user.tsunamiLocationSettings;
    }
    return settings;
}

function loadRangeEnd() {
	var settings = getCurrentChartSettings();
    if (!settings) return;
    if (settings.timeRangeEnd) {
    	var endOptions = document.getElementById("rangeend").getElementsByClassName("timeOption");
    	_.forEach(endOptions, function(option) {
    		if (option.innerHTML == getOptionFromDateString(settings.timeRangeEnd)) {
    			option.click();
    		}
    	});
    }
}

function loadQuakeMagnitudeData(blockLoadUserSettings) {
	if (!blockLoadUserSettings) loadUserSettings();
	var getURL = "chart-data/quake/magnitude";
	var continentDropdown = document.getElementById("continentDropdown").innerHTML;
	var continentFilter = continentDropdown.replace("<span class=\"caret\"></span>", "").trim();
	if (continentFilter != "No Filter") getURL += "/continent/" + continentFilter;
	$.ajax({
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		url: getURL,
		success: function(result) {
			rawData = result;
			clearChart();
			var settings = getCurrentChartSettings();
			var aggregateBy = "QuakeDate";
			if (settings && settings.aggregateBy) aggregateBy = settings.aggregateBy;
			loadChart(aggregateBy);
			loadChartSettings();
			if (loadChart.toString().indexOf("function loadChart") != -1) {
				var origLoadChart = loadChart;
				loadChart = function(aggregateBy, preserveMenuLabel) {
					origLoadChart(aggregateBy, preserveMenuLabel)
					if (getUserSession()) {
						saveUserSettings();
					}
				};	
			}
		},
		error: function(err) {
			$.notify({
				// options
				message: 'There was retrieving the earthquake data.' 
			},{
				// settings
				type: 'danger'
			});
			console.log(err);
		}
	});
}

function loadQuakeLocationData() {
	loadUserSettings();
	$.ajax({
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		url: "chart-data/quake/location",
		success: function(result) {
			rawData = result;
			var settings = getCurrentChartSettings();
			var aggregateBy = "QuakeDate";
			if (settings && settings.aggregateBy) aggregateBy = settings.aggregateBy;
			loadChart(aggregateBy);
			setMagnitudeOptions();
			loadChartSettings();
			var origLoadChart = loadChart;
			loadChart = function(aggregateBy, preserveMenuLabel) {
				origLoadChart(aggregateBy, preserveMenuLabel)
				if (getUserSession()) {
					saveUserSettings();
				}
			};
		},
		error: function(err) {
			$.notify({
				// options
				message: 'There was retrieving the earthquake data.' 
			},{
				// settings
				type: 'danger'
			});
			console.log(err);
		}
	});
}

function loadTsunamiLocationData() {
	loadUserSettings();
	$.ajax({
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		url: "chart-data/tsunami/location",
		success: function(result) {
			rawData = result;
			var settings = getCurrentChartSettings();
			var aggregateBy = "QuakeDate";
			if (settings && settings.aggregateBy) aggregateBy = settings.aggregateBy;
			loadChart(aggregateBy);
			setMagnitudeOptions();
			loadChartSettings();
			var origLoadChart = loadChart;
			loadChart = function(aggregateBy, preserveMenuLabel) {
				origLoadChart(aggregateBy, preserveMenuLabel)
				if (getUserSession()) {
					saveUserSettings();
				}
			};
		},
		error: function(err) {
			$.notify({
				// options
				message: 'There was retrieving the tsunami data.' 
			},{
				// settings
				type: 'danger'
			});
			console.log(err);
		}
	});
}

function loadTsunamiMagnitudeData(blockLoadUserSettings) {
	if (!blockLoadUserSettings) loadUserSettings();
	var getURL = "chart-data/tsunami/magnitude";
	var continentDropdown = document.getElementById("continentDropdown").innerHTML;
	var continentFilter = continentDropdown.replace("<span class=\"caret\"></span>", "").trim();
	if (continentFilter != "No Filter") getURL += "/continent/" + continentFilter;
	$.ajax({
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		url: getURL,
		success: function(result) {
			rawData = result;
			clearChart();
			var settings = getCurrentChartSettings();
			var aggregateBy = "QuakeDate";
			if (settings && settings.aggregateBy) aggregateBy = settings.aggregateBy;
			loadChart(aggregateBy);
			loadChartSettings();
			if (loadChart.toString().indexOf("function loadChart") != -1) {
				var origLoadChart = loadChart;
				loadChart = function(aggregateBy, preserveMenuLabel) {
					origLoadChart(aggregateBy, preserveMenuLabel)
					if (getUserSession()) {
						saveUserSettings();
					}
				};	
			}
		},
		error: function(err) {
			$.notify({
				// options
				message: 'There was retrieving the tsunami data.' 
			},{
				// settings
				type: 'danger'
			});
			console.log(err);
		}
	});
}