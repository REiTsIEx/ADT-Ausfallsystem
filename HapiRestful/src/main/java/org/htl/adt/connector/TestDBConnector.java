package org.htl.adt.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htl.adt.client.RestfulClient;
import org.htl.adt.db.TestDB;
import org.htl.adt.domainobjects.EncounterRequest;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.LocationRequest;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.exception.AdtSystemErrorException;
import org.htl.adt.interfaces.Connector;

import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Encounter.Location;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * Diese Klasse dient als Connector zur TestDB
 */
public class TestDBConnector implements Connector{
	TestDB db = new TestDB();

	public void addPatient(PatientRequest patientRequest) {
		db.myPatients.put(patientRequest.getPatient().getId().getIdPartAsLong(), patientRequest.getPatient());
	}


	public List<Patient> searchPatient(PatientRequest patientRequest) {
		IdDt patID = patientRequest.getPatient().getId();
		Patient dbPatient = db.myPatients.get(patID.getIdPartAsLong());
		if(dbPatient == null){
			throw new ResourceNotFoundException("Der Patient mit der ID " + patID.getIdPart() + " ist nicht vorhanden");
		}
		List<Patient> retValue = new ArrayList<Patient>();
		retValue.add(dbPatient);
		
		return retValue;
	}
	
	public List<Patient> getAllPatients() {
		List<Patient> retValue = new ArrayList<Patient>();
		retValue.addAll(db.myPatients.values());
		return retValue;
	}

	public void getConnection() {
		// TODO Auto-generated method stub
		
	}

	public void setConnection(String url) {
		// TODO Auto-generated method stub

	}
	
	public List<Patient> searchPatientWithParameters(Map<String, String> patientParams){
		List<Patient> retValue = new ArrayList<Patient>();
		String patientFamilyName = patientParams.get("familyName");
		for (Patient next : db.myPatients.values()){
			String familyName = next.getNameFirstRep().getFamilyAsSingleString().toLowerCase();
			if(!familyName.contains(patientFamilyName.toLowerCase())){
				continue;
			}
			retValue.add(next);
		}
		if(retValue.isEmpty())
			throw new ResourceNotFoundException("Patient mit dem Nachnamen " + patientFamilyName + " nicht vorhanden");
		return retValue;
	}

	public void updatePatient(PatientRequest patientRequest) {
		db.myPatients.put(patientRequest.getPatient().getId().getIdPartAsLong(), patientRequest.getPatient());
	}

	public Patient getPatientbyIdentifier(PatientRequest patientRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addEncounter(PatientRequest patientRequest, EncounterRequest encounterRequest) {
		// TODO Auto-generated method stub
		
	}

	public void addLocationtoEncounter(EncounterRequest encounterRequest, LocationRequest locationRequest) {
		// TODO Auto-generated method stub
		
	}

	public List<Encounter> getEncounterbyPatient(Identifier patientIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}


	public void resetDatabase() throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		
	}


	public void addEncounter(EncounterRequest encounterRequest) throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		
	}


	public void addLocation(LocationRequest locationRequest) throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		
	}


	public List<ca.uhn.fhir.model.dstu2.resource.Location> getAllLocation() throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		return null;
	}


	public List<Encounter> getEncounterbyPatientID(Identifier patientIdentifier) throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		return null;
	}


	public Encounter getLastEncounterbyPatientID(Identifier patientIdentifier) throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		return null;
	}


	public void deleteAllDatabaseRows() throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		
	}


	

}
