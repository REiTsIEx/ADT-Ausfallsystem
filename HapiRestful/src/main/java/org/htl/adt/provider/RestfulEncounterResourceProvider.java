package org.htl.adt.provider;

import java.util.LinkedList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.htl.adt.client.RestfulClient;

import com.mysql.fabric.xmlrpc.Client;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Encounter.Location;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;

public class RestfulEncounterResourceProvider implements IResourceProvider{
	
	RestfulClient client = new RestfulClient();
	
	public Class<? extends IBaseResource> getResourceType() {
		return Encounter.class;
	}

	public RestfulEncounterResourceProvider() {
		
	}
	
	@Search
	public Encounter getEncounter(@RequiredParam(name="id") StringParam encounterID){
		//db.searchEncounter
		return null;
	}
	
	@Search
	public List<Encounter> getAllEncounter(){
		LinkedList<Encounter> listE = new LinkedList<Encounter>();
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
		return listE;
	}
	
}
