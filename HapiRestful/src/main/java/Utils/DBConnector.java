package Utils;

import java.util.List;

import Interfaces.Connector;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class DBConnector implements Connector {

	public Patient addPatient(Patient patient) {
		// TODO Auto-generated method stub
		return null;
	}


	public Patient searchPatient(Patient patient) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Patient> getAllPatients() {
		// TODO Auto-generated method stub
		return null;
	}

	public void getConnection() {
		// TODO Auto-generated method stub
		
	}

	public void setConnection(String url) {
		// TODO Auto-generated method stub
		
	}

	public Patient updatePatient(IdDt id, Patient patient) {
		// TODO Auto-generated method stub
		return null;
	}

}
