<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${Title} - EarthquakeViz</title>

    <link rel="stylesheet" href="https://ajax.aspnetcdn.com/ajax/bootstrap/3.3.6/css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/site.css" />

    <script src="https://ajax.aspnetcdn.com/ajax/jquery/jquery-2.2.3.min.js"></script>
    <script src="https://ajax.aspnetcdn.com/ajax/bootstrap/3.3.6/bootstrap.min.js"></script>
    <script src="js/bootstrap-notify.js"></script>
    <script src="js/useraccounts.js"></script>
    <script src="js/d3.min.js"></script>
    <script src="js/lodash.js"></script>
    <script src="js/quakecommon.js"></script>
</head>
<body>
    <div class="navbar navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a asp-area="" asp-controller="Home" asp-action="Index" class="navbar-brand">EarthquakeViz</a>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li><a href="Index" class="navbar-quake-custom">Home</a></li>
                    <li><a href="Magnitude" class="navbar-quake-custom">Earthquake Magnitudes</a></li>
                    <li><a href="QuakeLocation" class="navbar-quake-custom">Earthquake Locations</a></li>
                    <li><a href="TsunamiCount" class="navbar-quake-custom">Tsunami Occurrences</a></li>
                    <li><a href="TsunamiLocation" class="navbar-quake-custom">Tsunami Locations</a></li>
<li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"><b id="user-account-dropdown">Login</b> <span class="caret"></span></a>
			<ul id="login-dp" class="dropdown-menu dropdown-menu-right">
				<li>
					 <div class="user-account">
							<div id="login-div" class="col-md-12">
								 <form class="form" role="form" accept-charset="UTF-8" id="login-nav">
										<div class="form-group">
											 <label class="sr-only" for="email-login">Email address</label>
											 <input type="email" class="form-control" id="email-login" placeholder="Email Address" required>
										</div>
										<div class="form-group">
											 <label class="sr-only" for="password-login">Password</label>
											 <input type="password" class="form-control" id="password-login" placeholder="Password" minlength=4 required>
										</div>
										<div class="form-group">
											 <button class="btn btn-success btn-block" onclick="loginUser()">Sign in</button>
										</div>
								 </form>
								 <div class="bottom text-center">
									New here? <a href="#" onclick="showRegisterForm(event)"><b>Join Us</b></a>
								</div>
							</div>

							<div id="register-div" class="col-md-12">
								 <form class="form" role="form" accept-charset="UTF-8" id="login-nav">
										<div class="form-group">
											 <label class="sr-only" for="name">Name</label>
											 <input type="text" class="form-control" id="name" placeholder="Name" required>
										</div>
										<div class="form-group">
											 <label class="sr-only" for="email-register">Email address</label>
											 <input type="email" class="form-control" id="email-register" placeholder="Email Address" required>
										</div>
										<div class="form-group">
											 <label class="sr-only" for="password-register">Password</label>
											 <input type="password" class="form-control" id="password-register" placeholder="Password" required>
										</div>
										<div class="form-group">
											 <button class="btn btn-success btn-block" onclick="registerUser()">Register</button>
										</div>
								 </form>
	 							<div class="bottom text-center">
									Have an account? <a href="#" onclick="showLoginForm(event)"><b>Login</b></a>
								</div>
							</div>
							
							<div id="logout-div" class="col-md-12">
	 							<div class="bottom text-center">
	 								<button class="btn btn-success btn-block" onclick="logoutUser()">Sign Out</button>
								</div>
							</div>
					 </div>
				</li>
				</ul>
            </div>
        </div>
    </div>
    <div class="container body-content">
        <#include RenderBody>
        <hr />
    </div>


</body>
</html>
