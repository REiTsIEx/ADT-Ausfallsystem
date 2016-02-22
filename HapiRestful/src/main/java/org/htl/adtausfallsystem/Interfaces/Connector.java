package org.htl.adtausfallsystem.Interfaces;

import java.util.List;

import Utils.Identifier;
import Utils.MessageObject;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public interface Connector {
	public Patient addPatient(MessageObject patient);
	
	public Patient updatePatient(Identifier id, MessageObject patient);

	public Patient searchPatient(MessageObject patient);
	
	public List<Patient> getAllPatients();
	
	public void getConnection();
	
	public void setConnection(String url);
}
