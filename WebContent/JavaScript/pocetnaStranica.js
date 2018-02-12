var listaPodforuma
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
	
	
	var divForSubForums = $("#subForums").empty();
	$.ajax({
		url: "rest/userService/getSubForums",
		type:"GET",
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if(data!=null){
				listaPodforuma = data;
				$.each(data,function(i,SubForum){
					renderSubForum(i,SubForum);
					
				})
			}
		}
		});
	
};

function renderSubForum(i,SubForum){

	var imgConcat = SubForum.image;
	imgConcat = imgConcat.concat(i);
	var nameConcat = SubForum.name;
	nameConcat = nameConcat.concat(i);
	var str1 = "<div class=\"row\" id=\"subForum"+ i + "\"><div class=\"col-md-12\" id=\"d\"><div class=\"panel panel-primary\" id=\"subForums3\">" +
			"<div class=\"panel-heading\" id=\"subForums4\"><div class=\"panel-title pull-left\" id=\"subForums5Slika\">" +
			"<img src=\"\" " + SubForum.image + "\" class=\"img-rounded\" alt=\"ALT\" width=\"32\" height=\"32\">" +
			"<a href=\"#\" onclick=\"klikNaSubForum( " + i + ")\" > " + SubForum.name +"</a></div>" +
			"<div class=\"panel-title pull-right\" id=\"subForum6OdgovorniModerator\"><small>"+SubForum.mainModerator +"</small></div>" +
			"<div class=\"clearfix\" id=\"subForums7\"></div></div><div class=\"panel-body\" id=\"subForums8Opis\">" + SubForum.description + " </div>" +
			"<div class=\"panel-footer \" id=\"subForums9\"> <div class = \"row\" id=\"subForums10\">" +
			"<div class = \"col-sm-6\" id=\"subForums11Pravilo\"> Spisak pravila :  " + SubForum.rules + "   </div>" +
			"<div class = \"col-sm-6\" id=\"subForums13\" >  <div class=\"dropdown pull-right\" id=\"subForums14\">" +
			" <button class=\"btn btn-primary dropdown-toggle\" type=\"button\" data-toggle=\"dropdown\">" +
	"Moderatori<span class =\"caret\"></span></button>" +
	"<ul class=\"dropdown-menu\">";
	
	for (var k = 0; k < SubForum.moderators.length; k++) {
		str1 = str1.concat("<li> "+ SubForum.moderators[k].email + "</li>");
	} 
	
	str1 = str1.concat("</ul>" +
	"</div><a class=\"dropdown pull-right\" href=\"#\" onclick=\"obrisiSubForum(" + i + ")\"><span class=\"glyphicon glyphicon-remove\" ></span> Obrisi &nbsp&nbsp&nbsp </a>" +
		  "<a class=\"dropdown pull-right\" href=\"#\" onclick=\"pratiSubForum(" + i + ")\"> <span class=\"glyphicon glyphicon-star\" >  </span> Prati &nbsp&nbsp&nbsp </a>" +
		  "<a class=\"dropdown pull-right\" href=\"#\" onclick=\"zalba(" + i + ")\"> <span class=\"glyphicon glyphicon-exclamation-sign\" >  </span> Zalba &nbsp&nbsp&nbsp </a>" +
			" </div>   </div></div> </div> </div> </div>");
	
	
	
	var divForSubForums = $("#subForums");
	divForSubForums.append(str1);
		
	
	
}

function zalba(i){
	alert(i);
	var data = JSON.stringify(i);
	$.ajax({
		url: "rest/userService/goToSubForum",
		type:"POST",
		data: data,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				top.location.href="kreirajZalbu0.html";
				
				
			}else{
				alert("Uspesno setovanje");
				top.location.href="kreirajZalbu0.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function addSubForumPage() {
		top.location.href="kreirajPodforum.html";
}




function klikNaSubForum(i){
	
	
	var data = JSON.stringify(i);
	$.ajax({
		url: "rest/userService/goToSubForum",
		type:"POST",
		data: data,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				top.location.href="podforum.html";
				
				
			}else{
				alert("Registration successeful");
				top.location.href="pocetnaStranica.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}


function obrisiSubForum(i){
	$.ajax({
		url: "rest/userService/obrisiSubForum/" + i ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= undefined) {
				$("#subForums").empty();
				$.each(data,function(i,SubForum){
					renderSubForum(i,SubForum);
					
				})
				
				
			}else{
				alert("Ne mozete obrisati podforum jer nemate ovlascenja");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function pratiSubForum(i){
	//Treba poslati serveru podforum koji smo kliknuli da pratimo i taj podforum smestiti u listu svih zapracenih foruma korisnika
	
	$.ajax({
		url: "rest/userService/followSubForum/" + i,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data) {
				alert("Uspesno ste zapratili podforum");
				
				
			}else{
				alert("Ne mozete dvaput zapratiti isti podforum ili ste prijavljeni kao gost");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
	
	
}

function pretrazi(){
	
	var naslovChecked = $('#pretragaPoNaslovu').is(":checked");
	var opisChecked = $('#pretragaPoOpisu').is(":checked");
	var moderatorChecked = $('#pretragaPoModeratoru').is(":checked");
	
	var dzejson;
	
	var naslov;
	var opis;
	var moderator;
	
	if (naslovChecked){
		naslov = $("#name").val();
	} else {
		naslov = "";
	}
	
	if (opisChecked){
		opis = $("#description").val();
	} else {
		opis = "";
	}
	
	if (moderatorChecked){
		moderator = $("#mainModerator").val();
	} else {
		moderator = "";
	}
	
	
	
	dzejson = JSON.stringify({
		"name": naslov,
		"description" : opis,
		"mainModerator" : moderator
	});
	
	$("#subForums").empty();
	$.ajax({
		url: "rest/userService/pretraziPodforume" ,
		type:"POST",
		data: dzejson,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			$.each(listaPodforuma,function(i,SubForum){
				renderSubForum(i,SubForum);
			})
			if (data!= null) {
				$.each(data,function(i,Index){
					
					
					$('#subForum'+Index).hide();
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

