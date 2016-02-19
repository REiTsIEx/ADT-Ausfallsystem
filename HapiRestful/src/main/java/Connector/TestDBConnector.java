package Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DB.DB;
import Interfaces.Connector;
import Utils.Identifier;
import Utils.PatientRequest;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class TestDBConnector implements Connector{
	DB db = new DB();
	Long nextID = 3L;

	public Patient addPatient(PatientRequest patient) {
		patient.patient.setId(new IdDt(nextID));
		db.myPatients.put(nextID, patient.patient);
		nextID++;
		return null;
	}

	public Patient updatePatient(Identifier id, PatientRequest patient) {
		db.myPatients.put(id.identifier.getIdPartAsLong(), patient.patient);
		return null;
	}

	public Patient searchPatient(PatientRequest patient) {
		IdDt patID = patient.patient.getId();
		Patient retValue = db.myPatients.get(patID.getIdPartAsLong());
		if(patID == null){
			throw new ResourceNotFoundException(patID);
		}
		return retValue;
	}
	
	public List<Patient> getAllPatients() {
		List<Patient> retValue = new ArrayList<Patient>();
		retValue.addAll(db.myPatients.values());
		return retValue;
	}

	public void getConnection() {
		// TODO Auto-generated method stub
		
	}

	public void setConnection(String url) {
		// TODO Auto-generated method stub
		
	}

}
