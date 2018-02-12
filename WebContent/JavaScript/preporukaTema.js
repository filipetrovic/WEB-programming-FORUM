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
	
	
	$("#panelZaTemu").empty();
	
	$.ajax({
		url: "rest/userService/getPreporuceneTeme",
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data != null) {
				$.each(data,function(i,Theme){
					render(Theme);
				})
				
			}else{
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function render(Theme){
	var poz = Theme.likes;
	var neg = Theme.dislikes;
	var rez = poz - neg;
	
	$("#panelZaTemu").append("<div class=\"panel panel-success\" >" +
			"<div class=\"panel-heading\"><div class=\"panel-title pull-left\" >   " +
			"<label>" + Theme.name + " [ " + Theme.belongsToSubforum + "] </label>  </div><div  class=\"panel-title pull-right\" >" +
			"Skor " + rez + " </div>  " +
			"<div class=\"clearfix\"></div></div>" +  "<div class=\"panel-body\" id=\"subForums8Opis\"> " + Theme.content + "</div>" +
			"<div class=\"panel-footer\"><ul class = \"list-inline\">" +
			
			"<li><a href=\"#\" onclick=\"idiNaTemu(\'" + Theme.name + "\',\'" + Theme.belongsToSubforum + "\')\"><span class=\"glyphicon glyphicon-share-alt\" ></span> Idi na temu na forumu </a>" +
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
				top.location.href = "podforum.html";
			}
		},		
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	}); 
}