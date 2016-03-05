package org.htl.ADT.DomainObjects;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class Identifier {
	
	public IdDt identifier;

	public Identifier(IdDt identifier) {
		this.identifier = identifier;
	}

	public Identifier() {
	}
	
	public void getPatientId(Patient patient){
		this.identifier = patient.getId();
	}

}
