package org.htl.adt.domainobjects;

import ca.uhn.fhir.model.dstu2.resource.Encounter;

public class EncounterRequest extends Request{
	
	private Encounter encounter;

	public EncounterRequest(String messageText, Encounter encounter) {
		super(messageText);
		this.setEncounter(encounter);
	}

	public EncounterRequest() {
		super();
	}

	
	public Encounter getEncounter() {
		return encounter;
	}

	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}

}
