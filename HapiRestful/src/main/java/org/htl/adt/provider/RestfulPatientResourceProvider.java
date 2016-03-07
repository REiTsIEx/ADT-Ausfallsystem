package org.htl.adt.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htl.adt.domainobjects.Identifier;
import org.htl.adt.domainobjects.PatientRequest;
import org.htl.adt.interfaces.RestServer;
import org.htl.adt.restservlet.RestFactory;

import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.ExceptionCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class RestfulPatientResourceProvider implements IResourceProvider {

	private Map<Long, Patient> myPatients = new HashMap<Long, Patient>();
	private long nextID = 1L;
	
	private RestServer restServer = RestFactory.getInstance().getServer("TestServer");

	//TestFHIRRestServlet myServlet = new TestFHIRRestServlet();
	//DB db = new DB();
	
	public Class<Patient> getResourceType() {
		return Patient.class;
	}
	
	public RestfulPatientResourceProvider() {
		long id = nextID++;
		Patient patient = new Patient();
		patient.setId(new IdDt(id));
		patient.addIdentifier().setSystem("http://test.com/Patient").setValue("1234");
		patient.addName().addFamily("Simpson").addGiven("Homer").addGiven("J");
		patient.setGender(AdministrativeGenderEnum.MALE);
		myPatients.put(id, patient);
		
		/*Long id = myNextID++;
		
		Patient pat1 = new Patient();
		pat1.setId(new IdDt(id));
		pat1.addIdentifier().setSystem("http://test.com/Patients").setValue("1234");
		pat1.addName().addFamily("Max").addGiven("Mustermann").addGiven("M");
		myPatients.put(id, pat1);*/
	}

	@Read(version=true)
	public Patient getResourceById(@IdParam IdDt patID){
		/*Patient retValue = myPatients.get(patID.getIdPartAsLong());
		if(patID == null){
			throw new ResourceNotFoundException(patID);
		}
		return retValue;*/
		if(patID.isEmpty()){
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("Ungültige ID wurde eingegeben");
			throw new InternalErrorException("Ungültige ID", oo);
		}
		Patient pat = new Patient();
		pat.setId(new IdDt(patID));
		PatientRequest request = new PatientRequest("Der zu suchende Patient", pat);
		Patient retValue = restServer.searchPatientWithID(request);
		return retValue;
		
	}
	
	@Create
	public MethodOutcome create(@ResourceParam Patient patient){
		if(patient != null){
			PatientRequest request = new PatientRequest("Der anzulegende Patient", patient);
			restServer.addPatient(request);
			return new MethodOutcome(new IdDt("Patient", patient.getId().toString(), patient.getId().getVersionIdPart()));
			//OperationOutcome oo = new OperationOutcome();
			//oo.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDetails(patient.toString());
			//throw new InternalErrorException("Patient wurde angelegt", oo);
		}else {
			throw new InternalErrorException("Kein gültiger Patient eingegeben");
		}
		//return new MethodOutcome(patient.getId());
	}
	
	@Search
	public List<Patient> search(@RequiredParam(name="family") StringParam familyName, @OptionalParam(name="firstname") StringParam firstName){
		/*List<Patient> retValue = new ArrayList<Patient>();
		for (Patient next : myPatients.values()){
			String familyName = next.getNameFirstRep().getFamilyAsSingleString().toLowerCase();
			if(!familyName.contains(theParam.getValue().toLowerCase())){
				continue;
			}
			retValue.add(next);
		}
		if(retValue.isEmpty())
			throw new InternalErrorException("Patient mit diesem Nachnamn nicht vorhanden");
		return retValue;*/
		PatientRequest request = new PatientRequest(familyName.getValue(), null);
		return restServer.searchPatientWithFamily(request);
	}
	
	@Search
	public List<Patient> search(){
		/*List<Patient> retValue = new ArrayList<Patient>();
		retValue.addAll(myPatients.values());
		return retValue;*/
		return restServer.getAllPatient();
	}
	
	@Update
	public MethodOutcome updatePatient(@IdParam IdDt id, @ResourceParam Patient patient){
		Identifier identifier = new Identifier(id);
		PatientRequest request = new PatientRequest("Die neuen Patientendaten", patient);
		restServer.updatePatient(identifier, request);
		return new MethodOutcome(id);
	}
	
}