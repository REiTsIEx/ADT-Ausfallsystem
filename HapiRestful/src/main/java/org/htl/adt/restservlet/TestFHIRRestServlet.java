package org.htl.adt.restservlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.htl.adt.connector.DBConnector;
import org.htl.adt.connector.DBFactory;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.interfaces.Connector;
import org.htl.adt.interfaces.RestServer;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class TestFHIRRestServlet implements RestServer {

	Connector db = DBFactory.getInstance().getConnector("TestDBConnector");
	//DBConnector db = new DBConnector();
	//TestDBConnector db = new TestDBConnector();

	public List<Patient> getAllPatient() {
		return db.getAllPatients();
		
	}

	public void addPatient(PatientRequest patientRequest) {
		db.addPatient(patientRequest);
	}
	
	public Patient searchPatientWithID(PatientRequest patientRefactor) {
		return db.searchPatient(patientRefactor).get(0);
	}
	
	public List<Patient> searchPatient(PatientRequest patientRequest) {
		List<Patient> patients = null;
		patients.add(db.searchPatient(patientRequest).get(0));
		return patients;
	}

	public void updatePatient(Identifier id, PatientRequest patientRequest) {
		db.updatePatient(patientRequest);

	}

	public void movePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub

	}

	public void releasePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub

	}
	
	public List<Patient> searchPatientWithFamily(PatientRequest patient){
		//return db.searchPatientWithFamily(patient);
		return null;
	}

}
