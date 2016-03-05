package org.htl.ADT.RestServlet;

import org.htl.ADT.Interfaces.RestServer;

public class RestFactory {

	private static RestFactory factory;
	
	private RestFactory() {
		super();
	}
	
	public static synchronized RestFactory getInstance() {
		if(factory == null)
			factory  = new RestFactory();
		
		return factory;
	}
	
	public RestServer getServer(String typeOfServer){
		if(typeOfServer == null){
			return null;
		}
		if(typeOfServer.equalsIgnoreCase("TestServer")){
			return new TestFHIRRestServlet();
		}
		
		else if(typeOfServer.equalsIgnoreCase("Server")){
			return new FHIRRestServlet();
		}
		
		return null;
	}
}
