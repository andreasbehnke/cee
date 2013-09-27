package com.cee.news.client.error;

/**
 * Runtime exception thrown by the GWT service layer which has no cause, because of serialization problems.
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -3337805022593396593L;

	public ServiceException(String message) {
		super(message);
	}
}
