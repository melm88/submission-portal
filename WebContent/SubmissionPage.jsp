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
<title>Submissions Page</title>

<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- File Picker -->
<link href="css/fileinput.css" media="all" rel="stylesheet"
	type="text/css" />
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
					<li><a href="SubmissionTemplate.jsp">Submission Template</a></li>									
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
		<div class="col-lg-3 col-md-3 col-sm-3 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-10">
			
			<div id="submissiondropdown">
				<div class="form-horizontal">
					<div class="form-group">
						<label for="assignmentselection" class="col-sm-2 control-label">Assignment: </label>
						<div id="myselector" class="col-sm-10">
							<select id="assignmentselection" name="assignmentselection" class="form-control">
								<option value="choose">No Assignments Created</option>															
							</select>
						</div>
					</div>
				</div>				
			</div>
			
			<div id="submissionstructure">
				<div class="panel panel-default">
				  <div class="panel-heading">
				    <h3 class="panel-title">Submission File - Structure</h3>
				  </div>
				  <div class="panel-body">
				    Panel content
				  </div>
				</div>
			</div>
			
			<div id="submissiondiv">
				<form id="modalform" class="contact" action="AssignmentHandler"
					method="post" enctype="multipart/form-data" role="form">
					<fieldset>
						<br />
						<div class="input input-group-lg">
							<input id="file-0a" name="file-0a" class="file" type="file"
								accept="application/zip">
							<input type="hidden" name="selectedattribute" id="selectedattribute" value=""></input>
						</div>
					</fieldset>
				</form>
			</div>
			
			<div id="submissionresult">
				<h3>Submissions: </h3>
				<div id="resultdiv">
					<blockquote>
						<p>No Submissions yet !</p>		
					</blockquote>	
				</div>
			</div>
			<input type="hidden" id="loggeduser" name="loggeduser" value='<%=session.getAttribute("loggeduser")%>'>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-3 col-xs-1"><%if(session.getAttribute("flashmsg")!=null){ %><span id="flashmsg"><%=session.getAttribute("flashmsg") %></span><% session.removeAttribute("flashmsg"); }%></div>
	</div>	


	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="js/jquery-1_11_3.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/fileinput.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js"></script>
	
	<script>
		$(document).ready(function(){
			$('#submissionstructure').hide();
			$('#submissiondiv').hide();
			$('#submissionresult').hide();			
			
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
					if(value != "choose"){
						$('#submissionstructure').show();
						$('#submissiondiv').show();
						$('#selectedattribute').val(value);
						$('#submissionresult').show();
						$.post('ResultGenerator', {selector: $('#assignmentselection').val()}, function(data){
							if(data != "fail"){				
								var jsonarr = data[$('#loggeduser').val()];
								var tablecode = '<table class="table table-bordered"><tr><th>File Name</th><th>Status</th><th>Score</th><th>Timestamp</th></tr>';
								var counts = 0;
								for(ele in jsonarr){					
									temp = jsonarr[ele];
									tablecode = tablecode + "<tr><td>"+temp["filename"]+"</td><td>"+temp["status"]+"</td><td>&nbsp;</td><td>"+temp["timestamp"]+"</td></tr>";
									counts = counts+1;
								}
								tablecode = tablecode+"</table>";
								//alert(tablecode);
								if(counts>0)
									$('#resultdiv').html(tablecode);
							}								
						});
					} else {
						$('#selectedattribute').val("");
						$('#submissionstructure').hide();
						$('#submissiondiv').hide();
						$('#submissionresult').hide();
					}
				});
					
			});
			
		});
		
		$("#file-0a").fileinput({
	    	allowedFileExtensions : ['zip'],
	    	browseClass: "btn btn-info",
	    	browseLabel: "Browse",
	    	browseIcon: '<i class="glyphicon glyphicon-folder-open"></i> ',
	    	removeClass: "btn btn-danger",
	    	uploadClass: "btn btn-success",
	        minFileCount: 1,
	        maxFileCount: 1,
	        showUpload: true,		        
	        slugCallback: function(filename) {		        	
	            return filename.replace('(', '_').replace(']', '_');
	        }		       
	        
	    });	    
	</script>
</body>
</html>