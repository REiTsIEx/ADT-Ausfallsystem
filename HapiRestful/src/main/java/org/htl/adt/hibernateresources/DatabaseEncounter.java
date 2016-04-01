package org.htl.adt.hibernateresources;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Patient")
public class DatabaseEncounter {
	
	@Column(name = "Encounter_id")
	private int encounter_id;
	
	@Id
	@Column(name = "Identifier")
	private String encounterIdentifier;
	
	@Column(name = "PatientIdentifier")
	private String patientIdentifier;
	
	@Column(name = "Priority")
	private String priotity;
	
	@Column(name = "Reason")
	private String reason;
	
	@Column(name = "Status")
	private String status;
	
	@Column(name = "StartDate")
	private Date startDate;
	
	@Column(name = "EndDate")
	private String endDate;
	
	@Column(name = "FhirMessage")
	private String fhirMessage;

	public DatabaseEncounter() {
		super();
	}

	public DatabaseEncounter(String encounterIdentifier,
			String patientIdentifier, String priotity, String reason,
			String status, Date startDate, String endDate, String fhirMessage) {
		super();
		this.encounterIdentifier = encounterIdentifier;
		this.patientIdentifier = patientIdentifier;
		this.priotity = priotity;
		this.reason = reason;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
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

	public String getPriotity() {
		return priotity;
	}

	public void setPriotity(String priotity) {
		this.priotity = priotity;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getFhirMessage() {
		return fhirMessage;
	}

	public void setFhirMessage(String fhirMessage) {
		this.fhirMessage = fhirMessage;
	}
	

}
