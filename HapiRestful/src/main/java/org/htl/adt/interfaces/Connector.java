package org.htl.adt.interfaces;

import java.util.List;
import java.util.Map;

import org.htl.adt.domainobjects.EncounterRequest;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.LocationRequest;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.exception.AdtSystemErrorException;

import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Patient;

public interface Connector {

	public void addPatient(PatientRequest patientRequest)
			throws AdtSystemErrorException;

	public void updatePatient(PatientRequest patientRequest)
			throws AdtSystemErrorException;

	public Patient getPatientbyIdentifier(PatientRequest patientRequest)
			throws AdtSystemErrorException;

	public List<Patient> getAllPatients() throws AdtSystemErrorException;

	public void addEncounter(EncounterRequest encounterRequest)
			throws AdtSystemErrorException;

	public void addLocation(LocationRequest locationRequest)
			throws AdtSystemErrorException;
	
	public List<Location> getAllLocation()
			throws AdtSystemErrorException;

	public List<Encounter> getEncounterbyPatientID(Identifier patientIdentifier)
			throws AdtSystemErrorException;

	public Encounter getLastEncounterbyPatientID(Identifier patientIdentifier)
			throws AdtSystemErrorException;

	public List<Patient> searchPatientWithParameters(
			Map<String, String> patientParameter)
			throws AdtSystemErrorException;
	
	public void deleteAllDatabaseRows() throws AdtSystemErrorException;

	public void getConnection();

	public void setConnection(String url);

}
