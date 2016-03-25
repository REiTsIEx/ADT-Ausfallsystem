package org.htl.adt.domainobjects;

import ca.uhn.fhir.model.dstu2.resource.Patient;

public class PatientRequest extends Request {
		
	private Patient patient;
	
	
	public PatientRequest(String messageText, Patient messageObject) {
		super(messageText);
		this.patient = messageObject;
		
	}

	public PatientRequest() {
		super();
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	

}
