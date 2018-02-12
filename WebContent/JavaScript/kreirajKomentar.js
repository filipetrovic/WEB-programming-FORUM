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

function kreirajKomentar(){
	
	
	
	$("#naslov").empty();
	var $form = $("#kreirajKomentar");
	var data = getFormData($form);
	var s = JSON.stringify(data);
	
	$.ajax({
		url: "rest/userService/addComment",
		type:"POST",
		data: s,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data != false) {
				top.location.href="komentari.html";
					
				}
			else{
				alert("Ne mozete komentarisati jer ste prijavljeni kao gost");
				top.location.href="komentari.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}


