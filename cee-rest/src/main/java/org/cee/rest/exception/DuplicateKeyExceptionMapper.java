package org.cee.rest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class DuplicateKeyExceptionMapper implements
		ExceptionMapper<DuplicateKeyException> {

	@Override
	public Response toResponse(DuplicateKeyException exception) {
		return Response.status(409).entity(exception.getMessage()).type(MediaType.TEXT_PLAIN).build();
	}

}
