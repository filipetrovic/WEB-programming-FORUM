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

function kreirajZalbu(){
	var $form = $("#kreirajZalbu");
	var data = getFormData($form);
	var s = JSON.stringify(data);
	
	
	$.ajax({
		url: "rest/userService/kreirajZalbuPodforum",
		type:"POST",
		data: s,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data == false) {
				alert("Poslo po zlu ili ste prijavljeni kao gost");
				top.location.href="podforum.html";
			}else{
				alert("Zalba prilozena");
				top.location.href="pocetnaStranica.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}
