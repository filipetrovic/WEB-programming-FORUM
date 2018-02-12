
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
					alert(data);
				}
				
			} else {
				alert(data);
				
				top.location.href ="index.html";
			}s
		},
		error: function(jqxhr,textStatus,errorThrown){
			
			alert(errorThrown);
		}
	});
}
function posaljiPoruku(){
	var $form = $("#posaljiPoruku");
	var data = getFormData($form);
	var s = JSON.stringify(data);
	alert(s);
	$.ajax({
		url: "rest/userService/sendMessage",
		type:"POST",
		data: s,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data == false) {
				alert("Poruka nije poslata iz nekog razloga");
				
			}else{
				alert("Poruka poslata");
				top.location.href="pocetnaStranica.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});

}