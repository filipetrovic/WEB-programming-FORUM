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
	
	if (!validation()){
		alert("Morate uneti sva polja!");
		return false;
	} else {
		var $form = $("#kreirajTemu");
		var data = getFormData($form);
		var s = JSON.stringify(data);
		
		$.ajax({
			url: "rest/userService/addTheme",
			type:"POST",
			data: s,
			contentType:"application/json",
			dataType:"json",
			success : function(data){
				if (data == false) {
					alert("Vec postoji tema sa takvim imenom unutar izabranog podforuma ili ste prijavljeni kao gost");
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
	
	
}


function validation(){
	var name;
	var content;
	
	var name = $("#name").val();
	var content = $("#content").val();
	
	if (name === ""){
		return false;
	}
	
	if (content === ""){
		return false;
	}
	
	return true;
}