package Utils;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.IdDt;

public class DB {
	public Map<Long, Patient> myPatients = new HashMap<Long, Patient>();
	Long id = 1L;
	
	public DB() {
		Patient patientMax = new Patient();
		patientMax.setId(new IdDt(id));
		patientMax.addIdentifier().setSystem("http://loinc.org").setValue("1234");
		patientMax.addName().addFamily("Mustermann").addGiven("Max").addGiven("M");
		patientMax.setGender(AdministrativeGenderEnum.MALE);
		myPatients.put(id, patientMax);
		id++;
		
		Patient patientFrida = new Patient();
		patientFrida.setId(new IdDt(id));
		patientFrida.addIdentifier().setSystem("http://loinc.org").setValue("1111");
		patientFrida.addName().addFamily("Musterfrau").addGiven("Frida");
		patientFrida.setGender(AdministrativeGenderEnum.FEMALE);
		myPatients.put(id, patientFrida);
		id++;
	}
	
	public void addPatient(Patient patient){
		myPatients.put(id, patient);
		id++;
	}
	
	public Patient getPatient(IdDt patId){
		return myPatients.get(patId);
	}
	
	public void setPatient(Patient patient){
		myPatients.put(patient.getId().getIdPartAsLong(), patient);
	}
}
