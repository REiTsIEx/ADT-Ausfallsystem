package Connector;

import Interfaces.Connector;

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
