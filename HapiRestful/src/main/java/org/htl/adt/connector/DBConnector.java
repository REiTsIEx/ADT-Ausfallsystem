package org.htl.adt.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.log4j.BasicConfigurator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.htl.adt.client.RestfulClient;
import org.htl.adt.domainobjects.EncounterRequest;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.LocationRequest;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.exception.AdtSystemErrorException;
import org.htl.adt.exception.CommunicationException;
import org.htl.adt.hibernateresources.DatabaseEncounter;
import org.htl.adt.hibernateresources.DatabaseLocation;
import org.htl.adt.hibernateresources.DatabasePatient;
import org.htl.adt.interfaces.Connector;

import com.mysql.fabric.xmlrpc.Client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;

public class DBConnector implements Connector {

	private Configuration config;
	private SessionFactory sessionFactory;
	RestfulClient client = new RestfulClient();
	public DBConnector() throws CommunicationException {
		try {

			BasicConfigurator.configure();

			config = new Configuration();
			config.configure("hibernate.cfg.xml");

			sessionFactory = config.buildSessionFactory();

		} catch (HibernateException e) {
			throw new CommunicationException("Error during the Creation of the Hibernate Configuration", e);
		}

	}

	public void addPatient(PatientRequest patientRequest) throws AdtSystemErrorException {
		// TODO Auto-generated method stub

		Session session = null;
		Transaction transaction = null;

		try {

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			DatabasePatient data = new DatabasePatient(patientRequest.getPatient());

			session.save(data);

			transaction.commit();// transaction is committed
			//client.createPatient(patientRequest.getPatient());
		} catch (IndexOutOfBoundsException e) {
			throw new AdtSystemErrorException(
					"Error during the reading of the Patient Parameters - IndexOutOfBoundException of one of the Parameters", e);
		} catch (NullPointerException e) {
			throw new AdtSystemErrorException("Error during the reading of the Patient Parameters - NullPointException of one of the Parameters", e);
		} catch (DataFormatException e) {
			throw new AdtSystemErrorException("Error during Parse Operation", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new AdtSystemErrorException("Error during insert Operation - had to rollback transaction!", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public List<Patient> searchPatientWithParameters(Map<String, String> patientParameter) throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;

		List<DatabasePatient> datalist = new ArrayList<DatabasePatient>();
		List<Patient> patientlist = new ArrayList<Patient>();

		try {

			FhirContext ctx = FhirContext.forDstu2();
			IParser parser = ctx.newXmlParser();

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			Criteria criteria = session.createCriteria(DatabasePatient.class);

			for (Map.Entry<String, String> cursor : patientParameter.entrySet()) {
				criteria.add(Restrictions.like(cursor.getKey(), cursor.getValue()));
			}

			datalist = criteria.list();

			for (DatabasePatient data : datalist) {
				Patient fhirPatient = parser.parseResource(Patient.class, data.getFhirMessage());
				patientlist.add(fhirPatient);
			}

			transaction.commit();// transaction is committed

		} catch (DataFormatException e) {
			throw new AdtSystemErrorException("Error during Parse Operation", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new AdtSystemErrorException("Error during select Operation - had to rollback transaction!", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return patientlist;
	}

	public List<Patient> getAllPatients() throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;

		List<DatabasePatient> datalist = new ArrayList<DatabasePatient>();
		List<Patient> patientlist = new ArrayList<Patient>();

		try {

			FhirContext ctx = FhirContext.forDstu2();
			IParser parser = ctx.newXmlParser();

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			datalist = session.createCriteria(DatabasePatient.class).list();

			for (DatabasePatient data : datalist) {
				Patient fhirPatient = parser.parseResource(Patient.class, data.getFhirMessage());
				patientlist.add(fhirPatient);
			}

			transaction.commit();// transaction is committed

		} catch (DataFormatException e) {
			throw new AdtSystemErrorException("Error during Parse Operation", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new AdtSystemErrorException("Error during select Operation - had to rollback transaction!", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return patientlist;
	}

	public void updatePatient(PatientRequest patientRequest) throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			// Identifier of the patient that should be updated
			IdDt patientId = patientRequest.getPatient().getId();

			List<DatabasePatient> selectValue = session.createCriteria(DatabasePatient.class)
					.add(Restrictions.like("patientIdentifier", patientId.getIdPart())).addOrder(Order.asc("patient_id")).list();

			if (!selectValue.isEmpty()) {
				// Occurs when patient already exists in the Database
				Patient insertPatient = patientRequest.getPatient();

				DatabasePatient insertValue = new DatabasePatient(insertPatient);

				insertValue.setPatient_id(selectValue.get(0).getPatient_id());

				session.merge(insertValue);

			} else {
				// Occurs when patient doesn't exists in the Database
				Patient insertPatient = patientRequest.getPatient();

				DatabasePatient insertValue = new DatabasePatient(insertPatient);

				session.save(insertValue);

			}

			transaction.commit();// transaction is committed
			//client.updatePatient(patientRequest.getPatient());
		} catch (IndexOutOfBoundsException e) {
			throw new AdtSystemErrorException(
					"Error during the reading of the Patient Parameters - IndexOutOfBoundException of one of the Parameters", e);
		} catch (NullPointerException e) {
			throw new AdtSystemErrorException("Error during the reading of the Patient Parameters - NullPointException of one of the Parameters", e);
		} catch (DataFormatException e) {
			throw new AdtSystemErrorException("Error during Parse Operation", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new AdtSystemErrorException("Error during insert Operation - had to rollback transaction!", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	public Patient getPatientbyIdentifier(PatientRequest patientRequest) throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;

		Patient returnPatient;

		try {

			FhirContext ctx = FhirContext.forDstu2();
			IParser parser = ctx.newXmlParser();

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			// Identifier of the patient that should be searched
			IdDt patientId = patientRequest.getPatient().getId();

			List<DatabasePatient> selectValue = session.createCriteria(DatabasePatient.class)
					.add(Restrictions.like("patientIdentifier", patientId.getIdPart()))
					.addOrder(Order.asc("patient_id"))
					.list();

			if (!selectValue.isEmpty()) {
				returnPatient = parser.parseResource(Patient.class, selectValue.get(0).getFhirMessage());
			} else {
				returnPatient = null;
			}

			transaction.commit();// transaction is committed

		} catch (IndexOutOfBoundsException e) {
			throw new AdtSystemErrorException(
					"Error during the reading of the Patient Parameters - IndexOutOfBoundException of one of the Parameters", e);
		} catch (NullPointerException e) {
			throw new AdtSystemErrorException("Error during the reading of the Patient Parameters - NullPointException of one of the Parameters", e);
		} catch (DataFormatException e) {
			throw new AdtSystemErrorException("Error during Parse Operation", e);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
			throw new AdtSystemErrorException("Error during insert Operation - had to rollback transaction!", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new AdtSystemErrorException("Error during insert Operation - had to rollback transaction!", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return returnPatient;
	}

	public void addEncounter(EncounterRequest encounterRequest) throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;

		try {

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			Encounter insertEncounter = encounterRequest.getEncounter();
			DatabaseEncounter data = new DatabaseEncounter(insertEncounter);

			session.save(data);

			transaction.commit();// transaction is committed

		} catch (IndexOutOfBoundsException e) {
			throw new AdtSystemErrorException(
					"Error during the reading of the Encounter Parameters - IndexOutOfBoundException of one of the Parameters", e);
		} catch (NullPointerException e) {
			throw new AdtSystemErrorException("Error during the reading of the Encounter Parameters - NullPointException of one of the Parameters", e);
		} catch (DataFormatException e) {
			throw new AdtSystemErrorException("Error during Parse Operation", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new AdtSystemErrorException("Error during insert Operation - had to rollback transaction!", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public List<Encounter> getEncounterbyPatientID(Identifier patientIdentifier) throws AdtSystemErrorException{
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;

		List<Encounter> encounterlist = new ArrayList<Encounter>();
		
		try {

			FhirContext ctx = FhirContext.forDstu2();
			IParser parser = ctx.newXmlParser();

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			IdDt patientId = patientIdentifier.getIdentifier();

			List<DatabaseEncounter> datalist = session.createCriteria(DatabaseEncounter.class)
					.add(Restrictions.like("patientIdentifier", patientId.getIdPart()))
					.addOrder(Order.asc("encounter_id"))
					.list();

			for (DatabaseEncounter encounter : datalist) {
				encounterlist.add(parser.parseResource(Encounter.class, encounter.getFhirMessage()));
			}			

			transaction.commit();// transaction is committed

		} catch (IndexOutOfBoundsException e) {
			throw new AdtSystemErrorException(
					"Error during the reading of the Patient Parameters - IndexOutOfBoundException of one of the Parameters", e);
		} catch (NullPointerException e) {
			throw new AdtSystemErrorException("Error during the reading of the Patient Parameters - NullPointException of one of the Parameters", e);
		} catch (DataFormatException e) {
			throw new AdtSystemErrorException("Error during Parse Operation", e);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
			throw new AdtSystemErrorException("Error during insert Operation - had to rollback transaction!", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new AdtSystemErrorException("Error during insert Operation - had to rollback transaction!", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return encounterlist;
	}

	public Encounter getLastEncounterbyPatientID(Identifier patientIdentifier) throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;

		Encounter returnEncounter = null;
		
		try {

			FhirContext ctx = FhirContext.forDstu2();
			IParser parser = ctx.newXmlParser();

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			List<Encounter> encounterlist = new ArrayList<Encounter>();
			IdDt patientId = patientIdentifier.getIdentifier();

			List<DatabaseEncounter> datalist = session.createCriteria(DatabaseEncounter.class)
					.add(Restrictions.like("patientIdentifier", patientId.getIdPart()))
					.addOrder(Order.asc("patient_id"))
					.list();

			for (DatabaseEncounter encounter : datalist) {
				encounterlist.add(parser.parseResource(Encounter.class, encounter.getFhirMessage()));
			}			
			
			//Nicht RICHTIG !!! Nur übergangslösung
			returnEncounter = encounterlist.get(0);
			
			transaction.commit();// transaction is committed

		} catch (IndexOutOfBoundsException e) {
			throw new AdtSystemErrorException(
					"Error during the reading of the Patient Parameters - IndexOutOfBoundException of one of the Parameters", e);
		} catch (NullPointerException e) {
			throw new AdtSystemErrorException("Error during the reading of the Patient Parameters - NullPointException of one of the Parameters", e);
		} catch (DataFormatException e) {
			throw new AdtSystemErrorException("Error during Parse Operation", e);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
			throw new AdtSystemErrorException("Error during insert Operation - had to rollback transaction!", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new AdtSystemErrorException("Error during insert Operation - had to rollback transaction!", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return returnEncounter;
	}

	public void addLocation(LocationRequest locationRequest) throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;

		try {

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			DatabaseLocation databaseLocation = new DatabaseLocation(locationRequest.getLocation());

			session.save(databaseLocation);

			transaction.commit();// transaction is committed

		} catch (IndexOutOfBoundsException e) {
			throw new AdtSystemErrorException(
					"Error during the reading of the Patient Parameters - IndexOutOfBoundException of one of the Parameters", e);
		} catch (NullPointerException e) {
			throw new AdtSystemErrorException("Error during the reading of the Patient Parameters - NullPointException of one of the Parameters", e);
		} catch (DataFormatException e) {
			throw new AdtSystemErrorException("Error during Parse Operation", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new AdtSystemErrorException("Error during insert Operation - had to rollback transaction!", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	public List<Location> getAllLocation() throws AdtSystemErrorException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;

		List<DatabaseLocation> databaselocations = new ArrayList<DatabaseLocation>();
		List<Location> locationlist = new ArrayList<Location>();

		try {

			FhirContext ctx = FhirContext.forDstu2();
			IParser parser = ctx.newXmlParser();

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			databaselocations = session.createCriteria(DatabaseLocation.class).list();

			for (DatabaseLocation dbLocation : databaselocations) {
				Location fhirLocation = parser.parseResource(Location.class, dbLocation.getFhirMessage());
				locationlist.add(fhirLocation);
			}

			transaction.commit();// transaction is committed

		} catch (DataFormatException e) {
			throw new AdtSystemErrorException("Error during Parse Operation", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new AdtSystemErrorException("Error during select Operation - had to rollback transaction!", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return locationlist;
	}

	public void deleteAllDatabaseRows() throws AdtSystemErrorException {
		Session session = null;
		Transaction transaction = null;
		try {

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			List<DatabaseEncounter> databaseEncounterList = session.createCriteria(DatabaseEncounter.class).list();
			for (DatabaseEncounter dbEncounter : databaseEncounterList) {
				session.delete(dbEncounter);
			}

			List<DatabasePatient> databasePatientList = session.createCriteria(DatabasePatient.class).list();
			for (DatabasePatient dbPatient : databasePatientList) {
				session.delete(dbPatient);
			}

			List<DatabaseLocation> databaseLocationList = session.createCriteria(DatabaseLocation.class).list();
			for (DatabaseLocation dbLocation : databaseLocationList) {
				session.delete(dbLocation);
			}

			transaction.commit();// transaction is committed

		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new AdtSystemErrorException("Error during delete Operation - had to rollback transaction!", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void getConnection() {
		// TODO Auto-generated method stub

	}

	public void setConnection(String url) {
		// TODO Auto-generated method stub

	}

}
