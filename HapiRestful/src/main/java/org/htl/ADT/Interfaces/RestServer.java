package org.htl.ADT.Interfaces;

import java.util.List;

import org.htl.ADT.DomainObjects.Identifier;
import org.htl.ADT.DomainObjects.PatientRequest;

import ca.uhn.fhir.model.dstu2.resource.Patient;

public interface RestServer {
	public List<Patient>  getAllPatient();
	
	public void addPatient (PatientRequest patient); 

	public List<Patient> searchPatient(PatientRequest patient);
	
	public void updatePatient(Identifier id, PatientRequest patient);

	public void movePatient(Identifier id, PatientRequest patient);
	
	public void releasePatient(Identifier id, PatientRequest patient);

	public Patient searchPatientOK(PatientRequest pat);
	
}
