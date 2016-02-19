package Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DB.DB;
import Interfaces.Connector;
import Utils.Identifier;
import Utils.MessageObject;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class TestDBConnector implements Connector{
	DB db = new DB();
	Long nextID = 3L;

	public Patient addPatient(MessageObject patient) {
		patient.messageObject.setId(new IdDt(nextID));
		db.myPatients.put(nextID, patient.messageObject);
		nextID++;
		return null;
	}

	public Patient updatePatient(Identifier id, MessageObject patient) {
		db.myPatients.put(id.identifier.getIdPartAsLong(), patient.messageObject);
		return null;
	}

	public Patient searchPatient(MessageObject patient) {
		IdDt patID = patient.messageObject.getId();
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
