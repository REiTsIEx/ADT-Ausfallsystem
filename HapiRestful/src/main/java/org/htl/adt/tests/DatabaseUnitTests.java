package org.htl.adt.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.htl.adt.connector.DBFactory;
import org.htl.adt.domainobjects.DatabasePatient;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.interfaces.Connector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;

public class DatabaseUnitTests {

	@Before
	public void init() {
		BasicConfigurator.configure();
	}
	
	@After
	public void cleanup() {
		
		
	}
	
	/**
	* 
	* Fügt einen DatabasePatient zur Datenbank hinzu.
	* Es wird nicht der DBConnector aufgerufen um den Patienten hinzuzufï¿½gen, sondern wird direkt an die Datenbank übergeben.
	*/
	@Test
	public void addPatientDirect() {
		
		FhirContext ctx = FhirContext.forDstu2();

		Patient testPatient = new Patient();
		testPatient.addIdentifier().setSystem("http://loinc.org").setValue("1234");
		testPatient.addName().addFamily("Mustermann").addGiven("Max").addGiven("M");
		testPatient.setGender(AdministrativeGenderEnum.MALE);

		String fhirMessage = ctx.newXmlParser().encodeResourceToString(testPatient);
		
		BasicConfigurator.configure();

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
	* Der Patient wird mithilfe des DBConnectors in die Datenbank hinzugefï¿½gt
	*/
	@Test
	public void addPatient() {
		Patient testPatient = new Patient();
		testPatient.addIdentifier().setSystem("http://loinc.org").setValue("1234");
		testPatient.addName().addFamily("Nachname").addGiven("Marcel").addGiven("M");
		testPatient.setGender(AdministrativeGenderEnum.MALE);
		
		Connector connector = DBFactory.getInstance().getConnector("DBConnector");
		
		try {
			connector.addPatient(new PatientRequest("Patient hinzufügen", testPatient));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	* 
	* Ruft alle Patienten aus der Datenbank an.
	* Die Patienten werden mithilfe des DBConnectors aus der Datenbank abgerufen
	*/
	@Test
	public void getAllPatient() {
		BasicConfigurator.configure();
		
		Connector connector = DBFactory.getInstance().getConnector("DBConnector");
		
		List<Patient> patientlist = new ArrayList<Patient>();

		try {
			patientlist = connector.getAllPatients();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Patient patient : patientlist) {
			System.out.println(patient.getNameFirstRep().getNameAsSingleString());
		}
		
	}

}
