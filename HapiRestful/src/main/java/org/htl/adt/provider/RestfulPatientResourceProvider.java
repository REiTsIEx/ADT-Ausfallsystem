package org.htl.adt.provider;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.hibernate.HibernateException;
import org.htl.adt.client.RestfulClient;
import org.htl.adt.connector.DBFactory;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.exception.AdtSystemErrorException;
import org.htl.adt.exception.CommunicationException;
import org.htl.adt.interfaces.Connector;
import org.htl.adt.interfaces.RestServer;

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
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class RestfulPatientResourceProvider implements IResourceProvider {

	Connector db;
	RestfulClient client = new RestfulClient();
	
	public Class<Patient> getResourceType() {
		return Patient.class;
	}

	public RestfulPatientResourceProvider() {
		try {
			db = DBFactory.getInstance().getConnector("DBConnector");
		} catch (CommunicationException e) {
			throw new InternalErrorException("Es konnte keine Verbindung mit der Datenbank hergestellt werden");
		}
	}

	/**
	 * Sucht einen Patienten mit dem eindeutigen Identifier in der Datenbank
	 * @param patientID (der Identifier des Patienten)
	 * @return Die Patientenressource, die diesen Identifier hat
	 */
	@Read
	public Patient getResourceById(@IdParam IdDt patientID) {
		if (patientID.isEmpty()) {
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("Ungültige ID wurde eingegeben");
			throw new InternalErrorException("Ungültige ID", oo);
		}
		Patient patient = new Patient();
		patient.setId(new IdDt(patientID.getIdPart()));
		PatientRequest patientRequest = new PatientRequest("", patient);
		try {
			return db.getPatientbyIdentifier(patientRequest);
		} catch (AdtSystemErrorException e) {
			throw new ResourceNotFoundException(
					"Der Patient mit der ID " + patientID.getIdPart() + " wurde nicht gefunden");
		}
	}

	/**
	 * Legt einen neuen Patient in der Datenbank an
	 * @param patient (der neu zu erzeugende Patient)
	 * @return
	 */
	@Create
	public MethodOutcome create(@ResourceParam Patient patient) {
		if (patient != null) {
			try{
			PatientRequest patientRequest = new PatientRequest("Der anzulegende Patient", patient);
			db.addPatient(patientRequest);
			client.createPatient(patient);
			return new MethodOutcome(
					new IdDt("Patient", patient.getId().toString(), patient.getId().getVersionIdPart()));
			} catch (AdtSystemErrorException e){
				throw new ResourceNotFoundException("Der Patient konnte nich angelegt werden");
			}
		} else {
			throw new ResourceNotFoundException("Kein gültiger Patient eingegeben");
		}
		// return new MethodOutcome(patient.getId());
	}

	/**
	 * Sucht einen Patienten in der Datenbank mit mehreren Parametern,
	 * Unterstützt die Parameter die die Weboberfläche vorgibt,
	 * Nur der Nachname muss übergeben werden, die anderen Werte sind optional
	 * @param familyName
	 * @param firstName
	 * @param patientIdentifier
	 * @param patientGender
	 * @return
	 */
	@Search
	public List<Patient> searchWithRequiredFamilyName(@RequiredParam(name = "family") StringParam familyName,
			@OptionalParam(name = Patient.SP_GIVEN) StringParam firstName,
			@OptionalParam(name = Patient.SP_IDENTIFIER) StringParam patientIdentifier,
			@OptionalParam(name = Patient.SP_GENDER) StringParam patientGender) {
			
		Map<String, String> params = new HashMap<String, String>();
		Patient pat = new Patient();
		pat.setId(new IdDt(1));

		if (familyName != null || familyName.getValue() == " ") {
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
		} catch (AdtSystemErrorException e) {
			throw new ResourceNotFoundException("Es wurde kein Patient mit den eingegebenen Parametern gefunden.");
		}
	}


	/**
	 * Übergibt alle Patienten die sich in der Datenbank befinden zurück
	 * @return List<Patient>
	 */
	@Search
	public List<Patient> getAllPatients() {
		try {
			return db.getAllPatients();
		} catch (AdtSystemErrorException e) {
			throw new ResourceNotFoundException("Es konnten keine Patienten in der Datenbank gefunden werden");
		} 
	}
	
	/**
	 * Mithilfe des Identifiers, wird ein Patient aktualisiert; ist er nicht vorhanden, wird er neu angelegt
	 * @param id (Identiefier des zu aktualiserenden Patienten)
	 * Patient patient (die neuen Patientendaten)
	 * @param patient
	 * @return
	 */
	@Update
	public MethodOutcome updatePatient(@IdParam IdDt id, @ResourceParam Patient patient) {
		if (patient != null) {
			PatientRequest patientRequest = new PatientRequest("Die neuen Patientendaten", patient);
			try {
				db.updatePatient(patientRequest);
				client.updatePatient(patient);
				return new MethodOutcome(id);
			} catch (AdtSystemErrorException e) {
				throw new ResourceNotFoundException("Der zu aktualisierende Patient konnte nicht gefunden werden.");
			}
		} else {
			throw new ResourceNotFoundException("Es wurde kein gültiger Patient eingegebe.");
		}
	}
	
	
}
