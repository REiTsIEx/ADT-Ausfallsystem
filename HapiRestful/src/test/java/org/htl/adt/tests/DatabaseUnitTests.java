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
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.IdDt;

public class DatabaseUnitTests {

	@Before
	public void init() {
		
		Connector connector = null;
		try {
			connector = DBFactory.getInstance().getConnector("DBConnector");
		} catch (CommunicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			connector.deleteAllPatients();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("Hallo");
		}
		
	}
	
	/**
	* 
	* Fügt einen DatabasePatient zur Datenbank hinzu.
	* Der Patient wird mithilfe des DBConnectors in die Datenbank hinzugefügt
	*/
	@Test
	public void addPatient() {
		Patient testPatient = new Patient();
		testPatient.setId(new IdDt("8577"));
		testPatient.addName().addFamily("Nachname789").addGiven("Vorname");
		testPatient.setGender(AdministrativeGenderEnum.MALE);
		
		Patient returnPatient = new Patient();
		
		Connector connector = null;
		try {
			connector = DBFactory.getInstance().getConnector("DBConnector");
			
		} catch (CommunicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			connector.addPatient(new PatientRequest("Patient hinzufügen", testPatient));
			
			returnPatient = connector.getPatientbyIdentifier(new PatientRequest("Patient nach Nachname suchen", testPatient));


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		assertTrue(testPatient.getId().getIdPart().equals(returnPatient.getId().getIdPart()));
		
	}
	
	@Test
	public void addEncounter() {
		Patient testPatient = new Patient();
		testPatient.setId(new IdDt("8577"));
		testPatient.addName().addFamily("Nachname789").addGiven("Vorname");
		testPatient.setGender(AdministrativeGenderEnum.MALE);
		
		Encounter testEncounter = new Encounter();
		testEncounter.setId(new IdDt("5"));
		
		
		Connector connector = null;
		try {
			
			connector = DBFactory.getInstance().getConnector("DBConnector");
			
		} catch (CommunicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			
			
			connector.addEncounter(new PatientRequest("Patient für den Encounter hinzugefügt wird",testPatient), new EncounterRequest("", testEncounter));


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
		
		Connector connector = null;
		try {
			connector = DBFactory.getInstance().getConnector("DBConnector");
		} catch (CommunicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
		
		String searchID = "23";
		
		Patient testPatient = new Patient();
		testPatient.setId(new IdDt("23"));
		testPatient.addName().addFamily("Nach").addGiven("Tobias");
		testPatient.setGender(AdministrativeGenderEnum.MALE);
				
		Connector connector = null;
		try {
			connector = DBFactory.getInstance().getConnector("DBConnector");
		} catch (CommunicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<Patient> patientlist = new ArrayList<Patient>();
				
		Map<String, String> params = new HashMap<String, String>();
		
		//params.put("patientGender", "male");
		params.put("familyName", "Nach");
		params.put("firstName", "Tobias");
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
		Patient testPatient = new Patient();
		testPatient.setId(new IdDt("101"));
		testPatient.addName().addFamily("Nach").addGiven("Tobias");
		testPatient.setGender(AdministrativeGenderEnum.MALE);
		
		Connector connector = null;
		try {
			connector = DBFactory.getInstance().getConnector("DBConnector");
		} catch (CommunicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
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
		Connector connector = null;
		try {
			connector = DBFactory.getInstance().getConnector("DBConnector");
		} catch (CommunicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Patient testPatient = new Patient();
		testPatient.setId(new IdDt("101"));
		testPatient.addName().addFamily("Nachname789").addGiven("Vorname");
		testPatient.setGender(AdministrativeGenderEnum.MALE);
		
		Patient searchPatient = new Patient();
		searchPatient.setId(new IdDt("101"));
		Patient returnPatient = null;
				
		try {
			connector.addPatient(new PatientRequest("Patient hinzufügen", testPatient));
			
			returnPatient = connector.getPatientbyIdentifier(new PatientRequest("Patient nach Nachname suchen", searchPatient));
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AdtSystemErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(searchPatient.getId().getIdPart().equals(returnPatient.getId().getIdPart()));
		
	}

}
