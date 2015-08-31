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
<title>Submission Report</title>

<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">

</head>
<body>
<body>
	<%if(session.getAttribute("loggedusername") == null || session.getAttribute("loggeduser") == null){
		response.sendRedirect("index.jsp");
	} else {
	%>
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
	<% if(request.getAttribute("score") != null){%>
	<div class="container">
		<div class="col-lg-2 col-md-2 col-sm-2 col-xs-12"></div>
		<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">
			<%if(request.getAttribute("status").toString().trim().equalsIgnoreCase("success")){%>
				<div class="panel panel-success">	
			<%} else{ %>
				<div class="panel panel-danger">	
			<%} %>
			  <div class="panel-heading">
			  	<%if(request.getAttribute("status").toString().trim().equalsIgnoreCase("success")){%>
			    	<h3 class="panel-title"><%=request.getParameter("file")+" Report" %><span class="pull-right" ><%=request.getAttribute("status")%></span></h3>
			    <%} else { %>
			    	<h3 class="panel-title"><%=request.getParameter("file")+" Report" %><span class="pull-right" ><%=request.getAttribute("status")%></span></h3>
			    <%} %>
			  </div>
			  <div class="panel-body table-responsive">
			    
			    <table class="table">
			    	<tr>
			    		<th>Assignment</th><th>Submission Time</th><th>Score</th><th>Feedback</th>
			    	</tr>
			    	<tr>
			    		<td><%=request.getParameter("assgn")%></td><td><%=request.getAttribute("submissiontime") %></td><td><%=request.getAttribute("score") %></td><td><%=request.getAttribute("feedback") %></td>
			    	</tr>
			    </table>
			  </div>
			</div>
		</div>
		<div class="col-lg-2 col-md-2 col-sm-2 col-xs-12"></div>
	</div>
	<%} %>
	
		
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="js/jquery-1_11_3.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.min.js"></script>
	
	
	<%} %>
</body>
</html>