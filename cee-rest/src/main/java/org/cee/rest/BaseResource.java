package org.cee.rest;

import java.util.List;

import org.cee.rest.exception.ValidationIssue;

public class BaseResource {

	protected void checkNotNull(String fieldName, Object field, List<ValidationIssue> issues) {
		if (field == null) {
			issues.add(new ValidationIssue(fieldName, "Field " + fieldName + " must not be not empty"));
		}
	}

}