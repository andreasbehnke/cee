package org.cee.service;

import org.cee.store.EntityKey;

public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(EntityKey key) {
		super(String.format("Could not find %s", key.getName()));
	}
}
