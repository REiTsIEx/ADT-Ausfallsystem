package org.htl.ADT.Servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.htl.ADT.Provider.RestfulOrganizationResourceProvider;
import org.htl.ADT.Provider.RestfulPatientResourceProvider;

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
