package Interfaces;

import java.util.List;

import Utils.Identifier;
import Utils.PatientRequest;
import ca.uhn.fhir.model.dstu2.resource.Patient;

public interface Connector {
	public Patient addPatient(PatientRequest patient);
	
	public Patient updatePatient(Identifier id, PatientRequest patient);

	public Patient searchPatient(PatientRequest patient);
	
	public List<Patient> getAllPatients();
	
	public void getConnection();
	
	public void setConnection(String url);
}
