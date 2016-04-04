package org.htl.adt.hibernateresources;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Encounter;

@Entity
@Table(name = "Encounter")
public class DatabaseEncounter {
	
	@Column(name = "Encounter_id")
	private int encounter_id;
	
	@Id
	@Column(name = "Identifier")
	private String encounterIdentifier;
	
	@Column(name = "PatientIdentifier")
	private String patientIdentifier;
	
	@Column(name = "LocationIdentifier")
	private String locationIdentifier;
	
	@Column(name = "Priority")
	private String priority;
	
	@Column(name = "Reason")
	private String reason;
	
	@Column(name = "Status")
	private String status;
	
	@Column(name = "FhirMessage")
	private String fhirMessage;
	
	

	public DatabaseEncounter() {
		super();
	}

	public DatabaseEncounter(Encounter encounter) {
		FhirContext ctx = FhirContext.forDstu2();
	
		this.encounterIdentifier = encounter.getId().getIdPart();
		this.patientIdentifier = encounter.getPatient().getReference().getIdPart();
		this.locationIdentifier = encounter.getLocationFirstRep().getLocation().getReference().getIdPart();
		this.priority = encounter.getPriority().getText();
		if(encounter.getReason().isEmpty()){
			this.reason = null;
		}else{
			this.reason = encounter.getReason().toString();
		}
		this.status = encounter.getStatus();
		this.fhirMessage = ctx.newXmlParser().encodeResourceToString(encounter);
		
	}

	public DatabaseEncounter(String encounterIdentifier, String patientIdentifier, String locationIdentifier, String priority, String reason,
			String status, String fhirMessage) {
		super();
		this.encounterIdentifier = encounterIdentifier;
		this.patientIdentifier = patientIdentifier;
		this.locationIdentifier = locationIdentifier;
		this.priority = priority;
		this.reason = reason;
		this.status = status;
		this.fhirMessage = fhirMessage;
	}

	public int getEncounter_id() {
		return encounter_id;
	}

	public void setEncounter_id(int encounter_id) {
		this.encounter_id = encounter_id;
	}

	public String getEncounterIdentifier() {
		return encounterIdentifier;
	}

	public void setEncounterIdentifier(String encounterIdentifier) {
		this.encounterIdentifier = encounterIdentifier;
	}

	public String getPatientIdentifier() {
		return patientIdentifier;
	}

	public void setPatientIdentifier(String patientIdentifier) {
		this.patientIdentifier = patientIdentifier;
	}

	public String getLocationIdentifier() {
		return locationIdentifier;
	}

	public void setLocationIdentifier(String locationIdentifier) {
		this.locationIdentifier = locationIdentifier;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFhirMessage() {
		return fhirMessage;
	}

	public void setFhirMessage(String fhirMessage) {
		this.fhirMessage = fhirMessage;
	}
	
	
	

	
}