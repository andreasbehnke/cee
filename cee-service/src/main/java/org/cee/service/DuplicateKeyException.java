package org.cee.service;

import org.cee.store.EntityKey;


public class DuplicateKeyException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private final EntityKey entityKey;

	public DuplicateKeyException(EntityKey entityKey) {
		super();
		this.entityKey = entityKey;
	}
	
	public EntityKey getEntityKey() {
		return entityKey;
	}
}
