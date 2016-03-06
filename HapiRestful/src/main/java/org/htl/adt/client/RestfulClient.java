package org.htl.adt.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

public class RestfulClient {
	FhirContext context = FhirContext.forDstu2();
	String serverURL = "http://localhost:8080/HapiRestful/hapiservlet/";
	IGenericClient client = context.newRestfulGenericClient(serverURL);
	
	public RestfulClient() {

	}
	
	public void searchPatient(Patient toSearchPatient){
		Bundle results = client
				.search()
				.forResource(Patient.class)
				.where(Patient.FAMILY.matches().value(toSearchPatient.getNameFirstRep().getFamilyAsSingleString()))
				.returnBundle(Bundle.class)
				.execute();	
	}
	
	public void createPatient(Patient toCreatePatient){
		MethodOutcome createStatment = client
				.create()
				.resource(toCreatePatient)
				.prettyPrint()
				.encodedJson()
				.execute();			
	}
	
	public void updatePatient(Patient toUpdatePatient){
		MethodOutcome updateStatement = client
				.update()
				.resource(toUpdatePatient)
				.execute();
	}
	
	public void readPatientWithID(Patient toReadPatient){
		Patient patient = client
				.read()
				.resource(Patient.class)
				.withId(toReadPatient.getId())
				.execute();
	}
}
