package org.htl.adt.provider;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.CommunicationException;

import org.hibernate.HibernateException;
import org.htl.adt.connector.DBFactory;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.interfaces.Connector;
import org.htl.adt.interfaces.RestServer;
import org.htl.adt.restservlet.RestFactory;

import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.ExceptionCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class RestfulPatientResourceProvider implements IResourceProvider {

	private Map<Long, Patient> myPatients = new HashMap<Long, Patient>();
	private long nextID = 1L;

	// private RestServer restServer =
	// RestFactory.getInstance().getServer("TestServer");
	Connector db = DBFactory.getInstance().getConnector("DBConnector");
	// TestFHIRRestServlet myServlet = new TestFHIRRestServlet();
	// DB db = new DB();

	public Class<Patient> getResourceType() {
		return Patient.class;
	}

	public RestfulPatientResourceProvider() {
		long id = nextID++;
		Patient patient = new Patient();
		patient.setId(new IdDt(id));
		patient.addIdentifier().setSystem("http://test.com/Patient").setValue("1234");
		patient.addName().addFamily("Simpson").addGiven("Homer").addGiven("J");
		patient.setGender(AdministrativeGenderEnum.MALE);
		myPatients.put(id, patient);
	}

	@Read
	public Patient getResourceById(@IdParam IdDt patID) {
		/*
		 * Patient retValue = myPatients.get(patID.getIdPartAsLong()); if(patID
		 * == null){ throw new ResourceNotFoundException(patID); } return
		 * retValue;
		 */

		if (patID.isEmpty()) {
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("Ungültige ID wurde eingegeben");
			throw new InternalErrorException("Ungültige ID", oo);
		}
		Patient pat = new Patient();
		pat.setId(new IdDt(patID));
		PatientRequest patientRequest = new PatientRequest("Der zu suchende Patient", pat);
		// Patient retValue = new Patient();
		try {
			List<Patient> values = db.searchPatient(patientRequest);
			return values.get(0);
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException(
					"Der Patient mit der ID " + patID.getIdPart() + " wurde nicht gefunden");
		}
	}

	@Create
	public MethodOutcome create(@ResourceParam Patient patient) {
		if (patient != null) {
			PatientRequest patientRequest = new PatientRequest("Der anzulegende Patient", patient);
			db.addPatient(patientRequest);
			return new MethodOutcome(
					new IdDt("Patient", patient.getId().toString(), patient.getId().getVersionIdPart()));

			// OperationOutcome oo = new OperationOutcome();
			// oo.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDetails(patient.toString());
			// throw new InternalErrorException("Patient wurde angelegt", oo);
		} else {
			throw new InternalErrorException("Kein gültiger Patient eingegeben");
		}
		// return new MethodOutcome(patient.getId());
	}

	@Search
	public List<Patient> search(@RequiredParam(name = "family") StringParam familyName,
			@OptionalParam(name = "given") StringParam firstName,
			@OptionalParam(name = "identifier") StringParam patientIdentifier,
			@OptionalParam(name = "gender") StringParam patientGender) {

		/*
		 * List<Patient> retValue = new ArrayList<Patient>(); for (Patient next
		 * : myPatients.values()){ String familyName =
		 * next.getNameFirstRep().getFamilyAsSingleString().toLowerCase();
		 * if(!familyName.contains(theParam.getValue().toLowerCase())){
		 * continue; } retValue.add(next); } if(retValue.isEmpty()) throw new
		 * InternalErrorException("Patient mit diesem Nachnamn nicht vorhanden"
		 * ); return retValue;
		 */

		Map<String, String> params = new HashMap<String, String>();
		Patient pat = new Patient();
		pat.setId(new IdDt(1));

		if (familyName != null) {
			pat.addName().addFamily(familyName.getValue());
			params.put("familyName", familyName.getValue());
		}
		if (firstName != null) {
			pat.addName().addGiven(firstName.getValue());
			params.put("firstName", firstName.getValue());
		}
		if (patientIdentifier != null) {
			pat.addIdentifier().setValue(patientIdentifier.getValue());
			params.put("patientIdentifier", patientIdentifier.getValue());
			// throw new InternalErrorException("Identifier angekommen");
		}
		if (patientGender != null) {
			if (patientGender.getValue().equals("Male") || patientGender.equals("MALE")) {
				pat.setGender(AdministrativeGenderEnum.MALE);
				params.put("patientGender", "male");
			} else if (patientGender.getValue().equals("Female")) {
				pat.setGender(AdministrativeGenderEnum.FEMALE);
				params.put("patientGender", "female");
			} else if (patientGender.getValue().equals("Undefined")) {
				pat.setGender(AdministrativeGenderEnum.UNKNOWN);
				params.put("patientGender", "unknown");
			}
		}
		PatientRequest request = new PatientRequest(familyName.getValue(), pat);
		try {
			return db.searchPatientWithParameters(params);
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException("Es wurde kein Patient mit den eingegebenen Parametern gefunden.");
		}
	}

	@Search
	public List<Patient> search() {
		return db.getAllPatients();
	}

	@Update
	public MethodOutcome updatePatient(@IdParam IdDt id, @ResourceParam Patient patient) {
		if (patient != null) {
			PatientRequest patientRequest = new PatientRequest("Die neuen Patientendaten", patient);
			try {
				db.updatePatient(patientRequest);
				return new MethodOutcome(id);
			} catch (ResourceNotFoundException e) {
				throw new ResourceNotFoundException("Der zu aktualisierende Patient konnte nicht gefunden werden.");
			}
		} else {
			throw new InternalErrorException("Es wurde kein gültiger Patient eingegebe.");
		}
	}

}
