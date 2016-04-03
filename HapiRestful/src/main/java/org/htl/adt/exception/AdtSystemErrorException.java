package org.htl.adt.exception;

/**
 * Eine Exception, die für interne Fehler im Ausfallsystem genützt wird
 */
public class AdtSystemErrorException extends Exception {


	public AdtSystemErrorException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdtSystemErrorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdtSystemErrorException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdtSystemErrorException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdtSystemErrorException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
