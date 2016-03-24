package org.htl.adt.interfaces;

import java.io.IOException;
import java.util.List;

import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;

import ca.uhn.fhir.model.dstu2.resource.Patient;

public interface Connector {
	
	public void addPatient(PatientRequest patient);
	
	public void updatePatient(Identifier id, PatientRequest patient);

	public List<Patient> searchPatient(PatientRequest patient);
	
	public List<Patient> getAllPatients();
	
	public void getConnection();
	
	public void setConnection(String url);
	
	public List<Patient> searchPatientWithFamily(PatientRequest patient);
}
