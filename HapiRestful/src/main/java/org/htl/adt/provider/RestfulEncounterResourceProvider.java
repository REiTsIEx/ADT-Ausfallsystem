package org.htl.adt.provider;

import java.util.LinkedList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.htl.adt.client.RestfulClient;
import org.htl.adt.connector.DBConnector;
import org.htl.adt.connector.DBFactory;
import org.htl.adt.domainobjects.EncounterRequest;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.exception.AdtSystemErrorException;
import org.htl.adt.exception.CommunicationException;
import org.htl.adt.interfaces.Connector;

import com.mysql.fabric.xmlrpc.Client;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Encounter.Location;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
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

public class RestfulEncounterResourceProvider implements IResourceProvider{
	
	RestfulClient client = new RestfulClient();
	Connector db;
	
	public Class<? extends IBaseResource> getResourceType() {
		return Encounter.class;
	}

	public RestfulEncounterResourceProvider() {
		try {
			db = DBFactory.getInstance().getConnector("DBConnector");
		} catch (CommunicationException e) {
			throw new InternalErrorException("Es konnte keine Verbindung mit der Datenbank hergestellt werden");
		}
	}
	
	
	@Search
	public List<Encounter> getAllEncounter(){
		/*LinkedList<Encounter> listE = new LinkedList<Encounter>();
		Encounter encounter = new Encounter();
		encounter.setId(new IdDt(1));
		Patient patientToRead = new Patient();
		patientToRead.setId(new IdDt(1));
		patientToRead.addName().addFamily("Reiter");
		List<Patient> allPatients = client.searchPatient(patientToRead);
		//Patient patient = allPatients.getFirst();
		ResourceReferenceDt ref = new ResourceReferenceDt(allPatients.get(0));
		encounter.setPatient(ref);
		encounter.setLanguage(new CodeDt("German"));
		
		Location location = new Location();
		ca.uhn.fhir.model.dstu2.resource.Location realLocation = new ca.uhn.fhir.model.dstu2.resource.Location();
		realLocation.setId(new IdDt(1));
		location.setLocation(new ResourceReferenceDt(client.readLocationWithID(realLocation)));
		encounter.addLocation(location);
		listE.add(encounter);
		return listE;*/
		
		LinkedList<Encounter> listE = new LinkedList<Encounter>();
		Encounter encounter = new Encounter();
		encounter.setId(new IdDt(1));
		Patient patientToRead = new Patient();
		patientToRead.setId(new IdDt(1));

		encounter.setLanguage(new CodeDt("German"));
		
		listE.add(encounter);
		return listE;
	}
	
	@Search
	public List<Encounter> getEncounterWithPatientID(@RequiredParam(name = "patient") StringParam patientID){
		Identifier id = new Identifier();
		id.setIdentifier(new IdDt(patientID.getValue()));
		try {
			return db.getEncounterbyPatientID(id);
		} catch (AdtSystemErrorException e) {
			throw new InternalErrorException("Es gab einen Fehler bei der Eingabe, bitte wiederholen Sie diese");
		}
		
	}
	
	@Search
	public Encounter getLastEncounter(){
		return null;
	}
	
	@Read
	public Encounter getEncounterByID(@IdParam IdDt encounterID) {
		return null;
	}
	
	@Update
	public MethodOutcome updateEncounter(@IdParam IdDt id, @ResourceParam Encounter encounter) {
		return null;
	}
	
	@Create
	public MethodOutcome createEncounter(@ResourceParam Encounter encounter) {
		try {
			EncounterRequest encounterRequest = new EncounterRequest("", encounter);
			db.addEncounter(encounterRequest);
		} catch (AdtSystemErrorException e) {
			throw new ResourceNotFoundException("Fehler bei der Eingabe");
		}
		return new MethodOutcome(encounter.getId());
	}
}
