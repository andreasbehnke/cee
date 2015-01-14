package org.cee.rest.exception;


public class DuplicateKeyException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private final Object issue;

	public DuplicateKeyException(Object issue) {
		super();
		this.issue = issue;
	}
	
	public Object getIssue() {
		return issue;
	}
}
