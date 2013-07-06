package com.cee.news.client.content;

import com.cee.news.model.EntityKey;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SiteUpdateResult implements IsSerializable {
	
	public enum State {
		ok,
		entityExists,
		languageMissing
	}
	
	private State state;
	
	private EntityKey key;

	public SiteUpdateResult() {}
	
	public SiteUpdateResult(State state, EntityKey key) {
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
