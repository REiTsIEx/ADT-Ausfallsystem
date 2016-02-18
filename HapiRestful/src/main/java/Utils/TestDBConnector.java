package Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Interfaces.Connector;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class TestDBConnector implements Connector{
	DB db = new DB();
	Long nextID = 3L;

	public Patient addPatient(Patient patient) {
		patient.setId(new IdDt(nextID));
		db.myPatients.put(nextID, patient);
		nextID++;
		return null;
	}

	public Patient updatePatient(IdDt id, Patient patient) {
		db.myPatients.put(id.getIdPartAsLong(), patient);
		return null;
	}

	public Patient searchPatient(Patient patient) {
		IdDt patID = patient.getId();
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
