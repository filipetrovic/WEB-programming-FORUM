window.onload = function(){
	
	$.ajax({
		async: "false",
		url: "rest/userService/logout",
		type:"GET",
		dataType:"text",
		success : function(data) {
			if(data == "true") {
				
				
			} else {
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			
			alert(errorThrown);
		}
	});
	
	
	
	$.ajax({
		url: "rest/userService/init",
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data == false) {
				//alert("Poslo je po zlu!");
			}else{
				
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function registerUser() {
	
	if (!validacijaRegistracije()){
		return false;
	} else {
		var $form = $("#registerForm");
		var data = getFormData($form);
		var s = JSON.stringify(data);
		
		
		$.ajax({
			url: "rest/userService/register",
			type:"POST",
			data: s,
			contentType:"application/json",
			dataType:"json",
			success : function(data){
				if (data == false) {
					alert("User email or username is already in use!");
				}else{
					alert("Registration successeful");
					top.location.href="pocetnaStranica.html";
				}
			},
			error: function(jqxhr,textStatus,errorThrown){
				alert(errorThrown);
			}
		});
	}
	
	
}

function logIn() {
	var $form = $("#login");
	var data = getFormData($form);
	var s = JSON.stringify(data);
	
	$.ajax({
		url: "rest/userService/login",
		type:"POST",
		data: s,
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data == false) {
				alert("Username doesnt exist, or password is incorrect!");
			}else{
				alert("Login successeful!");
				top.location.href="pocetnaStranica.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
}

function logInGuest() {
	
	$.ajax({
		url: "rest/userService/loginAsGuest",
		type:"GET",
		
		contentType:"application/json",
		dataType:"json",
		success : function(data){
			if (data == false) {
				alert("Username doesnt exist, or password is incorrect!");
			}else{
				alert("Ulogovali ste se kao gost");
				top.location.href="pocetnaStranica.html";
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			alert(errorThrown);
		}
	});
	
	
}

function validacijaRegistracije(){
	
	var email = $("#email").val();
	var name = $("#name").val();
	var surname = $("#surname").val();
	var phone = $("#phone").val();
	
	if (!validateEmail(email)){
		alert("Niste uneli dobar email");
		return false;
	}
	
	if (!validateName(name,surname)){
		alert("Niste uneli dobro ime ili prezime [ mora sadrzati samo karaktere ]");
		return false;
	}
	
	if (!validatePhone(phone)){
		alert("Niste uneli dobar broj telefona");
		return false;
	}
	
	return true;
}

function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function validateName(name,surname){
	var re = /^[a-zA-Z]*$/;
    return re.test(name) && re.test(surname);
}

function validatePhone(phone){
	var re = /^\d+$/;
	return re.test(phone);
}
