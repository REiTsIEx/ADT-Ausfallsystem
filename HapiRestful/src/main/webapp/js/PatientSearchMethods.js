/**
 * 
 */
function getPatientbyName() {
	
	var url = "http://10.0.2.18:8080";
	var method = "/HapiRestful/Patient";

	var lastname = document.getElementById('lastname');
	if(lastname.value == "") {
		alert("Bitte Nachname eingeben!");
		
	}else{
	
	var firstname = document.getElementById('firstname');

	
	var param = "?family="+lastname.value+"&firstname="+firstname.value;
	
	url = url+method+param;
	
    var xmlRequest = new XMLHttpRequest();
    xmlRequest.open( "GET", url, true );
    xmlRequest.send( null );
    return xmlRequest.responseText;
	

  console.log(url);
	}
}

function getValueToList() {
	
	
}
