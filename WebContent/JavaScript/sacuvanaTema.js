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
		url: "rest/userService/getSelectedSavedTheme",
		type:"GET",
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if(data != null) {
				$("#abstractComment").empty();
				$("#subForum").append(data.belongsToSubforum);
				
					renderTheme(data);
					$.each(data.comments,function(i,Comment){
						renderComment(i,Comment);
					})
				
			}
		}
		});
	
};

function renderTheme(Theme){
	var poz = Theme.likes;
	var neg = Theme.dislikes;
	var rez = poz - neg;
	
	$("#panelZaTemu").append("<div class=\"panel panel-success\" >" +
			"<div class=\"panel-heading\"><div class=\"panel-title pull-left\" >   " +
			"<a href=\"#\">" + Theme.name + " </a></div><div  class=\"panel-title pull-right\" >" +
			"Skor " + rez + " </div>  " +
			"<div class=\"clearfix\"></div></div>" +  "<div class=\"panel-body\" id=\"subForums8Opis\"> " + Theme.content + "</div>" +
			"<div class=\"panel-footer\"><ul class = \"list-inline\"><li>" +
			"<li  ><small> " + Theme.date + "</small></li>" + 
			"</ul> </div> </div> ");
	
}

function renderComment(i,Comment){
	
	if(Comment.logickiObrisan == true){
		$("#naslov").empty();
		$("#naslov").append("Komentari na temu : " + Comment.belongsToTheme);
		var poz = Comment.likes;
		var neg = Comment.dislikes;
		var rez = poz-neg;
		$("#abstractComment").append("<div  class=\"comment-box\" id=\"glavniKomentar" + i + "\"><div class=\"comment-head\">" +
				"<h4 class=\"comment-name by-author\"> " +Comment.author + "</h4> " +
				"<ul class = \"list-inline\"><li> Datum:  " +Comment.date + " </li>" +
				"<li class=\"pull-right\"> \ </li>" +
				"<li class=\"pull-right\">" +
				"<button disabled onclick=\"downvote( " + Comment.idKomentara+ " )\"><span class=\"glyphicon glyphicon-arrow-down fa-lg \"></span> </button></li>" +
				"<li class=\"pull-right\">" +
				"<button disabled onclick=\"upvote( " + Comment.idKomentara+ " )\"><span class=\"glyphicon glyphicon-arrow-up\"></span>  </button>" +
				"</li>" +
				"<li><p ><span class=\"glyphicon glyphicon-pencil\" ></span> Komentarisi </p></li>" +
						"<li><p><span class=\"glyphicon glyphicon-tags\" ></span> Izmeni </p></li>" +
						"<li><p><span class=\"glyphicon glyphicon-remove\" ></span> Obrisi </p></li>" +
						"<li><p><span class=\"glyphicon glyphicon-floppy-disk\" ></span> Snimi </p></li>" +
						"<li><p><span class=\"glyphicon glyphicon-exclamation-sign\" ></span> Zalba </p></li>" +
						"</ul></div><br><div class=\"comment-content\"> " +Comment.text + 
								"<hr style=\"height:3px;border:none;color:#333;background-color:#333;\">" +
								"</div></div>  " +
								"<ul id=\"podkomentari" + Comment.idKomentara + "\">  </ul>");
		
		$.each(Comment.childrenComments,function(i,ChildComment){
			
				renderChildrenComments(i,ChildComment);
		})
	}	else {
		$("#naslov").empty();
		$("#naslov").append("Komentari na temu : " + Comment.belongsToTheme);
		var poz = Comment.likes;
		var neg = Comment.dislikes;
		var rez = poz-neg;
		$("#abstractComment").append("<div  class=\"comment-box\" id=\"glavniKomentar" + i + "\"><div class=\"comment-head\">" +
				"<h4 class=\"comment-name by-author\"> " +Comment.author + "</h4> " +
				"<ul class = \"list-inline\"><li> Datum:  " +Comment.date + " </li>" +
				"<li> Izmenjen:  " + (Comment.edited ? 'Da' : 'Ne' ) + " </li>" +
				"<li class=\"pull-right\" id=\"skor"+Comment.idKomentara+"\">" + "Skor " + rez + " </li>" +
				"<li class=\"pull-right\">" +
				"<button disabled onclick=\"downvote( " + Comment.idKomentara+ " )\"><span class=\"glyphicon glyphicon-arrow-down fa-lg \"></span> </button></li>" +
				"<li class=\"pull-right\">" +
				"<button disabled onclick=\"upvote( " + Comment.idKomentara+ " )\"><span class=\"glyphicon glyphicon-arrow-up\"></span>  </button>" +
				"</li>" +
				"<li><p><span class=\"glyphicon glyphicon-pencil\" ></span> Komentarisi </p></li>" +
				"<li><p><span class=\"glyphicon glyphicon-tags\" ></span> Izmeni </p></li>" +
				"<li><p><span class=\"glyphicon glyphicon-remove\" ></span> Obrisi </p></li>" +
				"<li><p><span class=\"glyphicon glyphicon-floppy-disk\" ></span> Snimi </p></li>" +
				"<li><p><span class=\"glyphicon glyphicon-exclamation-sign\" ></span> Zalba </p></li>" +
				"</ul></div><br><div class=\"comment-content\"> " +Comment.text + 
								"<hr style=\"height:3px;border:none;color:#333;background-color:#333;\">" +
								"</div></div>  " +
								"<ul id=\"podkomentari" + Comment.idKomentara + "\">  </ul>");
		
		$.each(Comment.childrenComments,function(i,ChildComment){
			
				renderChildrenComments(i,ChildComment);
		})
	}
	
}

