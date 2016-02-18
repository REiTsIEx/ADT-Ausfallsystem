package Interfaces;

import java.util.List;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public interface Connector {
	public Patient addPatient(Patient patient);
	
	public Patient updatePatient(IdDt id, Patient patient);

	public Patient searchPatient(Patient patient);
	
	public List<Patient> getAllPatients();
	
	public void getConnection();
	
	public void setConnection(String url);
}
