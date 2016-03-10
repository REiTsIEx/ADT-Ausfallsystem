package org.htl.adt.connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.htl.adt.domainobjects.DatabasePatient;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.interfaces.Connector;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.parser.IParser;

public class DBConnector implements Connector {

	private Configuration config;
	private SessionFactory sessionFactory;

	public DBConnector() {
	BasicConfigurator.configure();
	
		System.out.println("Nach DB erzeugung");
		config = new Configuration();
		config.configure("hibernate.cfg.xml");

		sessionFactory = config.buildSessionFactory();
		System.out.println("Nach config erzeugung");
	}

	public void addPatient(PatientRequest patient) throws IOException {
		// TODO Auto-generated method stub

		Session session = null;
		Transaction transaction = null;

		try {

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			FhirContext ctx = FhirContext.forDstu2();
			String fhirMessage = ctx.newXmlParser().encodeResourceToString(patient.patient);

			DatabasePatient data = new DatabasePatient(patient.patient.getId().toString(),
					patient.patient.getNameFirstRep().getGivenAsSingleString(),
					patient.patient.getNameFirstRep().getFamilyAsSingleString(), fhirMessage);

			session.save(data);

			transaction.commit();// transaction is committed

		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new IOException("Fehler beim hinzufügen des Patienten: " +  exception.getMessage());
		}

	}

	public Patient searchPatient(PatientRequest patient) {
		// TODO Auto-generated method stub

		return null;
	}

	public List<Patient> getAllPatients() throws IOException {
		// TODO Auto-generated method stub
		FhirContext ctx = FhirContext.forDstu2();
		IParser parser = ctx.newXmlParser();

		List<DatabasePatient> datalist = new ArrayList<DatabasePatient>();
		List<Patient> patientlist = new ArrayList<Patient>();

		Session session = null;
		Transaction transaction = null;

		try {

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			datalist = session.createCriteria(DatabasePatient.class).list();

			for (DatabasePatient data : datalist) {
				Patient fhirPatient = parser.parseResource(Patient.class, data.getFhirMessage());
				patientlist.add(fhirPatient);
			}

			transaction.commit();// transaction is committed

		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			
			throw new IOException("Fehler beim abrufen der Patienten:" +  exception.getMessage());

		}
		return patientlist;
	}

	public void updatePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub

	}

	public List<Patient> searchPatientWithFamily(PatientRequest patient) throws IOException {
		// TODO Auto-generated method stub
		FhirContext ctx = FhirContext.forDstu2();
		IParser parser = ctx.newXmlParser();
	
		List<DatabasePatient> datalist = new ArrayList<DatabasePatient>();
		List<Patient> patientlist = new ArrayList<Patient>();

		Session session = null;
		Transaction transaction = null;

		try {

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			String criteriaLastName = patient.patient.getNameFirstRep().getFamilyAsSingleString().toLowerCase();
			
			datalist = session.createCriteria(DatabasePatient.class)
				    .add( Restrictions.like("lastName", criteriaLastName))
				    .addOrder( Order.asc("patient_id")).list();	
			
			for (DatabasePatient data : datalist) {
				Patient fhirPatient = parser.parseResource(Patient.class, data.getFhirMessage());
				patientlist.add(fhirPatient);
			}

			transaction.commit();// transaction is committed

		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			
			throw new IOException("Fehler beim abrufen der Patienten:" +  exception.getMessage());

		}
		return patientlist;
	}
	
	public void getConnection() {
		// TODO Auto-generated method stub

	}

	public void setConnection(String url) {
		// TODO Auto-generated method stub

	}

}
