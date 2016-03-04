package org.htl.ADT.Tests;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.htl.ADT.Connector.DBFactory;
import org.htl.ADT.DomainObjects.DatabasePatient;
import org.htl.ADT.DomainObjects.PatientRequest;
import org.htl.ADT.Interfaces.Connector;
import org.junit.Test;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;

public class DatabaseUnitTests {

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
	* Der Patient wird mithilfe des DBConnectors in die Datenbank hinzugefügt
	*/
	@Test
	public void addPatient() {
		BasicConfigurator.configure();

		Patient testPatient = new Patient();
		testPatient.addIdentifier().setSystem("http://loinc.org").setValue("1234");
		testPatient.addName().addFamily("Marcel").addGiven("Nachname").addGiven("M");
		testPatient.setGender(AdministrativeGenderEnum.MALE);
		
		Connector connector = DBFactory.getInstance().getConnector("DBConnector");
		
		connector.addPatient(new PatientRequest("Patient hinzufügen", testPatient));
		
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

		patientlist = connector.getAllPatients();
		
		for (Patient patient : patientlist) {
			System.out.println(patient.getNameFirstRep().getNameAsSingleString());
		}
		
	}

}
