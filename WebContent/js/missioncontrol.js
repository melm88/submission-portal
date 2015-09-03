/**
 * JavaScript for Mission Control Page.
 */

$('#missiontitle').on('change', function(){
	//alert("change triggered");
	if($('#missiontitle').val().trim() != ''){
		$('#missionassgn').val($('#missiontitle').val()+'_Final');
	} else {
		$('#missionassgn').val();
	}

});	

$.ajax({
	url: "AjaxGetMission",
	type: "POST",
}).done(function(data){
	//alert(data["mid"]);
	if(data["mid"].length != 0){
		var displayOP = '<div class="list-group">';
		var output = '<select id="submission" name="submission" class="form-control"><option value="choice">Choose Mission</option>';
		for(ele in data["mid"]){
			output = output + '<option value="'+data["mid"][ele]+'">'+data["titles"][ele]+'</option>';
			displayOP = displayOP + '<a class="list-group-item" href="VEDMission?id='+data["mid"][ele]+'">'+data["titles"][ele]+'</a>';
		}
		output = output + '</select>';
		displayOP = displayOP + '</div>';
		$('#subselect').html(output);
		$('#missiondiv').html(displayOP);
		$('#submission').on('change',function(){
			alert($('#submission').val());
			if($('#submission').val() != 'choice'){
				$.ajax({
					url: "AjaxSubtaskCount",
					type: "POST",
					data: {missionid: $('#submission').val()}
				}).done(function(data){
					alert("in side submission onChange: "+data);
					if(data.trim() == 0){
						$('#subassgn').val($("#submission option:selected").text()+"_subtask_1");
					} else {
						$('#subassgn').val($('#submission option:selected').text()+"_subtask_"+(parseInt(data.trim())+1));
					}
				});

			} else {
				$('#subassgn').val("");
			}
		});

	}

}).error(function(msg){
	alert("Error: "+msg);
});

$('#missionForm').submit(function(){
	//alert("missionSubmission");
	var error_points = "Please fill out the following: \n\n";
	var flag = false;

	if($('#missiontitle').val().trim() != ""){

	} else {
		//alert("Enter Mission Title");
		error_points = error_points + "- Mission Title \n";
		flag = true;
	}

	if($('#missiondesc').val().trim() != ""){

	} else {
		//alert("Enter Mission Desc");
		error_points = error_points + "- Mission Description \n";
		flag = true;
	}

	if(flag){
		alert(error_points);
		return false;
	} else {
		//alert("here");
		var datas = new FormData($('#missionForm')[0]);
		//alert(datas);
		$.ajax({
			url: 'AjaxMissionControl',
			data: datas,
			cache: false,
			contentType: false,
			processData: false,
			type: "POST",
			success: function(data){
				alert(data);
				console.log(data);
				//return false;
			},
			error: function(msg){
				alert(msg);
				console.log(msg);
			}
		});
		//return true;
	}

});

$('#subtaskForm').submit(function(){
	//alert("Sub task submission");
	var error_points = "Please fill out the following: \n\n";
	var flag = false;

	if($('#subdesc').val().trim() != ""){

	} else {
		error_points = error_points + "- Subtask Description \n";
		flag = true;
	}

	if($('#submission').val() != "choice"){

	} else {
		//alert("Enter Mission Desc");
		error_points = error_points + "- Choose a Mission \n";
		flag = true;
	}

	if(flag){
		alert(error_points);
		return false;
	} else {
		//alert("here");
		var datas = new FormData($('#subtaskForm')[0]);
		//alert(datas);
		$.ajax({
			url: 'AjaxMissionControl',
			data: datas,
			cache: false,
			contentType: false,
			processData: false,
			type: "POST",
			success: function(data){
				alert(data);
				console.log(data);
				//return false;
			},
			error: function(msg){
				alert(msg);
				console.log(msg);
			}
		});
		//return true;
	}
});

$("#file-0c").fileinput({
	allowedFileExtensions : ['zip'],
	browseClass: "btn btn-info",
	browseLabel: "Browse",
	browseIcon: '<i class="glyphicon glyphicon-folder-open"></i> ',
	removeClass: "btn btn-danger",
	uploadClass: "btn btn-success",
	minFileCount: 1,
	maxFileCount: 1,
	showUpload: false,
	showPreview: false,
	slugCallback: function(filename) {		        	
		return filename.replace('(', '_').replace(']', '_');
	}		       

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
	showUpload: false,
	showPreview: false,
	slugCallback: function(filename) {		        	
		return filename.replace('(', '_').replace(']', '_');
	}		       

});	    

$("#file-0b").fileinput({
	allowedFileExtensions : ['java'],
	browseClass: "btn btn-info",
	browseLabel: "Browse",
	browseIcon: '<i class="glyphicon glyphicon-folder-open"></i> ',
	removeClass: "btn btn-danger",
	uploadClass: "btn btn-success",
	minFileCount: 1,
	maxFileCount: 1,
	showUpload: false,
	showPreview: false,
	slugCallback: function(filename) {		        	
		return filename.replace('(', '_').replace(']', '_');
	}		       

});	    

$("#file-1c").fileinput({
	allowedFileExtensions : ['zip'],
	browseClass: "btn btn-info",
	browseLabel: "Browse",
	browseIcon: '<i class="glyphicon glyphicon-folder-open"></i> ',
	removeClass: "btn btn-danger",
	uploadClass: "btn btn-success",
	minFileCount: 1,
	maxFileCount: 1,
	showUpload: false,
	showPreview: false,
	slugCallback: function(filename) {		        	
		return filename.replace('(', '_').replace(']', '_');
	}		       

});	    

$("#file-1a").fileinput({
	allowedFileExtensions : ['zip'],
	browseClass: "btn btn-info",
	browseLabel: "Browse",
	browseIcon: '<i class="glyphicon glyphicon-folder-open"></i> ',
	removeClass: "btn btn-danger",
	uploadClass: "btn btn-success",
	minFileCount: 1,
	maxFileCount: 1,
	showUpload: false,
	showPreview: false,
	slugCallback: function(filename) {		        	
		return filename.replace('(', '_').replace(']', '_');
	}		       

});	    

$("#file-1b").fileinput({
	allowedFileExtensions : ['java'],
	browseClass: "btn btn-info",
	browseLabel: "Browse",
	browseIcon: '<i class="glyphicon glyphicon-folder-open"></i> ',
	removeClass: "btn btn-danger",
	uploadClass: "btn btn-success",
	minFileCount: 1,
	maxFileCount: 1,
	showUpload: false,
	showPreview: false,
	slugCallback: function(filename) {		        	
		return filename.replace('(', '_').replace(']', '_');
	}		       

});