
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
	
	
	$("#abstractComment").empty();
	
	
	$.ajax({
		url: "rest/userService/getSelectedTheme",
		type:"GET",
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if(data!=null)
				
				
				$.each(data.comments,function(i,Comment){
						renderComment(i,Comment);
				})
				
		}
		});
	
};

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
				"<button onclick=\"downvote( " + Comment.idKomentara+ " )\"><span class=\"glyphicon glyphicon-arrow-down fa-lg \"></span> </button></li>" +
				"<li class=\"pull-right\">" +
				"<button onclick=\"upvote( " + Comment.idKomentara+ " )\"><span class=\"glyphicon glyphicon-arrow-up\"></span>  </button>" +
				"</li>" +
				"<li><a href=\"#\" onclick=\"komentarisiKomentar(" + Comment.idKomentara + ")\"><span class=\"glyphicon glyphicon-pencil\" ></span> Komentarisi </a></li>" +
						"<li><a href=\"#\" onclick=\"izmeniKomentar(" +Comment.idKomentara + ")\"><span class=\"glyphicon glyphicon-tags\" ></span> Izmeni </a></li>" +
						"<li><a href=\"#\" onclick=\"logickiObrisiKomentar(" + i + ")\"><span class=\"glyphicon glyphicon-remove\" ></span> Obrisi </a></li>" +
						"<li><a href=\"#\" onclick=\"snimiKomentar(" + i + ")\"><span class=\"glyphicon glyphicon-floppy-disk\" ></span> Snimi </a></li>" +
						"<li><a href=\"#\" onclick=\"zalba(" + i + ")\"><span class=\"glyphicon glyphicon-exclamation-sign\" ></span> Zalba </a></li>" +
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
				"<li class=\"pull-right\" id=\"skor"+ ChildComment.idKomentara +"\">" + "Skor " + rez + " </li> </li><li class=\"pull-right\"><button onclick=\"downvote(\'" + ChildComment.idKomentara + "\')\"><span class=\"glyphicon glyphicon-arrow-down fa-lg \"></span> </button></li>" +
				"<li class=\"pull-right\"><button onclick=\"upvote(\'" + ChildComment.idKomentara + "\')\"><span class=\"glyphicon glyphicon-arrow-up\"></span>  </button></li>" +
				"<li><a href=\"#\" onclick=\"komentarisiKomentar(\'" + ChildComment.idKomentara + "\')\"><span class=\"glyphicon glyphicon-pencil\" ></span> Komentarisi </a></li>" +
				"<li><a href=\"#\" onclick=\"izmeniKomentar(\'" + ChildComment.idKomentara + "\')\"><span class=\"glyphicon glyphicon-tags\" ></span> Izmeni </a></li>" +
				"<li><a href=\"#\" onclick=\"logickiObrisiKomentar(\'" + ChildComment.idKomentara + "\')\"><span class=\"glyphicon glyphicon-remove\" ></span> Obrisi </a></li>" +
				"<li><a href=\"#\" onclick=\"snimiKomentar(\'" + ChildComment.idKomentara + "\')\"><span class=\"glyphicon glyphicon-floppy-disk\" ></span> Snimi </a></li>" +
				"<li><a href=\"#\" onclick=\"zalba(\'" + ChildComment.idKomentara + "\')\"><span class=\"glyphicon glyphicon-exclamation-sign\" ></span> Zalba </a></li>" +
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

function zalba(i){
	var data = JSON.stringify(i);
	$.ajax({
		url: "rest/userService/setSelectedComment",
		type:"POST",
		data: data,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				top.location.href="kreirajZalbu2.html";
				
				
			}else{
				alert("Uspesno setovanje");
				top.location.href="kreirajZalbu2.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}


function addComment(){ //dodaje komentar koji nema parenta
	top.location.href="kreirajKomentar.html";
}

function komentarisiKomentar(i){
	
	$.ajax({
		url: "rest/userService/saveCommentedCommentToDatabase/" + i,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				
				top.location.href="komentarisiKomentar.html";
				
			}else{
				top.location.href="komentarisiKomentar.html";
				
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
		url: "rest/userService/upvoteComment",
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
				alert("Vec ste dali pozitivan glas ovom komentaru ili ste prijavljeni kao gost");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	}); 
}

function downvote(i){
	var data = JSON.stringify(i);
	
	$.ajax({
		url: "rest/userService/downvoteComment",
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
				alert("Vec ste dali negativan glas ovom komentaru ili ste prijavljeni kao gost");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
} 

function izmeniKomentar(i){
	var data = JSON.stringify(i);
	$.ajax({
		url: "rest/userService/saveCommentToEditToDatabase",
		type:"POST",
		data: data,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				
				top.location.href="izmeniKomentar.html";
				
			}else{
				top.location.href="izmeniKomentar.html";
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function logickiObrisiKomentar(i){
	
	alert(i);
	$.ajax({
		url: "rest/userService/obrisiKomentar/" + i ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= undefined) {
				$("#subForums").empty();
				$("#abstractComment").empty();
				$.each(data,function(i,Comment){
						
						renderComment(i,Comment);	
				})
				
				
			}else{
				alert("Ne mozete obrisati komentar jer nemate ovlascenja. Samo korisnik koji je autor, moderator ili adminstrator moze da obrise komentar");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function snimiKomentar(i){
	$.ajax({
		url: "rest/userService/snimiKomentar/" + i ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data) {
				alert("Snimljen komentar: " + i);
			}else{
				alert("Vec ste snimili komentar: " + i + " ili ste prijavljeni kao gost");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}