package org.htl.adt.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.htl.adt.connector.DBFactory;
import org.htl.adt.interfaces.Connector;

import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.MessageHeader.Response;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class RestfulLocationProvider implements IResourceProvider {

	HashMap<Long, Location> myLocations = new HashMap<Long, Location>();
	Long nextID = 1L;
	Connector db = DBFactory.getInstance().getConnector("TestDBConnector");
	
	public RestfulLocationProvider() {
		Location location = new Location();
		location.setId(new IdDt(nextID));
		location.setDescription("Station: Tageschirurgie");
		myLocations.put(nextID, location);
		nextID++;
		Location location2 = new Location();
		location2.setId(new IdDt(nextID));
		location2.setDescription("Station: Labor");
		myLocations.put(nextID, location2);
		nextID++;
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
	
	@Create
	public MethodOutcome createLocation(@ResourceParam Location location){
		if(location.getId().isEmpty()){
			location.setId(new IdDt(nextID));
			myLocations.put(nextID, location);
			nextID++;
		} else {
			myLocations.put(location.getId().getIdPartAsLong(), location);
		}
		return new MethodOutcome(location.getId());
	}
	
	@Update
	public MethodOutcome updateLocation(@IdParam IdDt locID, @ResourceParam Location location){
		if(myLocations.keySet().contains(locID.getIdPartAsLong())){
			myLocations.put(locID.getIdPartAsLong(), location);
			return new MethodOutcome(locID);
		} else {
			throw new ResourceNotFoundException(locID);
		}
		
	}
	
	@Search
	public List<Location> getAllLocations(){
		List<Location> retValue = new ArrayList<Location>();
		retValue.addAll(myLocations.values());
		return retValue;
	}
}
