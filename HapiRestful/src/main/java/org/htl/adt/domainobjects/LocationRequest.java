package org.htl.adt.domainobjects;

import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Patient;

public class LocationRequest extends Request{
	public Location location;
	
	
	public LocationRequest(String messageText, Location messageObject) {
		this.messageText = messageText;
		this.location = messageObject;
	}

	public LocationRequest() {
		
	}

}
