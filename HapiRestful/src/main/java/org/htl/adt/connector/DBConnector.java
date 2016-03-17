package org.htl.adt.connector;

import java.io.IOException;
import java.sql.SQLException;
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
import org.htl.adt.client.RestfulClient;
import org.htl.adt.domainobjects.DatabasePatient;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.interfaces.Connector;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;

public class DBConnector implements Connector {
	// private RestfulClient client = new RestfulClient();
	private Configuration config;
	private SessionFactory sessionFactory;

	public DBConnector() {
		BasicConfigurator.configure();

		config = new Configuration();
		config.configure("hibernate.cfg.xml");

		sessionFactory = config.buildSessionFactory();
	}

	public void addPatient(PatientRequest patient) throws IOException {
		// TODO Auto-generated method stub

		Session session = null;
		Transaction transaction = null;

		try {

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			FhirContext ctx = FhirContext.forDstu2();
			String fhirMessage = ctx.newXmlParser().encodeResourceToString(patient.getPatient());

			DatabasePatient data = new DatabasePatient(patient.getPatient().getId().toString(),
					patient.getPatient().getNameFirstRep().getGivenAsSingleString(),
					patient.getPatient().getNameFirstRep().getFamilyAsSingleString(), fhirMessage);

			session.save(data);

			transaction.commit();// transaction is committed

		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new IOException("Fehler beim hinzufügen des Patienten: " + exception.getMessage());
		}
	}

	public List<Patient> searchPatient(PatientRequest patient) {
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

			throw new IOException("Fehler beim abrufen der Patienten:" + exception.getMessage());

		}
		return patientlist;
	}

	public void updatePatient(Identifier id, PatientRequest patient) throws IOException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;
		
		try {

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			FhirContext ctx = FhirContext.forDstu2();
			String fhirMessage = ctx.newXmlParser().encodeResourceToString(patient.getPatient());

			
			List<DatabasePatient> selectValue = session.createCriteria(DatabasePatient.class)
					.add(Restrictions.like("identifier", id.getIdentifier().getIdPart()))
					.addOrder(Order.asc("patient_id"))
					.list();
			
			
			if (!selectValue.isEmpty()) {
				
				DatabasePatient insertValue = new DatabasePatient(patient.getPatient().getId().toString(),
						patient.getPatient().getNameFirstRep().getGivenAsSingleString(),
						patient.getPatient().getNameFirstRep().getFamilyAsSingleString(), fhirMessage);
							
				insertValue.setPatient_id(selectValue.get(0).getPatient_id());
				
				session.merge(insertValue);
				
			} else {
				
				DatabasePatient insertValue = new DatabasePatient(patient.getPatient().getId().toString(),
						patient.getPatient().getNameFirstRep().getGivenAsSingleString(),
						patient.getPatient().getNameFirstRep().getFamilyAsSingleString(), fhirMessage);
								
				session.save(insertValue);
				
			}
						
			transaction.commit();// transaction is committed

		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}

			throw new IOException("Fehler beim updaten des Patientes:" + exception.getMessage());

		}

	}

	public List<Patient> searchPatientWithFamily(PatientRequest patientRequest) throws IOException {
		// TODO Auto-generated method stub
		FhirContext ctx = FhirContext.forDstu2();
		IParser parser = ctx.newXmlParser();

		List<DatabasePatient> datalist;
		List<Patient> patientlist = new ArrayList<Patient>();

		Session session = null;
		Transaction transaction = null;

		try {

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			String criteriaLastName = patientRequest.getPatient().getNameFirstRep().getFamilyAsSingleString();

			datalist = session.createCriteria(DatabasePatient.class)
					.add(Restrictions.like("lastName", criteriaLastName))
					.addOrder(Order.asc("patient_id"))
					.list();

			
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

			throw new IOException("Fehler beim abrufen der Patienten:" + exception.getMessage());

		}
		/*
		 * Patient newPatient = new Patient(); newPatient.setId(new IdDt(3));
		 * newPatient.addIdentifier().setSystem("http://test.com/Patient").
		 * setValue("1234");
		 * newPatient.addName().addFamily("Simpson").addGiven("Homer").addGiven(
		 * "J"); newPatient.setGender(AdministrativeGenderEnum.MALE);
		 * client.createPatient(newPatient);
		 */
		return patientlist;
	}

	public void getConnection() {
		// TODO Auto-generated method stub

	}

	public void setConnection(String url) {
		// TODO Auto-generated method stub

	}

}
