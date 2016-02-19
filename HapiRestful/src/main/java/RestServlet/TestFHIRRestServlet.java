package RestServlet;

import java.util.ArrayList;
import java.util.List;

import Connector.DBFactory;
import Interfaces.Connector;
import Interfaces.RestServer;
import Utils.Identifier;
import Utils.PatientRequest;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class TestFHIRRestServlet implements RestServer {

	DBFactory factory = new DBFactory();
	Connector db = factory.getConnector("TestDBConnector");

	//TestDBConnector db = new TestDBConnector();

	public List<Patient> getAllPatient() {
		return db.getAllPatients();
	}

	public void addPatient(PatientRequest patient) {
		db.addPatient(patient);
	}
	
	public Patient searchPatientOK(PatientRequest patient) {
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

}
