package org.htl.adt.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.htl.adt.connector.DBFactory;
import org.htl.adt.domainobjects.EncounterRequest;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.exception.AdtSystemErrorException;
import org.htl.adt.exception.CommunicationException;
import org.htl.adt.hibernateresources.DatabasePatient;
import org.htl.adt.interfaces.Connector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.IdDt;

public class DatabaseUnitTests {
	
	Connector connector = null;
	Patient testPatient = new Patient();
	Encounter testEncounter = new Encounter();

	@Before
	public void init() {
		
		testPatient.setId(new IdDt("633421021994"));
		testPatient.addName().addFamily("Mustermann").addGiven("Max");
		testPatient.setGender(AdministrativeGenderEnum.FEMALE);		
		
		try {		
			connector = DBFactory.getInstance().getConnector("DBConnector");
			
			connector.deleteAllPatients();			
		} catch (CommunicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (AdtSystemErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	* 
	* Fügt einen DatabasePatient zur Datenbank hinzu.
	* Der Patient wird mithilfe des DBConnectors in die Datenbank hinzugefügt
	*/
	@Test
	public void addPatient() {
		
		Patient returnPatient = new Patient();		
		
			try {
				connector.addPatient(new PatientRequest("Patient hinzufügen", testPatient));
				
				returnPatient = connector.getPatientbyIdentifier(new PatientRequest("Patient nach Nachname suchen", testPatient));

			} catch (AdtSystemErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		assertTrue(testPatient.getId().getIdPart().equals(returnPatient.getId().getIdPart()));
		
	}
	
	@Test
	public void addEncounter() {
		
		testEncounter.setId(new IdDt("5"));
		testEncounter.setPatient(new ResourceReferenceDt(testPatient.getId()));
		
		
		try {
			
			connector.addEncounter(new EncounterRequest("", testEncounter));


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		assertTrue(true);//Abfrage fehlt noch
		
	}
	
	/**
	* 
	* Ruft alle Patienten aus der Datenbank an.
	* Die Patienten werden mithilfe des DBConnectors aus der Datenbank abgerufen
	*/
	@Test
	public void getAllPatient() {
		
		List<Patient> patientlist = new ArrayList<Patient>();

		try {
			patientlist = connector.getAllPatients();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AdtSystemErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Patient patient : patientlist) {
			System.out.println("Identifier: " + patient.getId().getIdPart() + " - " + patient.getNameFirstRep().getNameAsSingleString() );
			
		}
		
	}

	/**
	* 
	* Ruft alle Patienten aus der Datenbank an.
	* Die Patienten werden mithilfe des DBConnectors aus der Datenbank abgerufen
	*/
	@Test
	public void searchPatientWithParameters() {
		
		String searchID = testPatient.getId().getIdPart();
		String searchFamilyName = testPatient.getNameFirstRep().getFamilyAsSingleString();
		String searchFirstName = testPatient.getNameFirstRep().getGiven().get(0).toString();
		String searchGender = testPatient.getGender();
			
		List<Patient> patientlist = new ArrayList<Patient>();
				
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("patientGender", searchGender);
		params.put("familyName", searchFamilyName);
		params.put("firstName", searchFirstName);
		params.put("patientIdentifier", searchID);
		
		try {
			connector.addPatient(new PatientRequest("Patient hinzufügen", testPatient));
			
			patientlist = connector.searchPatientWithParameters(params);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			System.out.println("Error");
			e.printStackTrace();
		} catch (AdtSystemErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean assertValue = true;
		for (Patient patient : patientlist) {
			if(patient.getId().getIdPart().equals(searchID)){
			System.out.println("Identifier: " + patient.getId().getIdPart() + " - " + patient.getNameFirstRep().getNameAsSingleString() );
			}else{
				System.out.println("ERROR");
				assertValue = false;
			}
		}
		
		assertTrue(assertValue);

		
	}
	
	/**
	* 
	* Updatet einen DatabasePatient in der Datenbank.
	* Der Patient wird mithilfe des DBConnectors in die Datenbank gesendet
	*/
	@Test
	public void updatePatient() {
		try {
			
			connector.updatePatient(new PatientRequest("", testPatient));
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AdtSystemErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	/**
	* 
	* Sucht einen Patienten mit dem Identifier in der Datenbank
	*/
	@Test
	public void getPatientbyIdentifier() {
				
		Patient returnPatient = null;
		
		try {
			connector.addPatient(new PatientRequest("Patient hinzufügen", testPatient));
			
			returnPatient = connector.getPatientbyIdentifier(new PatientRequest("Patient nach Nachname suchen", testPatient));
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AdtSystemErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(testPatient.getId().getIdPart().equals(returnPatient.getId().getIdPart()));
		
	}

}
