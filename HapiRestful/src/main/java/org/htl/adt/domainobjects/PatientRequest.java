package org.htl.adt.domainobjects;

import ca.uhn.fhir.model.dstu2.resource.Patient;

public class PatientRequest extends Request {
		
	private Patient patient;
	
	
	public PatientRequest(String messageText, Patient messageObject) {
		this.messageText = messageText;
		this.patient = messageObject;
		
	}

	public PatientRequest() {
		
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	

}
