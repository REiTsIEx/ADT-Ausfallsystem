package org.htl.ADT.Connector;

import java.util.List;

import org.htl.ADT.DomainObjects.Identifier;
import org.htl.ADT.DomainObjects.PatientRequest;
import org.htl.ADT.Interfaces.Connector;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class DBConnector implements Connector {

	public Patient addPatient(PatientRequest patient) {
		// TODO Auto-generated method stub
		return null;
	}


	public Patient searchPatient(PatientRequest patient) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Patient> getAllPatients() {
		// TODO Auto-generated method stub
		return null;
	}

	public void getConnection() {
		// TODO Auto-generated method stub
		
	}

	public void setConnection(String url) {
		// TODO Auto-generated method stub
		
	}

	public Patient updatePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub
		return null;
	}

}
