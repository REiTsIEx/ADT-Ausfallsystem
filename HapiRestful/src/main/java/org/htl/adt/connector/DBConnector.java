package org.htl.adt.connector;

import java.io.IOException;
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
import org.htl.adt.client.RestfulClient;
import org.htl.adt.domainobjects.DatabasePatient;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.interfaces.Connector;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class DBConnector implements Connector {
	private RestfulClient client = new RestfulClient();
	private Configuration config;
	private SessionFactory sessionFactory;

	public DBConnector() {
		BasicConfigurator.configure();

		config = new Configuration();
		config.configure("hibernate.cfg.xml");

		sessionFactory = config.buildSessionFactory();
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
			String fhirMessage = ctx.newXmlParser().encodeResourceToString(
					patientRequest.getPatient());

			DatabasePatient data = new DatabasePatient(patientRequest
					.getPatient().getId().getIdPart(), patientRequest
					.getPatient().getNameFirstRep().getGiven().get(1)
					.getValueAsString(), patientRequest.getPatient()
					.getNameFirstRep().getFamilyAsSingleString(), fhirMessage);

			session.save(data);

			transaction.commit();// transaction is committed

			client.createPatient(patientRequest.getPatient());

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
					"Error during insert Operation - had to rollback transaction!", e);
		} catch (BaseServerResponseException e) {
			throw new RuntimeException(
					"Error during during the create Patient of the Client", e);
		}
	}

	public List<Patient> searchPatient(PatientRequest patient) {
		return null;
	}

	public List<Patient> getAllPatients() {
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
					"Error during insert Operation - had to rollback transaction!",	e);
		} catch (BaseServerResponseException e) {
			throw new RuntimeException(
					"Error during the create Patient of the Client", e);
		}
		return patientlist;
	}

	public void updatePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;

		try {

			session = sessionFactory.openSession();

			transaction = session.beginTransaction();

			FhirContext ctx = FhirContext.forDstu2();
			String fhirMessage = ctx.newXmlParser().encodeResourceToString(
					patient.getPatient());

			List<DatabasePatient> selectValue = session
					.createCriteria(DatabasePatient.class)
					.add(Restrictions.like("identifier", id.getIdentifier()
							.getIdPart())).addOrder(Order.asc("patient_id"))
					.list();

			if (!selectValue.isEmpty()) {

				DatabasePatient insertValue = new DatabasePatient(patient
						.getPatient().getId().toString(), patient.getPatient()
						.getNameFirstRep().getGivenAsSingleString(), patient
						.getPatient().getNameFirstRep()
						.getFamilyAsSingleString(), fhirMessage);

				System.out.println("Update");
				insertValue.setPatient_id(selectValue.get(0).getPatient_id());

				session.merge(insertValue);

			} else {

				DatabasePatient insertValue = new DatabasePatient(patient
						.getPatient().getId().toString(), patient.getPatient()
						.getNameFirstRep().getGivenAsSingleString(), patient
						.getPatient().getNameFirstRep()
						.getFamilyAsSingleString(), fhirMessage);

				System.out.println("Insert");
				session.save(insertValue);

			}

			transaction.commit();// transaction is committed
			client.updatePatient(patient.getPatient());
		} 
		catch(DataFormatException e) {
			throw new RuntimeException("Error during Parse Operation", e);
		}
		catch(HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
			throw new RuntimeException("Error during insert Operation - had to rollback transaction!", e);
		}
		catch(BaseServerResponseException e){
			throw new RuntimeException("Error during the update Patient of the Client", e);
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
					"Error during insert Operation - had to rollback transaction!", e);
		} catch (BaseServerResponseException e) {
			throw new RuntimeException(
					"Error during during the create Patient of the Client", e);
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
