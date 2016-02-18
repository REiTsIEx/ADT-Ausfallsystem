package Interfaces;

import java.util.List;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public interface RestServer {
	public List<Patient>  getAllPatient();
	
	public void addPatient (Patient patient); 

	public List<Patient> searchPatient(Patient patient);
	
	public void updatePatient(IdDt id, Patient patient);

	public void movePatient(IdDt id, Patient patient);
	
	public void releasePatient(IdDt id, Patient patient);

	public Patient searchPatientOK(Patient pat);
	
}
