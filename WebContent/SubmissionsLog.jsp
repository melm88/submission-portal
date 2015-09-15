<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<% response.setHeader("Pragma","no-cache");%>
<% response.setHeader("Cache-Control","no-store");%>
<% response.setDateHeader("Expires",-1);%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Submissions Log</title>

<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

	<%if(session.getAttribute("loggedusername") == null || session.getAttribute("loggeduser") == null){
		response.sendRedirect("index.jsp");
	} else {
	if(!session.getAttribute("loggeduser").equals("pg@taramt.com") && !session.getAttribute("loggeduser").equals("melvin.m@taramt.com")){
		response.sendRedirect("SubmissionPage.jsp");
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
					<li><a href="SubmissionPage.jsp">Submit Assignment</a></li>						
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false"> <%=session.getAttribute("loggedusername") %> <span class="caret"></span></a>
						<ul class="dropdown-menu">							
							<!-- <li><a href="#">..</a></li> -->
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
		<div class="col-lg-1 col-md-1"></div>
		<div class="col-lg-10 col-md-10">
			<div id="submissiondropdown">
				<div class="form-horizontal">
					<div class="form-group">
						<label for="assignmentselection" class="col-lg-3 control-label">Assignment: </label>
						<div id="myselector" class="col-lg-9">
							<select id="assignmentselection" name="assignmentselection" class="form-control">
								<option value="choose">No Assignments Created</option>															
							</select>
						</div>
					</div>
				</div>				
			</div>
			<br/><br/>
			<div id="results">
			</div>
		</div>
		
		<div class="col-lg-1 col-md-1"></div>
			
	</div>
	
	
	
	
	
	
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="js/jquery-1_11_3.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.min.js"></script>
	
	<script>
	$.post('GetAssignments', function(data){
		if(data != "fail"){				
			var jsonarr = data["assignments"];
			var seloptions = '<select id="assignmentselection" name="assignmentselection" class="form-control"><option value="choose">Select Assignment</option>';
			var count=0;
			for(ele in jsonarr){					
				seloptions = seloptions + '<option value="'+jsonarr[ele]+'">'+jsonarr[ele]+'</option>';
				count = count + 1;
			}
			seloptions = seloptions+"</select>";
			var seloptions2 = '<select id="assignmentselection" name="assignmentselection" class="form-control"><option value="choose">No Assignments Created</option></select>';
			if(count!=0)
				$('#myselector').html(seloptions);
			else
				$('#myselector').html(seloptions2);
		}
		
		$('#assignmentselection').on('change', function(e){
			var value = this.value;
			//alert(value);
			if(value != 'choose'){
				$.post('AjaxAssignmentSubmissions', {assgn: value}, function(data){
					if(data != "fail"){
						var jsonarr = data["data"];
						var tablecode = '<table class="table table-bordered"><tr><th>Email</th><th>Filename</th><th>Status</th><th>Score</th><th>Feedback</th><th>Submission Time</th><th>Viewed On</th></tr>';
						for(ele in jsonarr){					
							temp = jsonarr[ele];
							tablecode = tablecode + "<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td><td>"+temp[2]+"</td><td>"+temp[3]+"</td><td>"+temp[4]+"</td><td>"+temp[5]+"</td><td>"+temp[6]+"</td></tr>";
						}
						tablecode = tablecode+"</table>";
						
						$('#results').html(tablecode);
					}
				});
			}
		});
	});
		
	
	</script>
	
	<%} %>
</body>
</html>