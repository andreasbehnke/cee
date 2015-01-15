package org.cee.rest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MissingParameterExceptionMapper implements ExceptionMapper<MissingParameterException> {

	@Override
	public Response toResponse(MissingParameterException exception) {
		return Response.status(406).entity(exception.getMessage()).type(MediaType.TEXT_PLAIN).build();
	}

}
