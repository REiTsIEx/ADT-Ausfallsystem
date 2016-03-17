package org.htl.adt.domainobjects;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class Identifier {
	
	private IdDt identifier;

	public Identifier(IdDt identifier) {
		this.identifier = identifier;
	}

	public Identifier() {
	}
	
	public void getPatientId(Patient patient){
		this.identifier = patient.getId();
	}

	public IdDt getIdentifier() {
		return identifier;
	}

	public void setIdentifier(IdDt identifier) {
		this.identifier = identifier;
	}

}
