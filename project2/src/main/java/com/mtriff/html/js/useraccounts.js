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
	// Register user
	$(".dropdown-toggle").dropdown("toggle");
}

function loginUser() {
	// Login user
	$(".dropdown-toggle").dropdown("toggle");
}

function logoutUser() {
	// Logout user
	$(".dropdown-toggle").dropdown("toggle");
}

function displayUsername() {
	var username = "Matt"; // get username from sessionstorage
	document.getElementById("user-account-dropdown").innerHTML = "Hi, " + username;
}