package org.htl.adt.db;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.IdDt;

public class TestDB {
	public Map<Long, Patient> myPatients = new HashMap<Long, Patient>();
	public Map<Long, Location> testLocations = new HashMap<Long, Location>();
	Long patID = 1L;
	Long locID = 1L;
	
	public TestDB() {
		Patient patientMax = new Patient();
		patientMax.setId(new IdDt(patID));
		patientMax.addIdentifier().setSystem("http://loinc.org").setValue("1234");
		patientMax.addName().addFamily("Mustermann").addGiven("Max").addGiven("M");
		patientMax.setGender(AdministrativeGenderEnum.MALE);
		myPatients.put(patID, patientMax);
		patID++;
		
		Patient patientFrida = new Patient();
		patientFrida.setId(new IdDt(patID));
		patientFrida.addIdentifier().setSystem("http://loinc.org").setValue("1111");
		patientFrida.addName().addFamily("Musterfrau").addGiven("Frida");
		patientFrida.setGender(AdministrativeGenderEnum.FEMALE);
		myPatients.put(patID, patientFrida);
		patID++;
		
		Location location = new Location();
		location.setId(new IdDt(locID));
		location.setDescription("Station: Tageschirurgie");
		testLocations.put(locID, location);
		locID++;
		Location location2 = new Location();
		location2.setId(new IdDt(locID));
		location2.setDescription("Station: Labor");
		testLocations.put(locID, location2);
		locID++;
	}
	
	public void addPatient(Patient patient){
		myPatients.put(patID, patient);
		patID++;
	}
	
	public Patient getPatient(IdDt patId){
		return myPatients.get(patId);
	}
	
	public void setPatient(Patient patient){
		myPatients.put(patient.getId().getIdPartAsLong(), patient);
	}
	
	public void addLocation(Location location) {
		testLocations.put(locID, location);
		locID++;
	}
	
	public void setLocation(Location location) {
		testLocations.put(location.getId().getIdPartAsLong(), location);
	}
	
	public Location getPatient(Location location) {
		return testLocations.get(location.getId().getIdPartAsLong());
	}
}
