package org.htl.ADT.Provider;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.IResourceProvider;

public class RestfulOrganizationResourceProvider implements IResourceProvider {

	public Class<Organization> getResourceType() {
		return Organization.class;
	}
	
	public RestfulOrganizationResourceProvider() {
		Organization organization = new Organization();
		organization.setId(new IdDt(1));
		organization.addIdentifier().setSystem("http://acme.com/MRNs").setValue("ExampleKH");
		organization.addAddress().addLine("123 Main Street").setCity("Wels");
		organization.addTelecom().setUse(ContactPointUseEnum.WORK).setValue("123456789");
	}

}
