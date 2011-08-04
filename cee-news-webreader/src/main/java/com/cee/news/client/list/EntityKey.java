package com.cee.news.client.list;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represents the name and the key of a link to an entity.
 */
public class EntityKey implements IsSerializable {

	private String key;

	private String name;

	public EntityKey() {
	}

	public EntityKey(String key, String name) {
		this.key = key;
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
