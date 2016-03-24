package org.htl.adt.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

public class RestfulClient {
	FhirContext context = FhirContext.forDstu2();
	String serverURL = "http://localhost:8080/Ausfallsystem/hapiservlet/";
	IGenericClient client = context.newRestfulGenericClient(serverURL);
	
	public RestfulClient() {

	}
	
	public List<Patient> searchPatient(Patient toSearchPatient){
		Bundle results = client
				.search()
				.forResource(Patient.class)
				.where(Patient.FAMILY.matches().value(toSearchPatient.getNameFirstRep().getFamilyAsSingleString()))
				.returnBundle(Bundle.class)
				.execute();	
		List<Patient> allPatients = new ArrayList();
		for(Entry entry : results.getEntry()){
			allPatients.add((Patient) entry.getResource());
		}
		return allPatients;
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
	
	public Patient readPatientWithID(Patient toReadPatient){
		Patient patient = client
				.read()
				.resource(Patient.class)
				.withId(toReadPatient.getId())
				.execute();
		return patient;
	}
	
	public Location readLocationWithID(Location toReadLoction) {
		return client
				.read()
				.resource(Location.class)
				.withId(toReadLoction.getId())
				.execute();
	}
}
