package org.cee.rest.exception;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class ConstraintValidationData {
	
	private List<ConstraintValidationIssue> issues;
	
	public ConstraintValidationData() {}
	
	public ConstraintValidationData(ConstraintViolationException exception) {
		issues = new ArrayList<>();
		for (ConstraintViolation<?> constraintValidation : exception.getConstraintViolations()) {
			issues.add(new ConstraintValidationIssue(constraintValidation));
		}
	}

	public List<ConstraintValidationIssue> getIssues() {
		return issues;
	}
	
	public void setIssues(List<ConstraintValidationIssue> issues) {
		this.issues = issues;
	}
}
