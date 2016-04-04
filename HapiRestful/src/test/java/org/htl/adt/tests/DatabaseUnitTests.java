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
import org.htl.adt.domainobjects.LocationRequest;
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
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.LocationStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;

public class DatabaseUnitTests {

	Connector connector = null;
	Patient testPatient = new Patient();
	Encounter testEncounter = new Encounter();
	Location testLocation = new Location();

	@Before
	public void init() {

		testPatient.setId(new IdDt("633421021994"));
		testPatient.addName().addFamily("Mustermann").addGiven("Erika");
		testPatient.setGender(AdministrativeGenderEnum.FEMALE);
		testPatient.addIdentifier().setValue("633421021994");
		
		testLocation.setId(new IdDt("radlog"));
		testLocation.setName("Radiologie");
		testLocation.setStatus(LocationStatusEnum.ACTIVE);
		testLocation.setDescription("Radiologie Abteilung");


		testEncounter.setId(new IdDt("5"));
		testEncounter.setPatient(new ResourceReferenceDt(testPatient.getId()));
		testEncounter.addLocation(new ca.uhn.fhir.model.dstu2.resource.Encounter.Location().setLocation(new ResourceReferenceDt(testLocation.getId())));

		
		try {
			connector = DBFactory.getInstance().getConnector("DBConnector");

			connector.deleteAllDatabaseRows();
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
	 * Fügt einen DatabasePatient zur Datenbank hinzu. Der Patient wird mithilfe
	 * des DBConnectors in die Datenbank hinzugefügt
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

		String testID = testPatient.getId().getIdPart();
		String testFamily = testPatient.getNameFirstRep().getFamilyFirstRep().toString();
		String testGiven = testPatient.getNameFirstRep().getGivenFirstRep().toString();
		String testGender = testPatient.getGender();
		
		String returnID = returnPatient.getId().getIdPart();
		String returnFamily = returnPatient.getNameFirstRep().getFamilyFirstRep().toString();
		String returnGiven = returnPatient.getNameFirstRep().getGivenFirstRep().toString();
		String returnGender = returnPatient.getGender();
		
		assertTrue(testID.equals(returnID) && testFamily.equals(returnFamily) && testGiven.equals(returnGiven) && testGender.equals(returnGender));

	}
	
	/**
	 * 
	 * Fügt einen DatabasePatient zur Datenbank hinzu. Der Patient wird mithilfe
	 * des DBConnectors in die Datenbank hinzugefügt
	 */
	@Test
	public void addLocation() {

		List<Location> locationList = new ArrayList<Location>();
		Location returnLocation = null;

		try {
			connector.addLocation(new LocationRequest("Add Location", testLocation));

			locationList = connector.getAllLocation();
			returnLocation = locationList.get(0);

		} catch (AdtSystemErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String testID = testLocation.getId().getIdPart();
		String testName = testLocation.getName();
		String testStatus = testLocation.getStatus();
		String testDescription = testLocation.getDescription();
		
		String returnID = returnLocation.getId().getIdPart();
		String returnName = returnLocation.getName();
		String returnStatus = returnLocation.getStatus();
		String returnDescription = returnLocation.getDescription();
		
		assertTrue(testID.equals(returnID) && testName.equals(returnName) && testStatus.equals(returnStatus) && testDescription.equals(returnDescription));

	}

	@Test
	public void addEncounter() {

		try {
			connector.addPatient(new PatientRequest("", testPatient));
			
			connector.addLocation(new LocationRequest("", testLocation));			
			
			connector.addEncounter(new EncounterRequest("", testEncounter));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		assertTrue(true);// Abfrage fehlt noch

	}

	/**
	 * 
	 * Ruft alle Patienten aus der Datenbank an. Die Patienten werden mithilfe
	 * des DBConnectors aus der Datenbank abgerufen
	 */
	@Test
	public void getAllPatient() {

		List<Patient> patientlist = new ArrayList<Patient>();

		Patient testPatient2 = new Patient();
		testPatient2.setId(new IdDt("567308121999"));
		testPatient2.addName().addFamily("Mustermann").addGiven("Max");
		testPatient2.setGender(AdministrativeGenderEnum.MALE);

		try {
			connector.addPatient(new PatientRequest("", testPatient));
			connector.addPatient(new PatientRequest("", testPatient2));

			patientlist = connector.getAllPatients();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AdtSystemErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean assertValue = true;
		for (Patient patient : patientlist) {

			String id = patient.getId().getIdPart();
			if (id.equals(testPatient.getId().getIdPart()) || id.equals(testPatient2.getId().getIdPart())) {
				System.out.println("Identifier: " + patient.getId().getIdPart() + " - " + patient.getNameFirstRep().getNameAsSingleString());
			} else {
				System.out.println("ERROR");
				assertValue = false;
			}
		}

		assertTrue(assertValue);

	}
	
	@Test
	public void getAllEncounterbyPatientID() {

		List<Encounter> encounterList = new ArrayList<Encounter>();

		try {
			
			connector.addPatient(new PatientRequest("", testPatient));
			
			connector.addLocation(new LocationRequest("", testLocation));			
			
			connector.addEncounter(new EncounterRequest("", testEncounter));
			
			encounterList = connector.getEncounterbyPatientID(new Identifier(testPatient.getId()));
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AdtSystemErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean assertValue = true;
		for (Encounter encounter : encounterList) {

			String id = encounter.getId().getIdPart();
			if (id.equals(testEncounter.getId().getIdPart())) {
				System.out.println("EncounterIdentifier: " + encounter.getId().getIdPart() + " - " + encounter.getPatient().getReference().getIdPart() + " - " + encounter.getLocationFirstRep().getLocation().getReference());
			} else {
				System.out.println("ERROR");
				assertValue = false;
			}
		}

		assertTrue(assertValue);

	}

	/**
	 * 
	 * Ruft alle Patienten aus der Datenbank an. Die Patienten werden mithilfe
	 * des DBConnectors aus der Datenbank abgerufen
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

		} catch (AdtSystemErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean assertValue = true;
		for (Patient patient : patientlist) {
			if (patient.getId().getIdPart().equals(searchID)) {
				System.out.println("Identifier: " + patient.getId().getIdPart() + " - " + patient.getNameFirstRep().getNameAsSingleString());
			} else {
				System.out.println("ERROR");
				assertValue = false;
			}
		}

		assertTrue(assertValue);

	}

	/**
	 * 
	 * Updatet einen DatabasePatient in der Datenbank. Bei diesem Testfall gibt
	 * es einen Patienten in der Datenbank der überschrieben wird
	 */
	@Test
	public void updatePatientwithMerge() {
		
		Patient updatePatient = new Patient();
		updatePatient.setId(testPatient.getId());
		updatePatient.addName().addFamily("Doppler").addGiven("Susane");
		updatePatient.setGender(AdministrativeGenderEnum.FEMALE);
		
		Patient returnPatient = new Patient();

		try {
			connector.addPatient(new PatientRequest("", testPatient));

			connector.updatePatient(new PatientRequest("", updatePatient));
			
			returnPatient = connector.getPatientbyIdentifier(new PatientRequest("Patient nach Nachname suchen", testPatient));

		} catch (AdtSystemErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String updateID = updatePatient.getId().getIdPart();
		String updateFamily = updatePatient.getNameFirstRep().getFamilyFirstRep().toString();
		String updateGiven = updatePatient.getNameFirstRep().getGivenFirstRep().toString();
		String updateGender = updatePatient.getGender();
		
		String returnID = returnPatient.getId().getIdPart();
		String returnFamily = returnPatient.getNameFirstRep().getFamilyFirstRep().toString();
		String returnGiven = returnPatient.getNameFirstRep().getGivenFirstRep().toString();
		String returnGender = returnPatient.getGender();

		
		assertTrue(updateID.equals(returnID) && updateFamily.equals(returnFamily) && updateGiven.equals(returnGiven) && updateGender.equals(returnGender));
		
	}

	/**
	 * 
	 * Updatet einen DatabasePatient in der Datenbank. Bei diesem Testfall gibt
	 * es noch keinen Patienten in der Datenbank, deshalb wird er ein neuer
	 * angelegt
	 */
	@Test
	public void updatePatientwithCreate() {

		Patient returnPatient = new Patient();
		
		try {

			connector.updatePatient(new PatientRequest("", testPatient));
			
			returnPatient = connector.getPatientbyIdentifier(new PatientRequest("Patient nach Nachname suchen", testPatient));
			
		} catch (AdtSystemErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String testID = testPatient.getId().getIdPart();
		String testFamily = testPatient.getNameFirstRep().getFamilyFirstRep().toString();
		String testGiven = testPatient.getNameFirstRep().getGivenFirstRep().toString();
		String testGender = testPatient.getGender();
		
		String returnID = returnPatient.getId().getIdPart();
		String returnFamily = returnPatient.getNameFirstRep().getFamilyFirstRep().toString();
		String returnGiven = returnPatient.getNameFirstRep().getGivenFirstRep().toString();
		String returnGender = returnPatient.getGender();
		
		assertTrue(testID.equals(returnID) && testFamily.equals(returnFamily) && testGiven.equals(returnGiven) && testGender.equals(returnGender));

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

		String testID = testPatient.getId().getIdPart();
		String testFamily = testPatient.getNameFirstRep().getFamilyFirstRep().toString();
		String testGiven = testPatient.getNameFirstRep().getGivenFirstRep().toString();
		String testGender = testPatient.getGender();
		
		String returnID = returnPatient.getId().getIdPart();
		String returnFamily = returnPatient.getNameFirstRep().getFamilyFirstRep().toString();
		String returnGiven = returnPatient.getNameFirstRep().getGivenFirstRep().toString();
		String returnGender = returnPatient.getGender();
		
		assertTrue(testID.equals(returnID) && testFamily.equals(returnFamily) && testGiven.equals(returnGiven) && testGender.equals(returnGender));

	}

}
