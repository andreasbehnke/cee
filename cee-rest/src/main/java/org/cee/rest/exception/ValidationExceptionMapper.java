package org.cee.rest.exception;

import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
	
	private static class ValidationIssueListGenericEntity extends GenericEntity<List<ValidationIssue>> {
		protected ValidationIssueListGenericEntity(List<ValidationIssue> entity) {
			super(entity);
		}		
	}

	@Override
	public Response toResponse(ValidationException exception) {
		ValidationIssueListGenericEntity entity = new ValidationIssueListGenericEntity((List<ValidationIssue>)exception.getIssue());
		return Response.status(406).entity(entity).type(MediaType.APPLICATION_JSON).build();
	}

}