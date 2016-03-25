package org.htl.adt.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.htl.adt.domainobjects.EncounterRequest;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.LocationRequest;
import org.htl.adt.domainobjects.PatientRequest;

import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Patient;

public interface Connector {
	
	public void addPatient(PatientRequest patientRequest);
	
	public void updatePatient(PatientRequest patientRequest);
	
	public Patient getPatientbyIdentifier(PatientRequest patientRequest);

	public List<Patient> searchPatient(PatientRequest patientRequest);
	
	public List<Patient> getAllPatients();
	
	public void addEncounter(PatientRequest patientRequest, EncounterRequest encounterRequest);
	
	public void addLocationtoEncounter(EncounterRequest encounterRequest, LocationRequest locationRequest);
	
	public List<Encounter> getEncounterbyPatient(Identifier patientIdentifier);
	
	public void getConnection();
	
	public void setConnection(String url);
	
	public List<Patient> searchPatientWithParams(Map<String, String> patientParams);
}
