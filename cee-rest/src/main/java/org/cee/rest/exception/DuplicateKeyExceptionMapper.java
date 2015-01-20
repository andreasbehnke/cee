package org.cee.rest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.cee.service.DuplicateKeyException;

@Provider
public class DuplicateKeyExceptionMapper implements
		ExceptionMapper<DuplicateKeyException> {

	@Override
	public Response toResponse(DuplicateKeyException exception) {
		return Response.status(409).entity(exception.getEntityKey()).type(MediaType.APPLICATION_JSON).build();
	}

}
