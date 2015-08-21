<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet" type="text/css">

      <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
      <script src="https://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
  <script src="https://apis.google.com/js/api:client.js"></script>
  <script>
  var googleUser = {};
  var startApp = function() {
    console.log("Start app 3");
    gapi.load('auth2', function(){
      // Retrieve the singleton for the GoogleAuth library and set up the client.
      auth2 = gapi.auth2.init({
        client_id: '541092662543-qscr7h3usqer58saeo5prl2dpjrkob28.apps.googleusercontent.com',
        cookiepolicy: 'single_host_origin',
        // Request scopes in addition to 'profile' and 'email'
        //scope: 'additional_scope'
      });
      attachSignin(document.getElementById('customBtn'));
      console.log("Start app 4");
    });
  };

  function attachSignin(element) {
    console.log("Start app 5");
    console.log(element.id);
    auth2.attachClickHandler(element, {},
        function(googleUser) {
          document.getElementById('name').innerText = "Signed in: " +
              googleUser.getBasicProfile().getEmail();
          	  console.log(googleUser.getBasicProfile().getEmail());
               $.post("AjaxSession",{loggeduser:googleUser.getBasicProfile().getEmail(), name:googleUser.getBasicProfile().getName()},function(dataa){
					console.log(dataa);
					//window.location.href="Apply.html"
					if(dataa.trim()!="")
						location.reload();
					else
						alert("Error while logging in. Please try again.");
			}); 
        }, function(error) {
          alert(JSON.stringify(error, undefined, 2));
        });
    console.log("Start app 6");
  }
  </script>
  <style type="text/css">
    #customBtn {
      display: inline-block;
      background: #4285f4;
      color: white;
      width: 190px;
      border-radius: 5px;
      white-space: nowrap;
    }
    #customBtn:hover {
      cursor: pointer;
    }
    span.label {
      font-weight: bold;
    }
    span.icon {
      background: url('img/g-normal.png') transparent 5px 50% no-repeat;
      display: inline-block;
      vertical-align: middle;
      width: 42px;
      height: 42px;
      border-right: #2265d4 1px solid;
    }
    span.buttonText {
      display: inline-block;
      vertical-align: middle;
      padding-left: 42px;
      padding-right: 42px;
      font-size: 14px;
      font-weight: bold;
      /* Use the Roboto font that is loaded in the <head> */
      font-family: 'Roboto', sans-serif;
    }
  </style>
  </head>
  <body>
  <!-- In the callback, you would hide the gSignInWrapper element on a
  successful sign in -->
  <div id="gSignInWrapper">
    <span class="label">Sign in with:</span>
    <div id="customBtn" class="customGPlusSignIn">
      <span class="icon"></span>
      <span class="buttonText">Google</span>
    </div>
  </div>
  <div id="name"></div>
  <input type="hidden" name="loggeduser" id="loggeduser" value='<%= session.getAttribute("loggeduser")!=null?session.getAttribute("loggeduser"):""%>'></input>
  <script>
  $(document).ready(function(){
    console.log("Start app 1");
    if($('#loggeduser').val().trim() != ""){
    	window.location.href = "SubmissionPage.jsp";
    } else {
    	startApp();
    }
      
      console.log("Start app 2");
  	 /*$.post("/meta/existsesn",{},function(dataa){
					console.log(dataa);
					if(dataa == "None")
						startApp();
					else {
						$("#gSignInWrapper").hide();
						document.getElementById('name').innerText = "Signed with Email: " +
              dataa.split(" ")[0] + "  Name:  "  + dataa.split(" ")[1];
          }
					//window.location.href="Apply.html" 
			});*/

  });
  </script>
</body>
</html>