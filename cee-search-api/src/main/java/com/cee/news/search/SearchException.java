package com.cee.news.search;

public class SearchException extends Exception {

	private static final long serialVersionUID = -6156927724860081041L;

	public SearchException(String message, Throwable cause) {
		super(message, cause);
	}

	public SearchException(String message) {
		super(message);
	}
}
