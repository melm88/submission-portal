<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Assignment Results</title>

<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
	<%if(session.getAttribute("loggedusername") == null || session.getAttribute("loggeduser") == null){
		response.sendRedirect("index.jsp");
	}%>
	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="index.jsp">Assignment Portal</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<!-- <ul class="nav navbar-nav">
					<li class="active"><a href="#">Link <span class="sr-only">(current)</span></a></li>
					<li><a href="#">Link</a></li>
				</ul> -->
				<ul class="nav navbar-nav navbar-right">					
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false"> <%=session.getAttribute("loggedusername") %> <span class="caret"></span></a>
						<ul class="dropdown-menu">							
							<!-- <li><a href="#">..</a></li> -->							
							<li role="separator" class="divider"></li>
							<li><a href="AjaxLogout">Logout</a></li>
						</ul></li>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container-fluid -->
	</nav>
	<br />
	<br />


	<div class="container">
		<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1"></div>
		<div id="resultdiv" class="col-lg-10 col-md-10 col-sm-10 col-xs-10">
			
		</div>
		<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1"></div>
		<input type="hidden" id="loggeduser" name="loggeduser" value='<%=session.getAttribute("loggeduser")%>'>
	</div>	


	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="js/jquery-1_11_3.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.min.js"></script>
	
	<script>
	$(document).ready(function() {
		$.post('ResultGenerator', function(data){
			if(data != "fail"){				
				var jsonarr = data[$('#loggeduser').val()];
				var tablecode = '<table class="table table-bordered"><tr><th>File Name</th><th>Status</th><th>Feedback</th><th>Timestamp</th></tr>';
				for(ele in jsonarr){					
					temp = jsonarr[ele];
					tablecode = tablecode + "<tr><td>"+temp["filename"]+"</td><td>"+temp["status"]+"</td><td>"+temp["feedback"]+"</td><td>"+temp["timestamp"]+"</td></tr>";
				}
				tablecode = tablecode+"</table>";
				//alert(tablecode);
				$('#resultdiv').html(tablecode);
			}
				
		});
	});
	</script>
	
</body>
</html>