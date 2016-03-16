package org.htl.adt.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htl.adt.client.RestfulClient;
import org.htl.adt.db.TestDB;
import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.interfaces.Connector;

import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class TestDBConnector implements Connector{
	TestDB db = new TestDB();
	Long nextID = 3L;
	RestfulClient client = new RestfulClient();

	public void addPatient(PatientRequest patient) {
		//IdDt newPatientID = new IdDt();
		//newPatientID.withVersion(nextID.toString());
		patient.getPatient().setId(new IdDt(nextID));
		//patient.patient.setId(newPatientID);
		db.myPatients.put(nextID, patient.getPatient());
		nextID++;
		
	}

	public void updatePatient(Identifier id, PatientRequest patient) {
		/*Patient oldPatient = db.myPatients.get(id.identifier.getIdPartAsLong());
		oldPatient.addName(patient.patient.getNameFirstRep());
		db.myPatients.put(id.identifier.getIdPartAsLong(), oldPatient);*/
		db.myPatients.put(id.identifier.getIdPartAsLong(), patient.getPatient());
	}

	public List<Patient> searchPatient(PatientRequest patient) {
		IdDt patID = patient.getPatient().getId();
		Patient dbPatient = db.myPatients.get(patID.getIdPartAsLong());
		if(dbPatient == null){
			throw new ResourceNotFoundException("Der Patient mit der ID " + patID.getIdPart() + " ist nicht vorhanden");
			//OperationOutcome oo = new OperationOutcome();
			//oo.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("Patient mit dieser ID nicht vorhanden");
			//throw new InternalErrorException("Ungueltige Patienten-ID", oo);
		}
		List<Patient> retValue = new ArrayList<Patient>();
		retValue.add(dbPatient);
		
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
	
	public List<Patient> searchPatientWithFamily(PatientRequest patient){
		List<Patient> retValue = new ArrayList<Patient>();
		for (Patient next : db.myPatients.values()){
			String familyName = next.getNameFirstRep().getFamilyAsSingleString().toLowerCase();
			if(!familyName.contains(patient.getMessageText().toLowerCase())){
				continue;
			}
			retValue.add(next);
		}
		if(retValue.isEmpty())
			throw new ResourceNotFoundException("Patient mit dem Nachnamen " + patient.messageText + " nicht vorhanden");
		/*Patient newPatient = new Patient();
		newPatient.setId(new IdDt(3));
		newPatient.addIdentifier().setSystem("http://test.com/Patient").setValue("1234");
		newPatient.addName().addFamily("Simpson").addGiven("Homer").addGiven("J");
		newPatient.setGender(AdministrativeGenderEnum.MALE);
		client.createPatient(newPatient);*/
		return retValue;
	}

}
