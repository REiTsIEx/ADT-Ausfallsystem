package org.htl.adt.domainobjects;


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
	private String identifier;
	
	@Column(name = "FirstName")
	private String firstName;
	
	@Column(name = "LastName")
	private String lastName;
	
	@Column(name = "FhirMessage")
	private String fhirMessage;
	
	

	public DatabasePatient() {
		super();
	}

	public DatabasePatient(String identifier, String firstName, String lastName, String fhirMessage) {
		super();
		this.identifier = identifier;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fhirMessage = fhirMessage;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public int getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFhirMessage() {
		return fhirMessage;
	}

	public void setFhirMessage(String fhirMessage) {
		this.fhirMessage = fhirMessage;
	}
	
	
	
	
	
	
	

}
