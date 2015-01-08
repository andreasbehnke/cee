package org.cee.store;

/*
 * #%L
 * Content Extraction Engine - News Store API
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.Serializable;

/**
 * Key-name pair referencing an entity within a store
 */
public class EntityKey implements Serializable {
	
	private static final long serialVersionUID = -8676630329141286696L;
	
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
	
	public EntityKey() {}
	
	protected EntityKey(String name, String key) {
		if (key == null) {
			throw new IllegalArgumentException("Parameter key must not be null");
		}
		this.name = name;
		this.key = key;
	}
	
	public static EntityKey get(String key) {
	    return new EntityKey(key, key);
	}
	
	public static EntityKey get(String name, String key) {
        return new EntityKey(name, key);
    }
	
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
		return "[key=" + key + ";name=" + name + "]";
	}
}
