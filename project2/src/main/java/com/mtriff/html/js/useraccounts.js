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
		url: "/users/create",
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
		type: "GET",
		contentType: "application/json",
		url: "/users/login",
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
		url: "/users/update",
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