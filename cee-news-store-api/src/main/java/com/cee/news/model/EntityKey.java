package com.cee.news.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Key-name pair referencing an entity within a store
 */
public class EntityKey implements Serializable {
	
	private static final long serialVersionUID = -8676630329141286696L;

	private String name;
	
	private String key;
	
	private double score = -1;

	private String htmlContent;
	
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
	
	/**
	 * @return Score of the hit if this EntityKey was generated from a related term or fulltext search. Otherwise returns always -1;
	 */
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public EntityKey() {}
	
	public EntityKey(String name, String key) {
		this.name = name;
		this.key = key;
	}
	
	public EntityKey(String name, String key, double score) {
		this.name = name;
		this.key = key;
		this.score = score;
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

	public static List<String> extractNames(List<EntityKey> input) {
		List<String> keys = new ArrayList<String>(input.size());
		for (EntityKey key : input) {
			keys.add(key.getName());
		}
		return keys;
	}

	public static List<String> extractKeys(List<EntityKey> input) {
		List<String> keys = new ArrayList<String>(input.size());
		for (EntityKey key : input) {
			keys.add(key.getKey());
		}
		return keys;
	}
}
