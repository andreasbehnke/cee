package org.cee.rest.exception;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException exception) {
		exception.getConstraintViolations();
		return Response.status(406).entity(new ConstraintValidationData(exception)).type(MediaType.APPLICATION_JSON).build();
	}

}
