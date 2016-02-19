package org.htl.adtausfallsystem.connector;

import org.htl.adtausfallsystem.interfaces.Connector;

public class DBFactory {

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
