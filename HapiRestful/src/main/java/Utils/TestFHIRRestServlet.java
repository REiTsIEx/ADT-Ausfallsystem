package Utils;

import java.util.ArrayList;
import java.util.List;

import Interfaces.Connector;
import Interfaces.RestServer;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class TestFHIRRestServlet implements RestServer {

	DBFactory factory = new DBFactory();
	Connector db = factory.getConnector("TestDBConnector");

	//TestDBConnector db = new TestDBConnector();

	public List<Patient> getAllPatient() {
		return db.getAllPatients();
	}

	public void addPatient(Patient patient) {
		db.addPatient(patient);
	}
	
	public Patient searchPatientOK(Patient patient) {
		return db.searchPatient(patient);
	}
	
	public List<Patient> searchPatient(Patient patient) {
		List<Patient> patients = null;
		patients.add(db.searchPatient(patient));
		return patients;
	}

	public void updatePatient(IdDt id, Patient patient) {
		db.updatePatient(id, patient);

	}

	public void movePatient(IdDt id, Patient patient) {
		// TODO Auto-generated method stub

	}

	public void releasePatient(IdDt id, Patient patient) {
		// TODO Auto-generated method stub

	}

}
