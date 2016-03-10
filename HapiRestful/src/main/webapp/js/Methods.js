/**
 * 
 */



var url = "http://localhost:8081";


function searchPatientByName() {

	var param= "";
	var method = "/HapiRestful/hapiservlet/Patient";

	var svn = document.getElementById("svnSearch");
	var lastname = document.getElementById('lastnameSearch');
	if(lastname.value == "" && svn.value == "") {
		alert("Bitte SVN oder Nachname eingeben!");
		
	}else{

	var firstname = document.getElementById('firstnameSearch');

	
	param = "?family="+lastname.value+"&firstname="+firstname.value;

		
	url = url+method+param;
	
    var xmlRequest = new XMLHttpRequest();
    xmlRequest.open( "GET", url, true );
    xmlRequest.withCredentials = false;
    xmlRequest.send(null);
   console.log(xmlRequest.responseXML);
   

			
}
}



function loadPatients() {
	
	var list = document.getElementById('demo');
	
	var entry = document.createElement('li');
	
	entry.appendChild(document.createTextNode("Test" + Math.random()));
	list.appendChild(entry);

}


function addNewPatient() {
	
	var method = "/HapiRestful/hapiservlet/"
		
	var svnValue = document.getElementById('svn_value');
	
	var lastnameValue = document.getElementById('lastnameAdd');
	
	var firstnameValue =document.getElementById('firstnameAdd');
	
	var genderValue = document.getElementById('genderAdd');
	
	var locationValue = document.getElementById('locationAdd');
	
	if(lastnameValue.value == "" || firstnameValue.value == "" || svnValue.value == ""){
		alert("Bitte alle Pflichteingaben abschließen!")
	}else{
		url = url+method;
		
		var xmlRequest = new XMLHttpRequest();
		
		xmlRequest.open("POST",url);
		xmlRequest.withCredentials = true;
		xmlRequest.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
		xmlRequest.send(JSON.stringify({value: svnValue, family: lastnameValue, given:firstnameValue, gender: genderValue, location : locationValue}));
		

	}
}



