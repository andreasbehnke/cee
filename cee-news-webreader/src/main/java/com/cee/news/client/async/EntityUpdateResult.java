package com.cee.news.client.async;

import com.cee.news.model.EntityKey;
import com.google.gwt.user.client.rpc.IsSerializable;

public class EntityUpdateResult implements IsSerializable {
	
	public enum State {
		ok,
		entityExists
	}
	
	private State state;
	
	private EntityKey key;

	public EntityUpdateResult() {}
	
	public EntityUpdateResult(State state, EntityKey key) {
		this.state = state;
		this.key = key;
	}

	public State getState() {
		return state;
	}

	public EntityKey getKey() {
		return key;
	}
}
