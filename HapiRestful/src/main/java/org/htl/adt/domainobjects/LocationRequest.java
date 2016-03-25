package org.htl.adt.domainobjects;

import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Patient;

public class LocationRequest extends Request{
	private Location location;
	
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public LocationRequest(String messageText, Location messageObject) {
		super(messageText);
		this.location = messageObject;
	}

	public LocationRequest() {
		super();
	}

}
