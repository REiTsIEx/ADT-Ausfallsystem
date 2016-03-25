package org.htl.adt.hibernateresources;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Patient")
public class DatabasePatient{
	
	@Column(name = "Patient_id")
	private int patient_id;
	
	@Id
	@Column(name = "Identifier")
	private String patientIdentifier;
	
	@Column(name = "FirstName")
	private String firstName;
	
	@Column(name = "Gender")
	private String patientGender;
	

	@Column(name = "LastName")
	private String familyName;
	
	@Column(name = "FhirMessage")
	private String fhirMessage;

	public int getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}

	public String getPatientIdentifier() {
		return patientIdentifier;
	}

	public void setPatientIdentifier(String patientIdentifier) {
		this.patientIdentifier = patientIdentifier;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(String patientGender) {
		this.patientGender = patientGender;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFhirMessage() {
		return fhirMessage;
	}

	public void setFhirMessage(String fhirMessage) {
		this.fhirMessage = fhirMessage;
	}

	public DatabasePatient(String patientIdentifier, String firstName,
			String patientGender, String familyName, String fhirMessage) {
		super();
		this.patientIdentifier = patientIdentifier;
		this.firstName = firstName;
		this.patientGender = patientGender;
		this.familyName = familyName;
		this.fhirMessage = fhirMessage;
	}

	public DatabasePatient() {
		super();
	}

	
	

}
