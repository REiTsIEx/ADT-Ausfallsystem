package Utils;

import java.util.List;

import Interfaces.RestServer;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class FHIRRestServlet implements RestServer{

	public List<Patient> getAllPatient() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addPatient(Patient patient) {
		// TODO Auto-generated method stub
		
	}

	public List<Patient> searchPatient(Patient patient) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updatePatient(IdDt id, Patient patient) {
		// TODO Auto-generated method stub
		
	}

	public void movePatient(IdDt id, Patient patient) {
		// TODO Auto-generated method stub
		
	}

	public void releasePatient(IdDt id, Patient patient) {
		// TODO Auto-generated method stub
		
	}

	public Patient searchPatientOK(Patient pat) {
		// TODO Auto-generated method stub
		return null;
	}

}
