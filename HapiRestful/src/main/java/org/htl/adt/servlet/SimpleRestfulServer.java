package org.htl.adt.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.htl.adt.provider.RestfulLocationProvider;
import org.htl.adt.provider.RestfulOrganizationResourceProvider;
import org.htl.adt.provider.RestfulPatientResourceProvider;

import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

@WebServlet("/hapiservlet/*")
public class SimpleRestfulServer extends RestfulServer{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void initialize() throws ServletException {
		List<IResourceProvider> provider = new ArrayList<IResourceProvider>();
		provider.add(new RestfulPatientResourceProvider());
		provider.add(new RestfulLocationProvider());
		setResourceProviders(provider);
		//setResourceProviders(new RestfulPatientResourceProvider());
		//setResourceProviders(new RestfulLocationProvider());
		//setResourceProviders(new RestfulOrganizationResourceProvider());
		setUseBrowserFriendlyContentTypes(true);
	}
}
