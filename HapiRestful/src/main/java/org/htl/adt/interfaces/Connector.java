package org.htl.adt.interfaces;

import java.io.IOException;
import java.util.List;

import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;

import ca.uhn.fhir.model.dstu2.resource.Patient;

public interface Connector {
	
	public void addPatient(PatientRequest patient) throws IOException;
	
	public void updatePatient(Identifier id, PatientRequest patient) throws IOException;

	public Patient searchPatient(PatientRequest patient) throws IOException;
	
	public List<Patient> getAllPatients() throws IOException;
	
	public void getConnection();
	
	public void setConnection(String url);
	
	public List<Patient> searchPatientWithFamily(PatientRequest patient) throws IOException;
}
