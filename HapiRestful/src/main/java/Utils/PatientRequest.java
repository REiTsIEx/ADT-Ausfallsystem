package Utils;

import ca.uhn.fhir.model.dstu2.resource.Patient;

public class PatientRequest extends Request {
		
	public Patient patient;
	
	public PatientRequest(String messageText, Patient messageObject) {
		this.messageText = messageText;
		this.patient = messageObject;
	}

	public PatientRequest() {
		
	}
	
	

}
