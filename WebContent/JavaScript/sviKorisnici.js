var listaKorisnika;
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
						//alert(data);
					}
					
				} else {
					alert(data);
					
					top.location.href ="index.html";
				}
			},
			error: function(jqxhr,textStatus,errorThrown){
				
				alert(errorThrown);
			}
		});
	
	
	var divForSubForums = $("#korisnici").empty();
	$.ajax({
		url: "rest/userService/getAllUsers2",
		type:"GET",
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if(data!=null)
				listaKorisnika = data;
				$.each(data,function(i,User){
					renderUser(i,User);
					
				})
				
		}
		});
}

function renderUser(i,User){
	$("#korisnici").append("<tr id=\""+i+"\">" +
			"<td>" + User.name + "</td>" +
			"<td>" + User.username + "</td>" +
			"<td>" + User.email + "</td>" +
			"<td>" + User.role + "</td>" +
			"<td >" +
			"<a href=\"javascript:;\" class=\"btn btn-small btn-success\" onclick=\"promote('"+User.email+"')\"> <span class=\"glyphicon glyphicon-arrow-up\"></span>Promote	</a>" +
			"<a href=\"javascript:;\" class=\"btn btn-small btn-primary\" onclick=\"demote('"+User.email+"')\"> <span class=\"glyphicon glyphicon-arrow-down\"></span>Demote	</a>" +
			"<a href=\"posaljiPoruku.html\" class=\"btn btn-small btn-danger pull-right\"> <span class=\"glyphicon glyphicon-envelope\"></span>Posalji poruku</a>" +
			"</td></tr>");
}

function promote(i){
	
	$.ajax({
		url: "rest/userService/promoteUser2/" + i ,
		type:"GET",
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				$("#korisnici").empty();
				listaKorisnika=data;
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
	
	$.ajax({
		url: "rest/userService/demoteUser2/" + i ,
		type:"GET",
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				$("#korisnici").empty();
				listaKorisnika=data;
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

function pretrazi(){

	
	var dzejson = JSON.stringify($("#username").val());
	
	
	
	$("#korisnici").empty();
	$.ajax({
		url: "rest/userService/pretraziKorisnike" ,
		type:"POST",
		data: dzejson,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			
			
			$.each(listaKorisnika,function(i,User){
					renderUser(i,User);
			})

			if (data!= null) {
				$.each(data,function(i,Index){
					
					
					$('#'+Index).hide();
				})
				
				
			}else{
				alert("Poslo je po zlu");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});  
}
