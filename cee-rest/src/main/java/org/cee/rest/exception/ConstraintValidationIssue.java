package org.cee.rest.exception;

import javax.validation.ConstraintViolation;
import javax.validation.Path.Node;

public class ConstraintValidationIssue {
	
	private String message;
	
	private String path;
	
	public ConstraintValidationIssue() {}
	
	public ConstraintValidationIssue(ConstraintViolation<?> violation) {
		StringBuilder path = new StringBuilder();
		boolean first = true;
		for (Node node : violation.getPropertyPath()) {
			if (!first) {
				path.append('.');
			} else {
				first = false;
			}
			path.append(node.getName());
		}
		this.path = path.toString();
		this.message = violation.getMessage();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
