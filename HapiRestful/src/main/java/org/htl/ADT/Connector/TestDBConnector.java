package org.htl.ADT.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htl.ADT.DB.DB;
import org.htl.ADT.DomainObjects.Identifier;
import org.htl.ADT.DomainObjects.PatientRequest;
import org.htl.ADT.Interfaces.Connector;

import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
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
		if(retValue == null){
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("Patient mit dieser ID nicht vorhanden");
			throw new InternalErrorException("Ungueltige Patienten-ID", oo);
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
			throw new InternalErrorException("Patient mit diesem Nachnamen nicht vorhanden");
		return retValue;
	}

}
