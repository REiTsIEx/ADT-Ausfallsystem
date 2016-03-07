package org.htl.adt.interfaces;

import java.util.List;

import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;

import ca.uhn.fhir.model.dstu2.resource.Patient;

public interface RestServer {
	public List<Patient>  getAllPatient();
	
	public void addPatient (PatientRequest patient); 

	public List<Patient> searchPatient(PatientRequest patient);
	
	public void updatePatient(Identifier id, PatientRequest patient);

	public void movePatient(Identifier id, PatientRequest patient);
	
	public void releasePatient(Identifier id, PatientRequest patient);

	public Patient searchPatientWithID(PatientRequest pat);
	
	public List<Patient> searchPatientWithFamily(PatientRequest patient);
	
}
