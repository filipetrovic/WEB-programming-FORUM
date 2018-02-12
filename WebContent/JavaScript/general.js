


function logout() {
	
	$.ajax({
		async: "false",
		url: "rest/userService/logout",
		type:"GET",
		dataType:"text",
		success : function(data) {
			if(data == "true") {
				
				window.location.replace("index.html");
			} else {
				
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			
			alert(errorThrown);
		}
	});
}

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function getAllUsers(){
	
	$.ajax({
		async: "false",
		url: "rest/userService/allUsers",
		type:"GET",
		dataType:"text",
		success : function(data) {
			if(data == "true") {
				//alert("OK");
				
			} else {
				//alert("NOT OK");
			}
		},
		error: function(jqxhr,textStatus,errorThrown){
			
			alert(errorThrown);
		}
	});
	
}