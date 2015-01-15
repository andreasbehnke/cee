package org.cee.rest.exception;

public class ValidationIssue {
	
	private String fieldName;
	
	private String message;

	public ValidationIssue() {}
	
	public ValidationIssue(String fieldName, String message) {
		this.fieldName = fieldName;
		this.message = message;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
