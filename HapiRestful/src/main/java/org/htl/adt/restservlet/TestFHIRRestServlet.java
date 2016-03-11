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
		try {
			return db.getAllPatients();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void addPatient(PatientRequest patient) {
		try {
			db.addPatient(patient);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Patient searchPatientWithID(PatientRequest patient) {
		try {
			return db.searchPatient(patient).get(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Patient> searchPatient(PatientRequest patient) {
		List<Patient> patients = null;
		try {
			patients.add(db.searchPatient(patient).get(0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return patients;
	}

	public void updatePatient(Identifier id, PatientRequest patient) {
		try {
			db.updatePatient(id, patient);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void movePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub

	}

	public void releasePatient(Identifier id, PatientRequest patient) {
		// TODO Auto-generated method stub

	}
	
	public List<Patient> searchPatientWithFamily(PatientRequest patient){
		try {
			return db.searchPatientWithFamily(patient);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
