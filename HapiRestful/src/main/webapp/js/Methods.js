/**
 * 
 */






//function searchPatientByName() {
//	var method ="";
//	var param= "";
//	method = "/Ausfallsystem/hapiservlet/Patient";
//
//	var svn = document.getElementById("svnSearch");
//	var lastname = document.getElementById('lastnameSearch');
//	if(lastname.value == "" && svn.value == "") {
//		alert("Bitte SVN oder Nachname eingeben!");
//		
//	}else{
//		if(svn.value == ""){
//
//	var firstname = document.getElementById('firstnameSearch');
//
//	
//	param = "?family="+lastname.value+"&firstname="+firstname.value;
//		}else{
//			param="/"+svn.value;
//		
//		
//		}
//		
//		
//	url = url+method+param;
//	
//    var xmlRequest = new XMLHttpRequest();
//    xmlRequest.open( "GET", url, true );
//    xmlRequest.withCredentials = false;
//    xmlRequest.send(null);
//    var xmlMessage = xmlRequest.responseXML;
//    if(xmlMessage != null) {
//    loadPatients(xmlMessage);
//    }
//    else{
//    	
//    	alert("Keine Patienten für diese Auswahlkriterien!")
//    }
   			
//}
//}

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
	var svn = document.getElementById('svn_value');
	var lastname = document.getElementById('lastname');
	var firstname =document.getElementById('firstname');
	if(svn.value != "" || (firstname.value != "" && lastname.value != "")){
	url = "";
	var url = "http://localhost:8080";
	var method = "/Ausfallsystem/hapiservlet/Patient/"+svn.value;
		
	
	
	
	var gender = document.getElementById('gender');
	var birthday = document.getElementById('birthdate');
	
	
	var street = document.getElementById('street');
	var country = document.getElementById('country');
	var city = document.getElementById('city');
	var plz = document.getElementById('plz');
	
	
	var location = document.getElementById('location');
 	var phone = document.getElementById('phoneNumber');
 	var phonetype = document.getElementById('phoneType');
	
 	var deceasedisChecked = document.getElementById("checkbocdeceasedtrue").checked;
	if(deceasedisChecked) {
	var deceasedBool = true;	
	}else{
		deceasedBool = false;
	}
	
	var insurance = document.getElementById('insurance');
		url = url+method;
		
		var xmlRequest = new XMLHttpRequest();
		
		xmlRequest.open("POST",url);
		xmlRequest.withCredentials = false;
		xmlRequest.setRequestHeader("Content-Type", "application/json+fhir;charset=UTF-8");
		xmlRequest.send(JSON.stringify({
			resourceType:"Patient", 
			identifier: {
				value: svn.value
			
				},
				name:{
					family: lastname.value,
					given: firstname.value
				},
				telecom: {
					system : 'phone',
					value : phone.value,
					use : phoneType.value
				},
				gender: gender.value,
				birthDate: birthday.value,
				deceasedBoolean : deceasedBool.value,
				address: {
					use: 'home',
					line:street.value,
					city: city.value,
					postalCode : plz.value,
					country: country.value
				},
				careProvider:{
					reference : insurance.value
				}
				}));
		if(svn.value != "") {
		alert("Patient mit der ID " + svn.value +" wurde angelegt!");
		}else{
			alert("Patient " + firstname.value + " " + lastname.value+ " wurde angelegt");
		}
	}else{
		alert("Zur Identifizierung muss mindestens SVN-Nummer oder Vor- und Nachname angebeben werden!");
	}
}

function updatePatient() {
	var svn = document.getElementById('svn_value');
	url = "";
	var url = "http://localhost:8080";
	var method = "/Ausfallsystem/hapiservlet/Patient/"+svn.value;
		
	
	
	var lastname = document.getElementById('lastname');
	var firstname =document.getElementById('firstname');
	var gender = document.getElementById('gender');
	var birthday = document.getElementById('birthdate');
	
	
	var street = document.getElementById('street');
	var country = document.getElementById('country');
	var city = document.getElementById('city');
	var plz = document.getElementById('plz');
	
	
	var location = document.getElementById('location');
 	var phone = document.getElementById('phoneNumber');
 	var phonetype = document.getElementById('phoneType');
	
// 	var deceasedisChecked = document.getElementById("checkbocdeceasedtrue").checked;
//	if(deceasedisChecked) {
//	var deceasedBool = true;	
//	}else{
//		deceasedBool = false;
//	}
	
	var insurance = document.getElementById('insurance');
		url = url+method;
		
		var xmlRequest = new XMLHttpRequest();
		
		xmlRequest.open("PUT",url);
		xmlRequest.withCredentials = false;
		xmlRequest.setRequestHeader("Content-Type", "application/json+fhir;charset=UTF-8");
		xmlRequest.send(JSON.stringify({
			resourceType:"Patient", 
			identifier: {
				value: svn.value
			
				},
				name:{
					family: lastname.value,
					given: firstname.value
				},
//				telecom: {
//					system : 'phone',
//					value : phone.value,
//					use : phoneType.value
//				},
//				gender: gender.value,
				birthDate: birthday.value,
				//deceasedBoolean : deceasedBool.value,
				address: {
					use: 'home',
					line:street.value,
					city: city.value,
					postalCode : plz.value,
					country: country.value
				},
				careProvider:{
					reference : insurance.value
				}
				}));
		alert("Patient mit der ID " + svn.value +" wurde überschrieben!")
	}




