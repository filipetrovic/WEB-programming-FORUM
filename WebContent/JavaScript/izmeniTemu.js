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

function kreirajTemu(){
	var $form = $("#kreirajTemu");
	var data = getFormData($form);
	var s = JSON.stringify(data);
	
	$.ajax({
		url: "rest/userService/editTheme",
		type:"POST",
		data: s,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data == false) {
				alert("Ne mozete promeniti temu! Tema sa imenom koje ste uneli vec postoji unutar izabranog podforuma ili ste prijavljeni kao gost");
				top.location.href="podforum.html";
			}else{
				alert("Dodata tema");
				top.location.href="podforum.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
	
}