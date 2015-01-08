package org.cee.rest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.cee.service.EntityNotFoundException;

@Provider
public class EntityNotFoundExceptionMapper implements
		ExceptionMapper<EntityNotFoundException> {

	@Override
	public Response toResponse(EntityNotFoundException exception) {
		return Response.status(404).entity(exception.getMessage()).type(MediaType.TEXT_PLAIN).build();
	}

}
