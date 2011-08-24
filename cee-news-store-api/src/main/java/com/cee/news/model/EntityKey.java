package com.cee.news.model;

import java.io.Serializable;

/**
 * Key-name pair referencing an entity within a store
 */
public class EntityKey implements Serializable {
	
	private static final long serialVersionUID = -8676630329141286676L;

	private String name;
	
	private String key;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public EntityKey(String name, String key) {
		this.name = name;
		this.key = key;
	}

	public EntityKey() {}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EntityKey) {
			return ((EntityKey) obj).key.equals(key);
		}
		return key.equals(obj);
	}
	
	@Override
	public String toString() {
		return "[" + key + ":" + name + "]";
	}
}
