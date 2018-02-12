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
		
		$.ajax({
			async: "false",
			url: "rest/userService/getUser",
			type:"GET",
			dataType:"json",
			success : function(data) {
				if(data != undefined) {
					if (data == "GUEST"){
						alert("Ne mozete posetiti ovu stranicu kao gost");
						top.location.href ="pocetnaStranica.html";
					} else {
						alert(data);
					}
					
				} else {
					alert(data);
					
					top.location.href ="index.html";
				}s
			},
			error: function(jqxhr,textStatus,errorThrown){
				
				alert(errorThrown);
			}
		});
	
	
	
	var divForSubForums = $("#korisnici").empty();
	$.ajax({
		url: "rest/userService/getAllUsers",
		type:"GET",
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if(data!=null)
			
				$.each(data,function(i,User){
					renderUser(i,User);
					
				})
				
		}
		});
}

function renderUser(i,User){
	$("#korisnici").append("<tr>" +
			"<td> " + User.email + "</td>" +
			"<td>" + User.role + "</td><td><a href=\"#\" class=\"btn btn-small btn-success\" onclick=\"promote('"+User.email+"')\"> <span class=\"glyphicon glyphicon-arrow-up\"></span><i class=\"btn-icon-only icon-ok\"></i>" +
			"Promote</a> &nbsp&nbsp<a href=\"#\" class=\"btn btn-small btn-primary\" onclick=\"demote('"+User.email+"')\"> <span class=\"glyphicon glyphicon-arrow-down\"></span><i class=\"btn-icon-only icon-remove\"></i>	Demote</a></td></tr>");
}

function promote(i){
	
	$.ajax({
		url: "rest/userService/promoteUser/" + i ,
		type:"GET",
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= undefined) {
				$("#korisnici").empty();
				$.each(data,function(i,User){
					renderUser(i,User);
					
				})
				
				
			}else{
				alert("Nemate ovlascenja za ovu operaciju ili pokusavate da promotujete administratora");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function demote(i){
	alert(i);
	$.ajax({
		url: "rest/userService/demoteUser/" + i ,
		type:"GET",
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				$("#korisnici").empty();
				$.each(data,function(i,User){
					renderUser(i,User);
					
				})
				
				
			}else{
				alert("Nemate ovlascenja za ovu operaciju ili pokusavate da demotujete obicnog korisnika");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}