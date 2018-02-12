function kreirajTemuPage(){
	top.location.href="kreirajTemu.html";
	
	/*$.ajax({
		url: "rest/userService/goToAddTheme",
		type:"POST",
		data: data,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data == false) {
				alert("User email is already in use!");
			}else{
				alert("Registration successeful");
				top.location.href="pocetnaStranica.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	}); */
}
var listaTema;
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
	

	var divForSubForums = $("#subForum").empty();
	$("#panelZaTemu").empty();
	$.ajax({
		url: "rest/userService/getSelectedSubForum",
		type:"GET",
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if(data!=null) {
				listaTema = data.themes;
				$("#subForum").append(data.name);
				$.each(data.themes,function(i,Theme){
					renderTheme(i,Theme);
					
				})
			}
		}
		});
	
};

function renderTheme(i,Theme){
	var poz = Theme.likes;
	var neg = Theme.dislikes;
	var rez = poz - neg;
	
	$("#panelZaTemu").append("<div class=\"panel panel-primary\" id=\"theme"+i+"\">" +
			"<div class=\"panel-heading\"><div class=\"panel-title pull-left\" >   " +
			"<a href=\"#\">" + Theme.name + " </a></div><div id=\"skor"+i+"\" class=\"panel-title pull-right\" >" +
			"Skor " + rez + " </div>  " +
			"<div class=\"clearfix\"></div></div>" +  "<div class=\"panel-body\" id=\"subForums8Opis\"> " + Theme.content + "</div>" +
			"<div class=\"panel-footer\"><ul class = \"list-inline\"><li>" +
			"<button onclick=\"upvote(" + i + ")\"><span class=\"glyphicon glyphicon-arrow-up\"></span> Upvote </button>" +
			"</li><li" +
			"><button onclick=\"downvote(" + i + ")\"><span class=\"glyphicon glyphicon-arrow-down fa-lg \"></span>Downvote </button>" +
			"</li><li><a href=\"#\" onclick=\"otvoriKomentareZaTemu(" + i + ")\"><span class=\"glyphicon glyphicon-pencil\" ></span> Komentari </a>" +
			"</li><li  ><small> " + Theme.date + "</small></li>" + 
			"</li><li class=\"pull-right\"><a href=\"#\" onclick=\"izmeniTemu(" + i + ")\"><span class=\"glyphicon glyphicon-tags\" ></span> Izmeni </a>" +
			"</li><li class=\"pull-right\"><a href=\"#\" onclick=\"obrisiTemu(" + i + ")\"><span class=\"glyphicon glyphicon-remove\" ></span> Obrisi </a>" +
			"</li><li class=\"pull-right\"><a href=\"#\" onclick=\"sacuvajTemu(" + i + ")\"><span class=\"glyphicon glyphicon-floppy-disk\" ></span> Sacuvaj </a>" +
			"</li><li class=\"pull-right\"><a href=\"#\" onclick=\"zalba(\'" + Theme.name + "\',\'" + Theme.belongsToSubforum + "\')\"><span class=\"glyphicon glyphicon-exclamation-sign\" ></span> Zalba </a> </li>" +
			
			
					"</ul> </div> </div> ");
	
}

function zalba(Theme,SubForum){
	
	$.ajax({
		url: "rest/userService/setSelectedSubForumAndTheme/" + Theme + "/" + SubForum,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				top.location.href="kreirajZalbu1.html";
				
				
			}else{
				alert("Uspesno setovanje");
				top.location.href="kreirajZalbu1.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function upvote(i){
	var data = JSON.stringify(i);
	$.ajax({
		url: "rest/userService/upvote",
		type:"POST",
		data: data,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= undefined) {
				//render skor vrati temu
				
				var poz = data.likes;
				var neg = data.dislikes;
				var rez = poz - neg;
				
				$('#skor' + i ).empty();
				$('#skor' + i ).append("Skor: " + rez);
			}else{
				alert("Ne mozete dati upvote vise od jednog puta! Ili ste prijavljeni kao gost");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function downvote(i){
	var data = JSON.stringify(i);
	var s = "skor";
	s = s.concat(i);
	$.ajax({
		url: "rest/userService/downvote",
		type:"POST",
		data: data,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= undefined) {
				var poz = data.likes;
				var neg = data.dislikes;
				var rez = poz - neg;
				
				$('#skor' + i ).empty();
				$('#skor' + i ).append("Skor: " + rez);
				
			}else{
				alert("Mozete udeliti samo jedan downvote Ili ste prijavljeni kao gost");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function otvoriKomentareZaTemu(i){
	var data = JSON.stringify(i);
	$.ajax({
		url: "rest/userService/goToComments",
		type:"POST",
		data: data,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				top.location.href="komentari.html";
				
			}else{
				alert("Registration successeful");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
	
}

function izmeniTemu(i){
	var data = JSON.stringify(i);
	$.ajax({
		url: "rest/userService/saveThemeEditTheme",
		type:"POST",
		data: data,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				
				top.location.href="izmeniTemu.html";
				
			}else{
				alert("Registration successeful");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function obrisiTemu(i){
	var data = JSON.stringify(i);
	
	$.ajax({
		url: "rest/userService/deleteTheme",
		type:"POST",
		data: data,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data != undefined) {
				$("#panelZaTemu").empty();
				listaTema = data.themes;
				$.each(data.themes,function(i,Theme){
					
					
					renderTheme(i,Theme);
					
				})
				
				
				
			}else{
				alert("Ne mozete uraditi ovo jer nemate ovlascenja");
				
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function sacuvajTemu(i){
	alert(i);
	$.ajax({
		url: "rest/userService/sacuvajTemu/" + i ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data) {
				alert("Tema sacuvana");
				
			}else{
				alert("Ne mozete dvaput sacuvati istu temu ili ste prijavljeni kao gost");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}


function pretrazi(){
	
	var naslovChecked = $('#pretragaPoNaslovu').is(":checked");
	var sadrzajChecked = $('#pretragaPoSadrzaju').is(":checked");
	var AutorChecked = $('#pretragaPoAutoru').is(":checked");
	var podforumChecked = $('#pretragaPoPodforumu').is(":checked");
	
	//alert(naslovChecked + "\t" + sadrzajChecked +"\t" + AutorChecked +"\t" + podforumChecked);
	
	var dzejson;
	
	var naslov;
	var sadrzaj;
	var Autor;
	var podforum;
	
	if (naslovChecked){
		naslov = $("#name").val();
	} else {
		naslov = "";
	}
	
	if (sadrzajChecked){
		sadrzaj = $("#content").val();
	} else {
		sadrzaj = "";
	}
	
	if (AutorChecked){
		Autor = $("#Author").val();
	} else {
		Autor = "";
	}
	
	if (podforumChecked){
		podforum = $("#belongsToSubforum").val();
	} else {
		podforum = "";
	}
	
	
	//alert(naslov + "\t" + sadrzaj +"\t" + Autor +"\t" + podforum);
	
	
	dzejson = JSON.stringify({
		"name": naslov,
		"content" : sadrzaj,
		"author" : Autor,
		"belongsToSubforum" : podforum 
	});
	alert(dzejson);
	
	$("#panelZaTemu").empty();
	$.ajax({
		url: "rest/userService/pretraziTemePodforuma" ,
		type:"POST",
		data: dzejson,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			$.each(listaTema,function(i,Theme){
				renderTheme(i,Theme);
			})
			if (data!= null) {
				$.each(data,function(i,Index){
					
					
					$('#theme'+Index).hide();
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

