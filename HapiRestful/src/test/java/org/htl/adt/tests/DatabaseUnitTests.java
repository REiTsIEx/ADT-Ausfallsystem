package org.htl.adt.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.htl.adt.connector.DBFactory;
import org.htl.adt.domainobjects.DatabasePatient;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.interfaces.Connector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.IdDt;

public class DatabaseUnitTests {

	@Before
	public void init() {

	}
	
	@After
	public void cleanup() {		
		
	}
	
	/**
	* 
	* Fügt einen DatabasePatient zur Datenbank hinzu.
	* Es wird nicht der DBConnector aufgerufen um den Patienten hinzuzufügen, sondern wird direkt an die Datenbank übergeben.
	*/
	@Test
	public void addPatientDirect() {
		
		FhirContext ctx = FhirContext.forDstu2();

		Patient testPatient = new Patient();
		testPatient.addIdentifier().setSystem("http://loinc.org").setValue("1234");
		testPatient.addName().addFamily("Mustermann").addGiven("Max");
		testPatient.addName().addFamily("Mustermann").addGiven("Zweitname");
		testPatient.setGender(AdministrativeGenderEnum.MALE);

		String fhirMessage = ctx.newXmlParser().encodeResourceToString(testPatient);
		
		
		Configuration config = new Configuration();
		config.configure("hibernate.cfg.xml");
		

		SessionFactory factory = config.buildSessionFactory();

		
		Session session = factory.openSession();

		Transaction t = session.beginTransaction();


		DatabasePatient c1 = new DatabasePatient("12345", testPatient.getNameFirstRep().getNameAsSingleString(), testPatient.getNameFirstRep().getFamilyAsSingleString(), fhirMessage);
		session.save(c1);
		


		t.commit();// transaction is committed

		session.close();

	}

	
	/**
	* 
	* Fügt einen DatabasePatient zur Datenbank hinzu.
	* Der Patient wird mithilfe des DBConnectors in die Datenbank hinzugefügt
	*/
	@Test
	public void addPatient() {
		Patient testPatient = new Patient();
		testPatient.setId(new IdDt("50"));
		testPatient.addName().addFamily("Nachname789").addGiven("Vorname");
		testPatient.setGender(AdministrativeGenderEnum.MALE);
		
		Connector connector = DBFactory.getInstance().getConnector("DBConnector");
		
		try {
			connector.addPatient(new PatientRequest("Patient hinzufügen", testPatient));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(true);
		
	}
	
	/**
	* 
	* Ruft alle Patienten aus der Datenbank an.
	* Die Patienten werden mithilfe des DBConnectors aus der Datenbank abgerufen
	*/
	@Test
	public void getAllPatient() {
		
		Connector connector = DBFactory.getInstance().getConnector("DBConnector");
		
		List<Patient> patientlist = new ArrayList<Patient>();

		try {
			patientlist = connector.getAllPatients();
		} catch (RuntimeException e) {
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
	public void getPatientbyLastName() {
				
		Connector connector = DBFactory.getInstance().getConnector("DBConnector");
		
		List<Patient> patientlist = new ArrayList<Patient>();
		
		Patient testPatient = new Patient();
		testPatient.addName().addFamily("Nachname789");
		
		try {
			patientlist = connector.searchPatientWithFamily(new PatientRequest("Patient nach Nachname suchen", testPatient));
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			System.out.println("Error");
			e.printStackTrace();
		}
		
		for (Patient patient : patientlist) {
			System.out.println(patient.getNameFirstRep().getFamilyAsSingleString().toLowerCase());
		}
		
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
		
		Connector connector = DBFactory.getInstance().getConnector("DBConnector");
		
		
		try {
			connector.updatePatient(new PatientRequest("", testPatient));
		} catch (RuntimeException e) {
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
		Connector connector = DBFactory.getInstance().getConnector("DBConnector");
		
		Patient searchPatient = new Patient();
		searchPatient.setId(new IdDt("101"));
		Patient returnPatient = null;
				
		try {
			returnPatient = connector.getPatientbyIdentifier(new PatientRequest("Patient nach Nachname suchen", searchPatient));
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(returnPatient.getId().getIdPart() +  ": " + returnPatient.getNameFirstRep().getFamilyAsSingleString() + " " + returnPatient.getNameFirstRep().getGivenFirstRep().getValue());
		
	}

}
