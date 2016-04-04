package org.htl.adt.hibernateresources;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.htl.adt.domainobjects.LocationRequest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Location;

@Entity
@Table(name = "Location")
public class DatabaseLocation {
	
	@Column(name = "Location_id")
	private int location_id;
	
	@Id
	@Column(name = "Identifier")
	private String locationIdentifier;
	
	@Column(name = "Name")
	private String name;
	
	@Column(name = "Status")
	private String status;
	
	@Column(name = "Description")
	private String description;
	
	@Column(name = "FhirMessage")
	private String fhirMessage;
	
		
	public DatabaseLocation() {
		super();
	}
	
	

	public DatabaseLocation(Location location) {
		FhirContext ctx = FhirContext.forDstu2();

		this.locationIdentifier = location.getId().getIdPart();
		this.name = location.getName();
		this.status = location.getStatus();
		this.description = location.getDescription();
		this.fhirMessage = ctx.newXmlParser().encodeResourceToString(location);
	}



	public DatabaseLocation(String locationIdentifier, String name, String status, String description, String fhirMessage) {
		super();
		this.locationIdentifier = locationIdentifier;
		this.name = name;
		this.status = status;
		this.description = description;
		this.fhirMessage = fhirMessage;
	}

	public int getLocation_id() {
		return location_id;
	}

	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}

	public String getLocationIdentifier() {
		return locationIdentifier;
	}

	public void setLocationIdentifier(String locationIdentifier) {
		this.locationIdentifier = locationIdentifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFhirMessage() {
		return fhirMessage;
	}

	public void setFhirMessage(String fhirMessage) {
		this.fhirMessage = fhirMessage;
	}

	

}