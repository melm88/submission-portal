<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<title>Submission Portal - Login</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="https://fonts.googleapis.com/css?family=Roboto"
			rel="stylesheet" type="text/css">
		<link
			href="http://netdna.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css"
			rel="stylesheet" id="bootstrap-css">
		<script src="js/jquery-1_11_3.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="https://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
		<script src="https://apis.google.com/js/api:client.js"></script>
		<script>
		  var googleUser = {};
		  var startApp = function() {
		    //console.log("Start app 3");
		    gapi.load('auth2', function(){
		      // Retrieve the singleton for the GoogleAuth library and set up the client.
		      auth2 = gapi.auth2.init({
		        client_id: '541092662543-qscr7h3usqer58saeo5prl2dpjrkob28.apps.googleusercontent.com',
		        cookiepolicy: 'single_host_origin',
		        // Request scopes in addition to 'profile' and 'email'
		        //scope: 'additional_scope'
		      });
		      attachSignin(document.getElementById('customBtn'));
		      //console.log("Start app 4");
		    });
		  };
		
		  function attachSignin(element) {
		    //console.log("Start app 5");
		    //console.log(element.id);
		    auth2.attachClickHandler(element, {},
		        function(googleUser) {
		          document.getElementById('name').innerText = "Signed in: " +
		              googleUser.getBasicProfile().getEmail();
		              $.post("AjaxSession",{loggeduser:googleUser.getBasicProfile().getEmail(), name:googleUser.getBasicProfile().getName()},function(dataa){
							//console.log(dataa);					
							if(dataa.trim()!="")
								location.reload();
							else
								alert("Error while logging in. Please try again.");
					}); 
		        }, function(error) {
		          alert(JSON.stringify(error, undefined, 2));
		        });
		    //console.log("Start app 6");
		  }
		  </script>
		
		<style type="text/css">
		body {
			background-color: #444;
			background: url(http://s18.postimg.org/l7yq0ir3t/pick8_1.jpg);
		}
		
		.form-signin input[type="text"] {
			margin-bottom: 5px;
			border-bottom-left-radius: 0;
			border-bottom-right-radius: 0;
		}
		
		.form-signin input[type="password"] {
			margin-bottom: 10px;
			border-top-left-radius: 0;
			border-top-right-radius: 0;
		}
		
		.form-signin .form-control {
			position: relative;
			font-size: 16px;
			font-family: 'Open Sans', Arial, Helvetica, sans-serif;
			height: auto;
			padding: 10px;
			-webkit-box-sizing: border-box;
			-moz-box-sizing: border-box;
			box-sizing: border-box;
		}
		
		.vertical-offset-100 {
			padding-top: 100px;
		}
		
		.img-responsive {
			display: block;
			max-width: 100%;
			height: auto;
			margin: auto;
		}
		
		.panel {
			margin-bottom: 20px;
			background-color: rgba(255, 255, 255, 0.75);
			border: 1px solid transparent;
			border-radius: 4px;
			-webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
			box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
		}
		</style>
		<script type="text/javascript">
	        window.alert = function(){};
	        var defaultCSS = document.getElementById('bootstrap-css');
	        function changeCSS(css){
	            if(css) $('head > link').filter(':first').replaceWith('<link rel="stylesheet" href="'+ css +'" type="text/css" />'); 
	            else $('head > link').filter(':first').replaceWith(defaultCSS); 
	        }
	    </script>
	</head>
	<body style="background-position: 132px 44px, 70px 35px, 35px 17px;">
		<script src="js/TweenLite.min.js"></script>	
		<div class="container">
			<div class="row vertical-offset-100">
				<div class="col-md-4 col-md-offset-4">
					<div class="panel panel-default">
						<div class="panel-heading">
							<div class="row-fluid user-row">
								<img src="img/logo_sm_2_mr_1.png" class="img-responsive"
									alt="Conxole Admin">
							</div>
						</div>
						<div id="gSignInWrapper">
							<div id="customBtn" class="customGPlusSignIn text-center">
								<input style="margin-top: 5px;" type="image"
									src="img/Red-signin-Long-base-44dp.png" alt="Submit">
							</div>
						</div>
						<div id="name"></div>
						<input type="hidden" name="loggeduser" id="loggeduser"
							value='<%= session.getAttribute("loggeduser")!=null?session.getAttribute("loggeduser"):""%>'></input>
					</div>
				</div>
			</div>
		</div>
	
	
		<script type="text/javascript">
		$(document).ready(function() {
			//console.log("Start app 1");
			if($('#loggeduser').val().trim() != ""){
				window.location.href = "SubmissionPage.jsp";
			} else {
				startApp();
			}
			$(document).mousemove(function(event) {
				TweenLite.to($("body"), 
				.5, {
					css: {
						backgroundPosition: "" + parseInt(event.pageX / 8) + "px " + parseInt(event.pageY / '12') + "px, " + parseInt(event.pageX / '15') + "px " + parseInt(event.pageY / '15') + "px, " + parseInt(event.pageX / '30') + "px " + parseInt(event.pageY / '30') + "px",
						"background-position": parseInt(event.pageX / 8) + "px " + parseInt(event.pageY / 12) + "px, " + parseInt(event.pageX / 15) + "px " + parseInt(event.pageY / 15) + "px, " + parseInt(event.pageX / 30) + "px " + parseInt(event.pageY / 30) + "px"
					}
				})
			})
		})
		</script>
	</body>
</html>