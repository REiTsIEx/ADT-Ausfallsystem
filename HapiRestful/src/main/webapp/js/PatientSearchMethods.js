/**
 * 
 */
function getPatientbyName() {

	var url = "http://127.0.0.1:8080";
	var method = "/HapiRestful/hapiservlet/Patient";


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