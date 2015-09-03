<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Mission Control</title>

<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- File Picker -->
<link href="css/fileinput.css" media="all" rel="stylesheet"
	type="text/css" />
</head>
<body>
	<%if(session.getAttribute("loggedusername") == null || session.getAttribute("loggeduser") == null){
		response.sendRedirect("index.jsp");
	} else {%>
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
					<%if(session.getAttribute("loggeduser").equals("pg@taramt.com") || session.getAttribute("loggeduser").equals("melvin.m@taramt.com")){%>
						<li><a href="SubmissionTemplate.jsp">Submission Template</a></li>
					<%} %>															
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
	
		<!-- Button trigger modal -->
		<button type="button" class="btn btn-primary btn-md" data-toggle="modal" data-target="#missionModal">
		  Create Mission
		</button>
		
		<!-- Button trigger modal -->
		<button type="button" class="btn btn-primary btn-md" data-toggle="modal" data-target="#subtaskModal">
		  Create Sub-Task
		</button>
		
		<!-- Mission Display Section -->
		<p class="lead" style="margin-top: 12px;">Missions: </p>
		<div class="" id="missiondiv">
			No Missions created yet.
		</div>		
		<!-- Mission Display Section [END] -->

		<!-- Modal -->
		<div class="modal fade" id="missionModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="myModalLabel">Create Mission</h4>
		      </div>
		      <div class="modal-body">
		      	<form id="missionForm" enctype="multipart/form-data" role="form">
			        <input type="text" class="form-control" name="missiontitle" id="missiontitle" placeholder="Mission Title" required></input><br/>
			        <textarea name="missiondesc" class="form-control" id="missiondesc" placeholder="Mission Description" required></textarea><br/>
			        <label>Assignment Name: </label> <input type="text" id="missionassgn" name="missionassgn" class="form-control" readonly></input>
			        
					<div class="input input-group-lg">
						<label for="file-0c">Question File (zip)</label>
						<input id="file-0c" name="file-0c" class="file" type="file"
							accept="application/zip" required>
					</div>
					<br/>
					<div class="input input-group-lg">
						<label for="file-0a">Submission Structure</label>
						<input id="file-0a" name="file-0a" class="file" type="file"
							accept="application/zip" required>
					</div>
					<br/>
					<div class="input input-group-lg">
						<label for="file-0b">Test Case (.java file)</label>
						<input id="file-0b" name="file-0b" class="file" type="file"
							accept="text/x-java-source" required>
					</div>
					<input type="hidden" name="modaltype" value="mission"></input>		        
		      </div>
		      <div class="modal-footer">
		        	<input type="submit" class="btn btn-primary" id="missionSubBt" value="Submit"></input>
		        </form>
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		      </div>
		    </div>
		  </div>
		</div>
		
		<!-- Modal -->
		<div class="modal fade" id="subtaskModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="myModalLabel">Create Sub-Task</h4>
		      </div>
		      <div class="modal-body">
		        <form id="subtaskForm" enctype="multipart/form-data" role="form">
		        	<div id="subselect">
			        	<select id="submission" name="submission" class="form-control">
			        		<option value="choice">No Mission Created</option>
			        	</select>
		        	</div>
		        	<br/>		        	
		        	<textarea name="subdesc" class="form-control" id="subdesc" placeholder="Sub-Task Description" required></textarea><br/>
		        	<label>Assignment Name: </label> <input type="text" id="subassgn" name="subassgn" class="form-control" readonly></input>
		        	<br/>
		        	<div class="input input-group-lg">
						<label for="file-1c">Question File (zip)</label>
						<input id="file-1c" name="file-1c" class="file" type="file"
							accept="application/zip" required>
					</div>
					<br/>
					<div class="input input-group-lg">
						<label for="file-1a">Submission Structure</label>
						<input id="file-1a" name="file-1a" class="file" type="file"
							accept="application/zip" required>
					</div>
					<br/>
					<div class="input input-group-lg">
						<label for="file-1b">Test Case (.java file)</label>
						<input id="file-1b" name="file-1b" class="file" type="file"
							accept="text/x-java-source" required>
		        	</div>
		        	<input type="hidden" name="modaltype" value="subtask"></input>	
		      </div>
		      <div class="modal-footer">
		        	<input type="submit" class="btn btn-primary" id="subtaskSubBt" value="Submit"></input>
		        </form>
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		      </div>
		    </div>
		  </div>
		</div>
	
	</div>
	
	
		
	
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="js/jquery-1_11_3.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/fileinput.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/missioncontrol.js"></script>	
	
	<%} %>
</body>
</html>