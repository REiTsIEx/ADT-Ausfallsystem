package org.htl.adt.connector;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.htl.adt.domainobjects.DatabasePatient;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.interfaces.Connector;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;

public class DBConnector implements Connector {

	private Configuration config;
	private SessionFactory sessionFactory;

	public DBConnector() {
		try {

			BasicConfigurator.configure();

			config = new Configuration();
			config.configure("hibernate.cfg.xml");

			sessionFactory = config.buildSessionFactory();

		} catch (HibernateException e) {
			throw new RuntimeException(
					"Error during the Creation of the Hibernate Configuration",
					e);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.htl.adt.interfaces.Connector#addPatient(org.htl.adt.domainobjects
	 * .PatientRequest)
	 */

	public void addPatient(PatientRequest patientRequest) {
		// TODO Auto-generated method stub

		Session session = null;
		Transaction transaction = null;

		try {

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			FhirContext ctx = FhirContext.forDstu2();

			Patient insertPatient = patientRequest.getPatient();

			String patientIdentifier = insertPatient.getId().getIdPart();
			String patientLastName = insertPatient.getNameFirstRep().getFamilyAsSingleString();
			String patientFirstName = insertPatient.getNameFirstRep().getGiven().get(0).getValue();
			String fhirMessage = ctx.newXmlParser().encodeResourceToString(insertPatient);

			DatabasePatient data = new DatabasePatient(patientIdentifier, patientFirstName, patientLastName, fhirMessage);

			session.save(data);

			transaction.commit();// transaction is committed

		} catch (IndexOutOfBoundsException e) {
			throw new RuntimeException(
					"Error during the reading of the Patient Parameters - IndexOutOfBoundException of one of the Parameters",
					e);
		} catch (NullPointerException e) {
			throw new RuntimeException(
					"Error during the reading of the Patient Parameters - NullPointException of one of the Parameters",
					e);
		} catch (DataFormatException e) {
			throw new RuntimeException("Error during Parse Operation", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new RuntimeException(
					"Error during insert Operation - had to rollback transaction!",
					e);
		}
	}

	public List<Patient> searchPatient(PatientRequest patient) {
		return null;
		// Test
	}

	public List<Patient> getAllPatients() {
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
				Patient fhirPatient = parser.parseResource(Patient.class,
						data.getFhirMessage());
				patientlist.add(fhirPatient);
			}

			transaction.commit();// transaction is committed

		} catch (DataFormatException e) {
			throw new RuntimeException("Error during Parse Operation", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new RuntimeException(
					"Error during select Operation - had to rollback transaction!",	e);
		}
		return patientlist;
	}

	public void updatePatient(PatientRequest patient) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;

		try {
			FhirContext ctx = FhirContext.forDstu2();
			
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();		
			
			IdDt patientId = patient.getPatient().getId();		//Identifier of the patient that should be updated

			List<DatabasePatient> selectValue = session
					.createCriteria(DatabasePatient.class)
					.add(Restrictions.like("identifier", patientId.getIdPart()))
					.addOrder(Order.asc("patient_id"))
					.list();

			if (!selectValue.isEmpty()) {
				//Occurs when patient already exists in the Database
				Patient insertPatient = patient.getPatient();
				
				String patientIdentifier = insertPatient.getId().getIdPart();
				String patientLastName = insertPatient.getNameFirstRep().getFamilyAsSingleString();
				String patientFirstName = insertPatient.getNameFirstRep().getGiven().get(0).getValue();
				String fhirMessage = ctx.newXmlParser().encodeResourceToString(insertPatient);

				DatabasePatient insertValue = new DatabasePatient(patientIdentifier, patientFirstName, patientLastName, fhirMessage);

				insertValue.setPatient_id(selectValue.get(0).getPatient_id());

				session.merge(insertValue);

			} else {
				//Occurs when patient doesn't exists in the Database
				Patient insertPatient = patient.getPatient();

				String patientIdentifier = insertPatient.getId().getIdPart();
				String patientLastName = insertPatient.getNameFirstRep().getFamilyAsSingleString();
				String patientFirstName = insertPatient.getNameFirstRep().getGiven().get(0).getValue();
				String fhirMessage = ctx.newXmlParser().encodeResourceToString(insertPatient);

				DatabasePatient insertValue = new DatabasePatient(patientIdentifier, patientFirstName, patientLastName, fhirMessage);

				session.save(insertValue);

			}

			transaction.commit();// transaction is committed
		} catch (IndexOutOfBoundsException e) {
			throw new RuntimeException(
					"Error during the reading of the Patient Parameters - IndexOutOfBoundException of one of the Parameters", e);
		} catch (NullPointerException e) {
			throw new RuntimeException(
					"Error during the reading of the Patient Parameters - NullPointException of one of the Parameters",	e);	
		} catch (DataFormatException e) {
			throw new RuntimeException("Error during Parse Operation", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new RuntimeException(
					"Error during insert Operation - had to rollback transaction!",	e);
		}

	}

	public List<Patient> searchPatientWithFamily(PatientRequest patientRequest) {
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

			String criteriaLastName = patientRequest.getPatient()
					.getNameFirstRep().getFamilyAsSingleString();

			datalist = session.createCriteria(DatabasePatient.class)
					.add(Restrictions.like("lastName", criteriaLastName))
					.addOrder(Order.asc("patient_id")).list();

			for (DatabasePatient data : datalist) {
				Patient fhirPatient = parser.parseResource(Patient.class,
						data.getFhirMessage());
				patientlist.add(fhirPatient);
			}

			transaction.commit();// transaction is committed

		} catch (DataFormatException e) {
			throw new RuntimeException("Error during Parse Operation", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new RuntimeException(
					"Error during insert Operation - had to rollback transaction!",
					e);
		}

		return patientlist;
	}

	public Patient getPatientbyIdentifier(PatientRequest patient) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;

		Patient returnPatient; 
		
		try {
			
			FhirContext ctx = FhirContext.forDstu2();
			IParser parser = ctx.newXmlParser();
			
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();		
						
			IdDt patientId = patient.getPatient().getId(); //Identifier of the patient that should be searched
			
			List<DatabasePatient> selectValue = session
					.createCriteria(DatabasePatient.class)
					.add(Restrictions.like("identifier", patientId.getIdPart()))
					.addOrder(Order.asc("patient_id"))
					.list();
			
			if(!selectValue.isEmpty()){
				returnPatient = parser.parseResource(Patient.class, selectValue.get(0).getFhirMessage());
			}else{
				returnPatient = null;
			}

			transaction.commit();// transaction is committed
			
		} catch (IndexOutOfBoundsException e) {
			throw new RuntimeException(
					"Error during the reading of the Patient Parameters - IndexOutOfBoundException of one of the Parameters", e);
		} catch (NullPointerException e) {
			throw new RuntimeException(
					"Error during the reading of the Patient Parameters - NullPointException of one of the Parameters",	e);	
		} catch (DataFormatException e) {
			throw new RuntimeException("Error during Parse Operation", e);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new RuntimeException(
					"Error during insert Operation - had to rollback transaction!",	e);
		}
		
			return returnPatient;
	}
	
	public void addEncounter(PatientRequest patient, Encounter encounter) {
		// TODO Auto-generated method stub
		
	}

	
	public void getConnection() {
		// TODO Auto-generated method stub

	}

	public void setConnection(String url) {
		// TODO Auto-generated method stub

	}


}
