package org.htl.adt.restservlet;

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

	public void addPatient(PatientRequest patient) {
		db.addPatient(patient);
	}
	
	public Patient searchPatientWithID(PatientRequest patient) {
		return db.searchPatient(patient);
	}
	
	public List<Patient> searchPatient(PatientRequest patient) {
		List<Patient> patients = null;
		patients.add(db.searchPatient(patient));
		return patients;
	}

	public void updatePatient(Identifier id, PatientRequest patient) {
		db.updatePatient(id, patient);

	}

	public void movePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub

	}

	public void releasePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub

	}
	
	public List<Patient> searchPatientWithFamily(PatientRequest patient){
		return db.searchPatientWithFamily(patient);
	}

}
