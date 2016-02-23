package org.htl.adtausfallsystem.restserverlet;

import java.util.List;

import org.htl.adtausfallsystem.Interfaces.RestServer;

import Utils.Identifier;
import Utils.MessageObject;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class FHIRRestServlet implements RestServer{

	public List<Patient> getAllPatient() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addPatient(MessageObject patient) {
		// TODO Auto-generated method stub
		
	}

	public List<Patient> searchPatient(MessageObject patient) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updatePatient(Identifier id, MessageObject patient) {
		// TODO Auto-generated method stub
		
	}

	public void movePatient(Identifier id, MessageObject patient) {
		// TODO Auto-generated method stub
		
	}

	public void releasePatient(Identifier id, MessageObject patient) {
		// TODO Auto-generated method stub
		
	}

	public Patient searchPatientOK(MessageObject pat) {
		// TODO Auto-generated method stub
		return null;
	}

}
