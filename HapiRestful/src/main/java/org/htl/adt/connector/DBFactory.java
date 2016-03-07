package org.htl.adt.connector;

import org.htl.adt.interfaces.Connector;

public class DBFactory {

	private static DBFactory factory;
	
	private DBFactory() {
		super();
	}
	
	public static synchronized DBFactory getInstance() {
		if(factory == null)
			factory  = new DBFactory();
		
		return factory;
	}
	
	public Connector getConnector(String typeOfConnector){
		if(typeOfConnector == null){
			return null;
		}
		if(typeOfConnector.equalsIgnoreCase("TestDBConnector")){
			return new TestDBConnector();
		}
		
		else if(typeOfConnector.equalsIgnoreCase("DBConnector")){
			return new DBConnector();
		}
		
		return null;
	}
}
