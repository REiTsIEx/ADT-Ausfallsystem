package org.htl.adt.domainobjects;

public abstract class Request {

	private String messageText;

	public Request(String messageText) {
		super();
		this.messageText = messageText;
	}

	public Request() {
		super();
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	
	
	
}
