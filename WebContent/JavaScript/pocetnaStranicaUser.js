
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
	
	$("#panelZaTemu").empty();
	$.ajax({
		url: "rest/userService/getThemesFromFollowedSubForums",
		type:"GET",
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if(data!=null){
				
				$.each(data,function(i,Theme){
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
	
	
	
	$("#panelZaTemu").append("<div class=\"panel panel-danger\" >" +
			"<div class=\"panel-heading\"><div class=\"panel-title pull-left\" >   " +
			"<label>" + Theme.name + " [ " + Theme.belongsToSubforum + "] </label>  </div><div  class=\"panel-title pull-right\" >" +
			"Skor " + rez + " </div>  " +
			"<div class=\"clearfix\"></div></div>" +  "<div class=\"panel-body\" id=\"subForums8Opis\"> " + Theme.content + "</div>" +
			"<div class=\"panel-footer\"><ul class = \"list-inline\">" +
			
			"<li><button class=\"btn btn-md btn-danger\" onclick=\"idiNaTemu(\'" + Theme.name + "\',\'" + Theme.belongsToSubforum + "\')\"><span class=\"glyphicon glyphicon-share-alt\" ></span> Idi na temu na forumu </button>" +
			"</li><li  ><small> " + Theme.date + "</small></li>" + 
			"</ul> </div> </div> ");
	
}

function idiNaTemu(Theme,SubForum){
	$.ajax({ //prvi ajax poziv za setovanje
		url: "rest/userService/setSelectedSubForumAndTheme/" + Theme + "/" + SubForum,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data != -1) {
				alert("Uspesno setovanje")
				top.location.href = "podforum.html"

			}else{
				alert("Neuspesno setovanje");
				
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
	
	
	$("#panelZaTemu").empty();
	$.ajax({
		url: "rest/userService/pretraziTemePodforumaKojeKorisnikPrati" ,
		type:"POST",
		data: dzejson,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			
			if (listaTema == undefined){
				$.each(listaPodforumaKojeKorisnikPrati,function(i,SubForum){
					$.each(SubForum.themes,function(i,Theme){
						renderTheme(i,Theme);
					})
		
				})
			} else {
				$.each(listaTema,function(i,Theme){
					renderTheme(i,Theme);
				})
			}
			
			
			
			if (data!= null) {
				$.each(data,function(i,TemaUser){
					
					
					$('#theme'+TemaUser.theme+""+TemaUser.subForum).hide();
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


