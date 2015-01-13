package org.cee.rest.exception;

import org.cee.store.EntityKey;

public class DuplicateKeyException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public DuplicateKeyException(EntityKey key) {
		super(String.format("Entity with key %s already exists", key.getName()));
	}
}
