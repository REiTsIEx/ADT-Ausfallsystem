package org.htl.adt.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.htl.adt.provider.RestfulOrganizationResourceProvider;
import org.htl.adt.provider.RestfulPatientResourceProvider;

import ca.uhn.fhir.rest.server.RestfulServer;

@WebServlet("/hapiservlet/*")
public class SimpleRestfulServer extends RestfulServer{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void initialize() throws ServletException {
		setResourceProviders(new RestfulPatientResourceProvider());
		//setResourceProviders(new RestfulOrganizationResourceProvider());
		setUseBrowserFriendlyContentTypes(true);
	}
}
