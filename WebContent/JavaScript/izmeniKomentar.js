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
	
	
	var $form = $("#kreirajKomentar");
	var data = getFormData($form);
	var s = JSON.stringify(data);
	
	$.ajax({
		url: "rest/userService/editComment",
		type:"POST",
		data: s,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data != false) {
				
				
				top.location.href="komentari.html";
					
				}
			else{
				alert("Ne mozete izmeniti komentar jer nemate ovlascenja. Da biste izmenili komentar" +
						" morate biti odgovorni moderator podforuma kojem on pripada ili autor komentara");
				top.location.href="komentari.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}


