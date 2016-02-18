package Provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import Interfaces.RestServer;
import Utils.DB;
import Utils.RestFactory;
import Utils.TestFHIRRestServlet;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class RestfulPatientResourceProvider implements IResourceProvider {

	private Map<Long, Patient> myPatients = new HashMap<Long, Patient>();
	private long myNextID = 1L;
	
	RestFactory restFactory = new RestFactory();
	RestServer myServlet = restFactory.getServer("TestServer");

	//TestFHIRRestServlet myServlet = new TestFHIRRestServlet();
	//DB db = new DB();
	
	public Class<Patient> getResourceType() {
		return Patient.class;
	}
	
	public RestfulPatientResourceProvider() {
		long id = myNextID++;
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

	@Read
	public Patient getResourceById(@IdParam IdDt patID){
		/*Patient retValue = myPatients.get(patID.getIdPartAsLong());
		if(patID == null){
			throw new ResourceNotFoundException(patID);
		}
		return retValue;*/
		Patient pat = new Patient();
		pat.setId(new IdDt(patID));
		return myServlet.searchPatientOK(pat);
	}
	
	@Create
	public MethodOutcome create(@ResourceParam Patient patient){
		myServlet.addPatient(patient);
		return new MethodOutcome(patient.getId());
	}
	
	@Search
	public List<Patient> search(@RequiredParam(name="family") StringParam theParam){
		List<Patient> retValue = new ArrayList<Patient>();
		for (Patient next : myPatients.values()){
			String familyName = next.getNameFirstRep().getFamilyAsSingleString().toLowerCase();
			if(!familyName.contains(theParam.getValue().toLowerCase())){
				continue;
			}
			retValue.add(next);
		}
		return retValue;
	}
	
	@Search
	public List<Patient> search(){
		/*List<Patient> retValue = new ArrayList<Patient>();
		retValue.addAll(myPatients.values());
		return retValue;*/
		return myServlet.getAllPatient();
	}
	
	@Update
	public MethodOutcome updatePatient(@IdParam IdDt id, @ResourceParam Patient patient){
		myServlet.updatePatient(id, patient);
		return new MethodOutcome(id);
	}
	
}
