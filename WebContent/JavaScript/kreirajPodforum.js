window.onload = function(){
	$.ajax({
		async: "false",
		url: "rest/userService/userLoggedIn",
		type:"GET",
		dataType:"text",
		success : function(data) {
			if(data == "true") {
				
				
			} else {
				alert("Niste ulogovani");
				top.location.href ="index.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			
			alert(errorThrown);
		}
	});
}

function getData(){
	var niz = [];
	var i = 0;
	$("#listaIzabranih option").each(function()
			{
			    niz[i] = $(this).val();
			    i++;
			});
	
	var data = JSON.stringify({
		"name": $('#createSubForum input[name=name]').val(),
		"description": $('#ta').val(),
		"image": $('#createSubForum input[name=image]').val(),
		"rules": $('#createSubForum input[name=rules]').val(),
		"moderatorsHelp": niz
	})
	return data;
}


function createSubForum() {
	if(!validation()){
		alert("Morate uneti ime i opis");
		return false;
	} else {
		var s = getData();
		
		$.ajax({
			url: "rest/userService/addSubForum",
			type:"POST",
			data: s,
			contentType:"application/json",
			dataType:"json",
			success : function(data){
				if (data == undefined) {
					alert("Vec postoji podforum sa takvim imenom ili nemate ovlascenja");
					top.location.href="pocetnaStranica.html";
				}else{
					
					alert("Uspesno kreiran podforum");
					top.location.href="pocetnaStranica.html";
				}
			},
			error: function(jqxhr,textStatus,errorThrown){
				alert(errorThrown);
			}
		});
	}
	
}

window.onload = function(){
	$("#combobox").empty();
	$.ajax({
		url: "rest/userService/getModerators",
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if(data!=null)
				$.each(data,function(i,Moderator){
					renderModerators(Moderator);
					
				})
		}
	});
}

function renderModerators(Moderator){
	
	var combo = $("#combobox").append("<option value = \""+ Moderator.email + " \" > " + Moderator.email + "</option> "); 
}

function addModerator(){
	
	
	var d = $("#combobox").val();
	alert(d);
	
	
    if(d==null){
    	alert("ne mozes vratitai nulllsa");
    	return false;
    	
    }
		
	
	$("#listaIzabranih").append("<option value=\" " + d + " \"> " + d  + "</option>");
	$('#combobox option:selected').remove();
	return false;
}

function validation(){
	var name;
	var content;
	
	var name = $("#name").val();
	alert(name);
	var content = $("#ta").val();
	alert(content);
	if (name === ""){
		return false;
	}
	
	if (content === ""){
		return false;
	}
	
	return true;
}

