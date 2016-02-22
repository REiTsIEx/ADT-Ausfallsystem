package org.htl.adtausfallsystem.restserverlet;

import java.util.ArrayList;
import java.util.List;

import org.htl.adtausfallsystem.Interfaces.Connector;
import org.htl.adtausfallsystem.Interfaces.RestServer;
import org.htl.adtausfallsystem.connector.DBFactory;

import Utils.Identifier;
import Utils.MessageObject;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class TestFHIRRestServlet implements RestServer {

	DBFactory factory = new DBFactory();
	Connector db = factory.getConnector("TestDBConnector");

	//TestDBConnector db = new TestDBConnector();

	public List<Patient> getAllPatient() {
		return db.getAllPatients();
	}

	public void addPatient(MessageObject patient) {
		db.addPatient(patient);
	}
	
	public Patient searchPatientOK(MessageObject patient) {
		return db.searchPatient(patient);
	}
	
	public List<Patient> searchPatient(MessageObject patient) {
		List<Patient> patients = null;
		patients.add(db.searchPatient(patient));
		return patients;
	}

	public void updatePatient(Identifier id, MessageObject patient) {
		db.updatePatient(id, patient);

	}

	public void movePatient(Identifier id, MessageObject patient) {
		// TODO Auto-generated method stub

	}

	public void releasePatient(Identifier id, MessageObject patient) {
		// TODO Auto-generated method stub

	}

}
