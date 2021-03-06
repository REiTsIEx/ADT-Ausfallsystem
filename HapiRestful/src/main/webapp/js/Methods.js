/**
 * 
 */

function checkHTTPStatus() {

	request = $
			.ajax({
				type : "GET",
				url : "http://localhost:8080/Ausfallsystem/hapiservlet/Patient/",
				headers : {
					"Content-Type" : "application/json+fhir;charset=UTF-8"
				},
				statusCode : {
					200 : function() {
						console.log("Verbindung ist möglich");
					},
					500 : function() {
						alert("Error 500: Keine Verbindung zur Datenbank! \n Bitte an Administrator wenden.");
					},
					400 : function() {
						alert("Bitte Eingabe erneut wiederholen!")
					}

				}
			});

}


function addNewPatient() {

	var svn = document.getElementById('svn_value');
	var lastname = document.getElementById('lastname');
	var firstname = document.getElementById('firstname');
	if (svn.value != "") {
		url = "";
		var url = "http://localhost:8080";
		var method = "/Ausfallsystem/hapiservlet/Patient/" + svn.value;

		var gender = document.getElementById('gender');
		var birthday = document.getElementById('birthdate');

		var street = document.getElementById('street');
		var country = document.getElementById('country');
		var city = document.getElementById('city');
		var plz = document.getElementById('plz');

		var location = document.getElementById('location');
//		var phone = document.getElementById('phoneNumber');
//		var phonetype = document.getElementById('phoneType');

		var encounterType = document.getElementById('encounterItem');

		var insurance = document.getElementById('insurance');


		request = $
				.ajax({
					type : "POST",
					url : url + method,
					headers : {
						"Content-Type" : "application/json+fhir;charset=UTF-8"
					},
					data : JSON.stringify({
						resourceType : "Patient",
						identifier : {
							value : svn.value

						},
						name : {
							family : lastname.value,
							given : firstname.value
						},
//						telecom : {
//							system : 'phone',
//							value : phone.value,
//							use : phoneType.value
//						},
						gender : gender.value,
						birthDate : birthday.value,
						address : {
							use : 'home',
							line :  street.value,
							city : city.value,
							postalCode : plz.value,
							country : country.value
						},
						careProvider : {
							reference : insurance.value
						}
					}),
					statusCode : {
						201 : function() {
							if (svn.value != "") {
								alert("Patient mit der ID " + svn.value
										+ " wurde angelegt!");
							} else {
								alert("Patient " + firstname.value + " "
										+ lastname.value + " wurde angelegt");
							}
						},
						500 : function() {
							alert("Error 500: Keine Verbindung zur Datenbank! \n Bitte an Administrator wenden.");
						},
						400 : function() {
							alert("Bitte Eingabe erneut wiederholen!")
						}

					}
				});
	} else {
		alert("Zur Identifizierung muss mindestens SVN-Nummer oder Vor- und Nachname angebeben werden!");
	}
}

function updatePatient() {

	var svn = document.getElementById('svn_value');
	url = "";
	var url = "http://localhost:8080";
	var method = "/Ausfallsystem/hapiservlet/Patient/" + svn.value;

	var lastname = document.getElementById('lastname');
	var firstname = document.getElementById('firstname');
	var gender = document.getElementById('gender');
	var birthday = document.getElementById('birthdate');

	var street = document.getElementById('street');
	var country = document.getElementById('country');
	var city = document.getElementById('city');
	var plz = document.getElementById('plz');

	var location = document.getElementById('location');
//	var phone = document.getElementById('phoneNumber');
//	var phonetype = document.getElementById('phoneType');

	var insurance = document.getElementById('insurance');
	
	
	

	request = $
			.ajax({
				type : "PUT",
				url : url + method,
				headers : {
					"Content-Type" : "application/json+fhir;charset=UTF-8"
				},
				data : JSON.stringify({
					resourceType : "Patient",
					identifier : {
						value : svn.value

					},
					name : {
						family : lastname.value,
						given : firstname.value
					},
					//telecom: {
					//system : 'phone',
					//value : phone.value,
					//use : phoneType.value
					//},
					gender: gender.value,
					birthDate : birthday.value,
					address : {
						use : 'home',
						line : {
							0 : street.value,
						},
						city : city.value,
						postalCode : plz.value,
						country : country.value
					},
					careProvider : {
						reference : insurance.value
					}
				}),
				
				
				statusCode : {
					200 : function() {
						if (svn.value != "") {
							alert("Patient mit der ID " + svn.value
									+ " wurde überschrieben!");
						} else {
							alert("Patient " + firstname.value + " "
									+ lastname.value + " wurde angelegt");
						}
					},
					500 : function() {
						alert("Error 500: Keine Verbindung zur Datenbank! \n Bitte an Administrator wenden.");
					},
					400 : function() {
						alert("Bitte Eingabe erneut wiederholen!")
					}

				}
			});
	

}
function addEncounter(classValue, patientValue, locationValue) {

	var url = "http://localhost:8080";
	var method = "/Ausfallsystem/hapiservlet/Encounter/";
	request = $
			.ajax({
				type : "POST",
				url : url + method,
				headers : {
					"Content-Type" : "application/json+fhir;charset=UTF-8"
				},
				data : JSON.stringify({
					resourceType : "Encounter",

					value : classValue,
					patient : {
						reference : patientValue
					},
					location : locationValue
				}),
				statusCode : {
					200 : function() {
						if (svn.value != "") {
							alert("Patient mit der ID " + svn.value
									+ " wurde überschrieben!");
						} else {
							alert("Patient " + firstname.value + " "
									+ lastname.value + " wurde angelegt");
						}
					},
					500 : function() {
						alert("Error 500: Keine Verbindung zur Datenbank! \n Bitte an Administrator wenden.");
					},
					400 : function() {
						alert("Bitte Eingabe erneut wiederholen!")
					}

				}
			});
}
