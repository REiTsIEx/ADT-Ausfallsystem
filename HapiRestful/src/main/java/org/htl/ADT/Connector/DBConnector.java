package org.htl.ADT.Connector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.htl.ADT.DomainObjects.DatabasePatient;
import org.htl.ADT.DomainObjects.Identifier;
import org.htl.ADT.DomainObjects.PatientRequest;
import org.htl.ADT.Interfaces.Connector;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.parser.IParser;

public class DBConnector implements Connector {

	private Configuration config;
	private SessionFactory factory;

	public DBConnector() {

		config = new Configuration();
		config.configure("hibernate.cfg.xml");

		factory = config.buildSessionFactory();
		

	}

	public void addPatient(PatientRequest patient) {
		// TODO Auto-generated method stub

		Session session = null;
		Transaction transaction = null;
		
		

		//try {

			session = factory.openSession();

			transaction = session.beginTransaction();

			FhirContext ctx = FhirContext.forDstu2();
			String fhirMessage = ctx.newXmlParser().encodeResourceToString(patient.patient);

			DatabasePatient data = new DatabasePatient("12345",
					patient.patient.getNameFirstRep().getNameAsSingleString(),
					patient.patient.getNameFirstRep().getFamilyAsSingleString(), fhirMessage);

			session.save(data);

			transaction.commit();// transaction is committed
/*
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			;

		}*/

	}

	public Patient searchPatient(PatientRequest patient) {
		// TODO Auto-generated method stub

		return null;
	}

	public List<Patient> getAllPatients() {
		// TODO Auto-generated method stub
		BasicConfigurator.configure();

		FhirContext ctx = FhirContext.forDstu2();
		IParser parser = ctx.newXmlParser();

		List<DatabasePatient> datalist = new ArrayList<DatabasePatient>();
		List<Patient> patientlist = new ArrayList<Patient>();

		Session session = null;
		Transaction transaction = null;

		try {

			session = factory.openSession();

			transaction = session.beginTransaction();

			datalist = session.createCriteria(DatabasePatient.class).list();
			
			for (DatabasePatient data : datalist) {
				Patient patient = parser.parseResource(Patient.class, data.getFhirMessage());
				patientlist.add(patient);
			}

			transaction.commit();// transaction is committed

		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}

		}
		return patientlist;
	}

	public void getConnection() {
		// TODO Auto-generated method stub

	}

	public void setConnection(String url) {
		// TODO Auto-generated method stub

	}

	public void updatePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub

	}

	public List<Patient> searchPatientWithFamily(PatientRequest patient) {
		// TODO Auto-generated method stub
		return null;
	}

}
