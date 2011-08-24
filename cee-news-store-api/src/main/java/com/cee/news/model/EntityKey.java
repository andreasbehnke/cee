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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityKey other = (EntityKey) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + key + ":" + name + "]";
	}
}
