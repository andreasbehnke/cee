package org.cee.rest.exception;

public class MissingParameterException extends Exception {

	private static final long serialVersionUID = 1L;

	public MissingParameterException(String parameterName) {
		super(String.format("Missing parameter: %s", parameterName));
	}
}
