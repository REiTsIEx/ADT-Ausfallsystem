package org.htl.adtausfallsystem.Interfaces;

import java.util.List;

import Utils.Identifier;
import Utils.MessageObject;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public interface RestServer {
	public List<Patient>  getAllPatient();
	
	public void addPatient (MessageObject patient); 

	public List<Patient> searchPatient(MessageObject patient);
	
	public void updatePatient(Identifier id, MessageObject patient);

	public void movePatient(Identifier id, MessageObject patient);
	
	public void releasePatient(Identifier id, MessageObject patient);

	public Patient searchPatientOK(MessageObject pat);
	
}
