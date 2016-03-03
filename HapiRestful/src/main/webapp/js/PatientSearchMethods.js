/**
 * 
 */
function getPatientbyName() {
	
	var url = "http://127.0.0.1:8080";
	var method = "/Patient/";

	var lastname = document.getElementById('lastname');
	if(lastname.value == "") {
		alert("yolo");
		
	}else{
	
	var firstname = document.getElementById('firstname');

	
	var param = "?family="+lastname.value+"&firstname="+firstname.value;
	
	url = url+method+param;
	
    var xmlRequest = new XMLHttpRequest();
    xmlRequest.open( "GET", url, false ); // false for synchronous request
    xmlRequest.send( null );
    return xmlRequest.responseText;
	

  console.log(url);
	}
}

function getValueToList() {
	
	
}
