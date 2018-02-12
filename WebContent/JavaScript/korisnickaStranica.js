
window.onload = function(){
	
	var flag = true;
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
					return false;
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
	
	
	
	$("#mestoZaPoruke").empty();
	$("#zapraceniPodforumi").empty();
	$("#sacuvaneTeme").empty();
	$("#glasaneTeme").empty();
	$("#sacuvaniKomentari").empty();
	$("#glasaniKomentari").empty();
	$("#mestoZaZalbe").empty();
	
	$.ajax({
		url: "rest/userService/getCurrentUser" ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				
				renderUser(data);
				
				
			}else{
				
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
	
	$.ajax({
		url: "rest/userService/getUserMessages" ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				$.each(data,function(i,Message){
					renderMessage(i,Message);
					
				})
				
				
			}else{
				
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
	
	$.ajax({
		url: "rest/userService/getFollowedSubForums" ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				$.each(data,function(i,SubForum){
					renderSubForum(i,SubForum);
					
				})
				
				
			}else{
				
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
	
	
	$.ajax({
		url: "rest/userService/getSavedThemes" ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				$.each(data,function(i,Theme){
					renderTheme(i,Theme);
					
				})
				
				
			}else{
				
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
	
	$.ajax({
		url: "rest/userService/getVotedThemes" ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				$.each(data,function(i,Theme){
					renderVotedTheme(i,Theme);
					
				})
				
				
			}else{
				
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
	
	$.ajax({
		url: "rest/userService/getUserComments" ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				$.each(data,function(i,Comment){
					renderComment(i,Comment);
					
				})
				
				
			}else{
				
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
	
	$.ajax({
		url: "rest/userService/getVotedComments" ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				$.each(data,function(i,Comment){
					renderVotedComment(i,Comment);
					
				})
				
				
			}else{
				
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
	
	$.ajax({
		url: "rest/userService/getZalbe" ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data!= null) {
				$.each(data,function(i,Zalba){
					renderZalba(i,Zalba);
					
				})
				
				
			}else{
				
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});

}

function renderZalba(i,Zalba){
	
	if(Zalba.entitet == "SUBFORUM" ){
		
		$("#mestoZaZalbe").append("<div class=\"panel panel-default\" id=\"zalba" +i+ "\"><div class=\"panel-body\"><header class=\"text-left\"><div class=\"comment-user\">" +
				"<i class=\"fa fa-user\">" +
				"</i> <label> " + Zalba.author + "  </label> <label class=\"pull-right\"> Podforum : " + Zalba.subForumZaZalbu.name + " </label> <br> " +
						"<button   class=\"btn btn-success btn-sm pull-right\" onclick=\" resiZalbuBrisanjem( " + i + ")  \"> " +
						"<span class=\"glyphicon glyphicon-remove\" ></span> Obrisi entitet </button>" +
						"<button  class=\"btn btn-success btn-sm pull-right\" onclick=\" resiZalbuUpozorenjem( " + i + ")  \"> " +
						"<span class=\"glyphicon glyphicon-warning-sign\" ></span> Upozori autora entiteta </button>" + 
						"<button  class=\"btn btn-success btn-sm pull-right\" onclick=\" resiZalbuOdbijanjemZalbe( " + i + ")  \"> " +
						"<span class=\"glyphicon glyphicon-ban-circle\" ></span> OdbijZalbu </button>" + 
						
						" </div></header><div class=\"comment-post\">" + 
						"<p> " + Zalba.content + "</p>" +
						"</div></div></div><hr>");
	} else if(Zalba.entitet == "THEME"){
		
		$("#mestoZaZalbe").append("<div class=\"panel panel-default\" id=\"zalba" +i+ "\"><div class=\"panel-body\"><header class=\"text-left\"><div class=\"comment-user\">" +
				"<i class=\"fa fa-user\">" +
				"</i> <label> " + Zalba.author + "  </label> <label class=\"pull-right\"> Tema : " + Zalba.temaZaZalbu.name + " </label> <br> " +
						"<button   class=\"btn btn-success btn-sm pull-right\" onclick=\" resiZalbuBrisanjemTeme( " + i + ")  \"> " +
						"<span class=\"glyphicon glyphicon-remove\" ></span> Obrisi entitet </button>" +
						"<button  class=\"btn btn-success btn-sm pull-right\" onclick=\" resiZalbuUpozorenjemTeme( " + i + ")  \"> " +
						"<span class=\"glyphicon glyphicon-warning-sign\" ></span> Upozori autora entiteta </button>" + 
						"<button  class=\"btn btn-success btn-sm pull-right\" onclick=\" resiZalbuOdbijanjemZalbeTeme( " + i + ")  \"> " +
						"<span class=\"glyphicon glyphicon-ban-circle\" ></span> OdbijZalbu </button>" + 
						
						" </div></header><div class=\"comment-post\">" + 
						"<p> " + Zalba.content + "</p>" +
						"</div></div></div><hr>");
	} else 
		{
		$("#mestoZaZalbe").append("<div class=\"panel panel-default\" id=\"zalba" +i+ "\"><div class=\"panel-body\"><header class=\"text-left\"><div class=\"comment-user\">" +
				"<i class=\"fa fa-user\">" +
				"</i> <label> " + Zalba.author + "  </label> <label class=\"pull-right\"> Komentar : " + Zalba.komentarZaZalbu + " </label> <br> " +
						"<button   class=\"btn btn-success btn-sm pull-right\" onclick=\" resiZalbuBrisanjemKomentara( " + i + ")  \"> " +
						"<span class=\"glyphicon glyphicon-remove\" ></span> Obrisi entitet </button>" +
						"<button  class=\"btn btn-success btn-sm pull-right\" onclick=\" resiZalbuUpozorenjemKomentara( " + i + ")  \"> " +
						"<span class=\"glyphicon glyphicon-warning-sign\" ></span> Upozori autora entiteta </button>" + 
						"<button  class=\"btn btn-success btn-sm pull-right\" onclick=\" resiZalbuOdbijanjemZalbeKomentara( " + i + ")  \"> " +
						"<span class=\"glyphicon glyphicon-ban-circle\" ></span> OdbijZalbu </button>" + 
						
						" </div></header><div class=\"comment-post\">" + 
						"<p> " + Zalba.content + "</p>" +
						"</div></div></div><hr>");
		
		}
		
	
	
}
		function resiZalbuBrisanjem(i){
			$.ajax({
				url: "rest/userService/resiZalbuBrisanjemPodforuma/" + i ,
				type:"GET",
				
				contentType:"application/json",
				dataType:"json",
				success : function(data){
					if (data) {
						alert("Zalba obrisana i autori zalbe i entiteta obavesteni");
						top.location.href="korisnickaStranica.html";
						
						
					}else{
						alert("Entitet je vec obrisan");
						
					}
				},
				error: function(jqxhr,textStatus,errorThrown){
					alert(errorThrown);
				}
			});
		}
		function resiZalbuUpozorenjem(i){
			$.ajax({
				url: "rest/userService/resiZalbuUpozorenjemPodforuma/" + i ,
				type:"GET",
				
				contentType:"application/json",
				dataType:"json",
				success : function(data){
					if (data) {
						alert("Autori zalbe i entiteta obavesteni");
						top.location.href="korisnickaStranica.html";
						
						
					}else{
						alert("Poslo je po zlu");
						
					}
				},
				error: function(jqxhr,textStatus,errorThrown){
					alert(errorThrown);
				}
			});
		}
		function resiZalbuOdbijanjemZalbe(i){
			$.ajax({
				url: "rest/userService/resiZalbuUpozorenjemZalbePodforuma/" + i ,
				type:"GET",
				
				contentType:"application/json",
				dataType:"json",
				success : function(data){
					if (data) {
						alert("Autor zalbe i  obavesten");
						top.location.href="korisnickaStranica.html";
						
						
					}else{
						alert("Poslo je po zlu");
						
					}
				},
				error: function(jqxhr,textStatus,errorThrown){
					alert(errorThrown);
				}
			});
		}
		//=====================================================TEME ZALBe===========================================
		
				function resiZalbuBrisanjemTeme(i){
					$.ajax({
						url: "rest/userService/resiZalbuBrisanjemTeme/" + i ,
						type:"GET",
						
						contentType:"application/json",
						dataType:"json",
						success : function(data){
							if (data) {
								alert("Entitet obrisan i autori zalbe i entiteta obavesteni");
								top.location.href="korisnickaStranica.html";
								
								
							}else{
								alert("Entitet je vec obrisan");
								
							}
						},
						error: function(jqxhr,textStatus,errorThrown){
							alert(errorThrown);
						}
					});
				}
				function resiZalbuUpozorenjemTeme(i){
					$.ajax({
						url: "rest/userService/resiZalbuUpozorenjemTeme/" + i ,
						type:"GET",
						
						contentType:"application/json",
						dataType:"json",
						success : function(data){
							if (data) {
								alert("Autori zalbe i entiteta obavesteni");
								top.location.href="korisnickaStranica.html";
								
								
							}else{
								alert("Poslo je po zlu");
								
							}
						},
						error: function(jqxhr,textStatus,errorThrown){
							alert(errorThrown);
						}
					});
				}
				function resiZalbuOdbijanjemZalbeTeme(i){
					alert("ODVIJ");
					$.ajax({
						url: "rest/userService/resiZalbuUpozorenjemZalbeTeme/" + i ,
						type:"GET",
						
						contentType:"application/json",
						dataType:"json",
						success : function(data){
							if (data) {
								alert("Autor zalbe  obavesten");
								top.location.href="korisnickaStranica.html";
								
								
							}else{
								alert("Poslo je po zlu");
								
							}
						},
						error: function(jqxhr,textStatus,errorThrown){
							alert(errorThrown);
						}
					});
				}
				
							//====================================================KOMENTARI ZALBe===========================================
							
							function resiZalbuBrisanjemKomentara(i){
								alert(i);
								$.ajax({
									url: "rest/userService/resiZalbuBrisanjemKomentara/" + i ,
									type:"GET",
									
									contentType:"application/json",
									dataType:"json",
									success : function(data){
										if (data) {
											alert("Entitet obrisan i autori zalbe i entiteta obavesteni");
											top.location.href="korisnickaStranica.html";
											
											
										}else{
											alert("Entitet je vec obrisan");
											
										}
									},
									error: function(jqxhr,textStatus,errorThrown){
										alert(errorThrown);
									}
								});
							}
							function resiZalbuUpozorenjemKomentara(i){
								$.ajax({
									url: "rest/userService/resiZalbuUpozorenjemKomentara/" + i ,
									type:"GET",
									
									contentType:"application/json",
									dataType:"json",
									success : function(data){
										if (data) {
											alert("Autori zalbe i entiteta obavesteni");
											top.location.href="korisnickaStranica.html";
											
											
										}else{
											alert("Poslo je po zlu");
											
										}
									},
									error: function(jqxhr,textStatus,errorThrown){
										alert(errorThrown);
									}
								});
							}
							function resiZalbuOdbijanjemZalbeKomentara (i){
								
								$.ajax({
									url: "rest/userService/resiZalbuOdbijanjemZalbeKomentara/" + i ,
									type:"GET",
									
									contentType:"application/json",
									dataType:"json",
									success : function(data){
										if (data) {
											alert("Autor zalbe  obavesten");
											top.location.href="korisnickaStranica.html";
											
											
										}else{
											alert("Poslo je po zlu");
											
										}
									},
									error: function(jqxhr,textStatus,errorThrown){
										alert(errorThrown);
									}
								});
							}

function renderVotedComment(i,Comment){
	$("#glasaniKomentari").append("<tr >" +
			"<td > " + Comment.text + "</td>" +
			"<td>" + Comment.belongsToTheme + "</td>" +
			"<td>" + (Comment.vecGlasao ? '+' : '-') + " </td>" +
			"<td>" + (Comment.logickiObrisan ? 'Da' : 'Ne') + "</td>" + 
			"<td>" + Comment.author + "</td></tr>");
}

function renderComment(i,Comment){
	$("#sacuvaniKomentari").append("<tr >" +
			"<td > " + Comment.text + "</td>" +
			"<td>" + Comment.belongsToTheme + "</td>" +
			
			"<td>" + (Comment.logickiObrisan ? 'Da' : 'Ne') + "</td>" + 
			"<td>" + Comment.author + "</td>" +
			"<td> <button class=\" btn btn-md btn-success\" onclick=\" prikaziSacuvaniKomentar(" + i +") \"> Prikazi </button> </td>" +
			"</tr>");
			
}

			function prikaziSacuvaniKomentar(i){
				alert(i);
				$.ajax({
					url: "rest/userService/setSelectedSavedComment/" + i ,
					type:"GET",
					
					contentType:"application/json",
					dataType:"json",
					success : function(data){
						if (data!= null) {
							top.location.href = "sacuvaniKomentar.html"
						}else{
							alert("nestoLose");
							
						}
					},
					error: function(jqxhr,textStatus,errorThrown){
						alert(errorThrown);
					}
				});
			}

function renderVotedTheme(i,Theme){
	$("#glasaneTeme").append("<tr >" +
			"<td > " + Theme.name + "</td>" +
			"<td>" + Theme.content + "</td>" +
			"<td>" + Theme.belongsToSubforum + "</td>" +
			"<td>" + (Theme.vecGlasano ? '+' : '-') + "</td>" + 
			"<td>" + Theme.author + "</td></tr>");
}

function renderTheme(i,Theme){
	$("#sacuvaneTeme").append("<tr >" +
			"<td > " + Theme.name + "</td>" +
			"<td>" + Theme.content + "</td>" +
			"<td>" + Theme.belongsToSubforum + "</td>" +
			"<td>" + Theme.author + "</td>" +
			"<td> <button class=\" btn btn-md btn-success\" onclick=\" prikaziSacuvanuTemu(" + i +") \"> Prikazi </button> </td></tr>");
}



			function prikaziSacuvanuTemu(i){
				
				$.ajax({
					url: "rest/userService/setSelectedSavedTheme/" + i ,
					type:"GET",
					
					contentType:"application/json",
					dataType:"json",
					success : function(data){
						if (data!= null) {
							top.location.href = "sacuvanaTema.html"
						}else{
							alert("nestoLose");
							
						}
					},
					error: function(jqxhr,textStatus,errorThrown){
						alert(errorThrown);
					}
				});
			}

			
			
			
function renderSubForum(i,SubForum){
	$("#zapraceniPodforumi").append("<tr >" +
			"<td > " + SubForum.name + "</td>" +
			"<td>" + SubForum.mainModerator + "</td>" +
			"<td> <button class=\" btn btn-md btn-success\" onclick=\" prikaziPodforum(\'" + SubForum.name +"\') \"> Prikazi </button> </td>" +
			"</tr>");
		
}

	function prikaziPodforum(name){
		var s = JSON.stringify(name);
		$.ajax({
			url: "rest/userService/setSelectedSubForum" ,
			type:"POST",
			data : s,
			contentType:"application/json",
			dataType:"json",
			success : function(data){
				if (data!= null) {
					top.location.href = "podforum.html"
				}else{
					alert("nestoLose");
					
				}
			},
			error: function(jqxhr,textStatus,errorThrown){
				alert(errorThrown);
			}
		});
	}
		
	

function renderUser(user){
	
	$("#korisnikovoIme").empty();
	$("#korisnikovoIme").append("Korisnik : " + user.username);
	
	$("#username").empty();
	$("#username").append(  user.username);
	
	$("#password").empty();
	$("#password").append(  user.password);
	
	$("#name").empty();
	$("#name").append( user.name);
	
	$("#surname").empty();
	$("#surname").append(  user.surname);
	
	$("#role").empty();
	$("#role").append(  user.role);

	$("#phone").empty();
	$("#phone").append(  user.phone);
	
	$("#email").empty();
	$("#email").append(  user.email);
	
	$("#date").empty();
	$("#date").append(  user.date);
	
}

function renderMessage(i,Message){
	if (Message.read){
		$("#mestoZaPoruke").append("<div class=\"panel panel-default\"><div class=\"panel-body\"><header class=\"text-left\"><div class=\"comment-user\">" +
				"<i class=\"fa fa-user\">" +
				"</i> <label> " + Message.sender + "  </label> " +
						" </div></header><div class=\"comment-post\">" +
						"<p> " + Message.content + "</p>" +
						"</div></div></div><hr>");
	} else {
		$("#mestoZaPoruke").append("<div class=\"panel panel-default\"><div class=\"panel-body\"><header class=\"text-left\"><div class=\"comment-user\">" +
				"<i class=\"fa fa-user\">" +
				"</i> <label> " + Message.sender + "  </label> " +
						"<button id=\"read" + i + "\" class=\"btn btn-danger btn-lg pull-right\" onclick=\" readMessage( " + i + ")  \"> <span class=\"glyphicon glyphicon-envelope\" ></span> Mark as read </button>" +
						" </div></header><div class=\"comment-post\">" +
						"<p> " + Message.content + "</p>" +
						"</div></div></div><hr>");
	}
	
}

function readMessage(i){
	$.ajax({
		url: "rest/userService/readMessage/" + i ,
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data) {
				$('#read' +i).remove();
				
			}else{
				alert("Doslo do greske!");
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}