package org.htl.adt.provider;

import java.util.HashMap;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class RestfulLocationProvider implements IResourceProvider {

	HashMap<Long, Location> myLocations = new HashMap<Long, Location>();
	Long nextID = 1L;
	
	public RestfulLocationProvider() {
		Location location = new Location();
		location.setId(new IdDt(nextID));
		location.setDescription("Station: Tageschirurgie");
		myLocations.put(nextID, location);
	}

	public Class<? extends IBaseResource> getResourceType() {
		// TODO Auto-generated method stub
		return Location.class;
	}

	@Read
	public Location getResourceByID(@IdParam IdDt locID){
		Location retValue = myLocations.get(locID.getIdPartAsLong());
		if(locID == null || retValue == null){
			throw new ResourceNotFoundException(locID);
		}
		return retValue;
	}
}