function renderChildrenComments(i,ChildComment){
	
	if(ChildComment.logickiObrisan == true){
		var poz = ChildComment.likes;
		var neg = ChildComment.dislikes;
		var rez = poz-neg;
		$("#podkomentari"+ChildComment.idKomentara.substring(0, ChildComment.idKomentara.length - 2)+"").append("<li> " +
				"<h4 >" +ChildComment.author + "</h4>" +
				"<ul class = \"list-inline\"><li> " +ChildComment.date + " </li" +
				"><li class=\"pull-right\" > / </li> </li><li class=\"pull-right\"><button disabled onclick=\"downvote(\'" + ChildComment.idKomentara + "\')\"><span class=\"glyphicon glyphicon-arrow-down fa-lg \"></span> </button></li>" +
				"<li class=\"pull-right\"><button disabled onclick=\"upvote(\'" + ChildComment.idKomentara + "\')\"><span class=\"glyphicon glyphicon-arrow-up\"></span>  </button></li>" +
				"<li><p><span class=\"glyphicon glyphicon-pencil\" ></span> Komentarisi </p></li>" +
				"<li><p><span class=\"glyphicon glyphicon-tags\" ></span> Izmeni </p></li>" +
				"<li><p><span class=\"glyphicon glyphicon-remove\" ></span> Obrisi </p></li>" +
				"<li><p><span class=\"glyphicon glyphicon-floppy-disk\" ></span> Snimi </p></li>" +
				"<li><p><span class=\"glyphicon glyphicon-exclamation-sign\" ></span> Zalba </p></li>" +
				" </ul><div class=\"comment-content\">" +ChildComment.text + "</div> " +
						"<hr style=\"height:3px;border:none;color:#333;background-color:#333;\">" +
						"<ul id=\"podkomentari" + ChildComment.idKomentara + "\"> </ul>" +
						"</li> ");
		
		if(ChildComment.childrenComments.length == 0){
			
			return;
		} else {
			$.each(ChildComment.childrenComments,function(i,ChildComment){
				
				
				renderChildrenComments(i,ChildComment);
			})
		}
	} else {
		var poz = ChildComment.likes;
		var neg = ChildComment.dislikes;
		var rez = poz-neg;
		$("#podkomentari"+ChildComment.idKomentara.substring(0, ChildComment.idKomentara.length - 2)+"").append("<li> " +
				"<h4 >" +ChildComment.author + "</h4>" +
				"<ul class = \"list-inline\"><li> " +ChildComment.date + " </li>" +
				"<li> Izmenjen:  " + (ChildComment.edited ? 'Da' : 'Ne' ) + " </li>" +
				"<li class=\"pull-right\" id=\"skor"+ ChildComment.idKomentara +"\">" + "Skor " + rez + " </li> </li><li class=\"pull-right\"><button disabled onclick=\"downvote(\'" + ChildComment.idKomentara + "\')\"><span class=\"glyphicon glyphicon-arrow-down fa-lg \"></span> </button></li>" +
				"<li class=\"pull-right\"><button disabled onclick=\"upvote(\'" + ChildComment.idKomentara + "\')\"><span class=\"glyphicon glyphicon-arrow-up\"></span>  </button></li>" +
				"<li><p><span class=\"glyphicon glyphicon-pencil\" ></span> Komentarisi </p></li>" +
				"<li><p><span class=\"glyphicon glyphicon-tags\" ></span> Izmeni </p></li>" +
				"<li><p><span class=\"glyphicon glyphicon-remove\" ></span> Obrisi </p></li>" +
				"<li><p><span class=\"glyphicon glyphicon-floppy-disk\" ></span> Snimi </p></li>" +
				"<li><p><span class=\"glyphicon glyphicon-exclamation-sign\" ></span> Zalba </p></li>" +
				" </ul><div class=\"comment-content\">" +ChildComment.text + "</div> " +
						"<hr style=\"height:3px;border:none;color:#333;background-color:#333;\">" +
						"<ul id=\"podkomentari" + ChildComment.idKomentara + "\"> </ul>" +
						"</li> ");
		
		if(ChildComment.childrenComments.length == 0){
			
			return;
		} else {
			$.each(ChildComment.childrenComments,function(i,ChildComment){
				
				
				renderChildrenComments(i,ChildComment);
			})
		}
	}
	
	
	
}



