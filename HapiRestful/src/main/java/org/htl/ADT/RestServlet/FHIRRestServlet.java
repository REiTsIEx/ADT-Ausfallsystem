package org.htl.ADT.RestServlet;

import java.util.List;

import org.htl.ADT.DomainObjects.Identifier;
import org.htl.ADT.DomainObjects.PatientRequest;
import org.htl.ADT.Interfaces.RestServer;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class FHIRRestServlet implements RestServer{

	public List<Patient> getAllPatient() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addPatient(PatientRequest patient) {
		// TODO Auto-generated method stub
		
	}

	public List<Patient> searchPatient(PatientRequest patient) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updatePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub
		
	}

	public void movePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub
		
	}

	public void releasePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub
		
	}

	public Patient searchPatientWithID(PatientRequest pat) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Patient> searchPatientWithFamily(PatientRequest patient) {
		// TODO Auto-generated method stub
		return null;
	}

}
