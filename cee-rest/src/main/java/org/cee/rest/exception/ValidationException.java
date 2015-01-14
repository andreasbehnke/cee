package org.cee.rest.exception;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final Object issue;

	public ValidationException(Object issue) {
		super();
		this.issue = issue;
	}

	public Object getIssue() {
		return issue;
	}
}
