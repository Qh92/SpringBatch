package org.springbatch.skip;

public class CustomRetryException extends Exception{

	private static final long serialVersionUID = -1406411434441162872L;

	public CustomRetryException() {
		super();
	}

	public CustomRetryException(String message) {
		super(message);
	}

}
