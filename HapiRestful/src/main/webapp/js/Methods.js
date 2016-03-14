/**
 * 
 */



var url = "http://localhost:8080";


function searchPatientByName() {

	var param= "";
	var method = "/Ausfallsystem/hapiservlet/Patient";

	var svn = document.getElementById("svnSearch");
	var lastname = document.getElementById('lastnameSearch');
	if(lastname.value == "" && svn.value == "") {
		alert("Bitte SVN oder Nachname eingeben!");
		
	}else{
		if(svn.value == ""){

	var firstname = document.getElementById('firstnameSearch');

	
	param = "?family="+lastname.value+"&firstname="+firstname.value;
		}else{
			param="/"+svn.value;
		
		
		}
		
		
	url = url+method+param;
	
    var xmlRequest = new XMLHttpRequest();
    xmlRequest.open( "GET", url, true );
    xmlRequest.withCredentials = false;
    xmlRequest.send(null);
    var xmlMessage = xmlRequest.responseXML;
    if(xmlMessage != null) {
    loadPatients(xmlMessage);
    }
//    else{
//    	
//    	alert("Keine Patienten für diese Auswahlkriterien!")
//    }
   			
}
}

////function loadPatients(xmlMessage) {
//	
//	var list = document.getElementById('demo');
//	
//	var entry = document.createElement('li');
//	
//	
//	var multipleMatches = xmlMessage.getElementsByTagName('Bundle')
//	if(multipleMatches =! null) {
//		length = xmlMessage.getElementsByTagName('total').getAttribute('value');
//		for(i = 0; i < length; i++) {
//			var nameValues = xmlD
//			var svn = xmlMessage.getElementsByTagName('value')[i].getAttribute('value');
//			var lastname = xmlMessage.getElementsByTagName('family')[i].getAttribute('value');
//			var firstname = xmlMessage.getElementsByTagName('given')[i].getAttribute('value');
//		}
//		
//	}
//	
//	
//	
//	entry.appendChild(document.createTextNode("Test" + Math.random()));
//	list.appendChild(entry);
//	
//	
//
//}


function addNewPatient() {
	
	var method = "/Ausfallsystem/hapiservlet/Patient"
		
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
		xmlRequest.withCredentials = false;
		xmlRequest.setRequestHeader("Content-Type", "application/json+fhir;charset=UTF-8");
		xmlRequest.send(JSON.stringify({resourceType:"Patient", id : svnValue.value, name:{ family: lastnameValue.value, given:firstnameValue.value }, gender: genderValue.value, location : locationValue}));
		

	}
}



