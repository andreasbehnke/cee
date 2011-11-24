package com.cee.news.client.list;

import java.io.Serializable;

import com.cee.news.model.EntityKey;

/**
 * Contains the rendered content for an entity
 */
public class EntityContent implements Serializable {

	private static final long serialVersionUID = -2700033381023447136L;

	private String htmlContent;
	
	private EntityKey key;

	public EntityContent() {}
	
	public EntityContent(EntityKey key, String htmlContent) {
		this.key = key;
		this.htmlContent = htmlContent;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public EntityKey getKey() {
		return key;
	}

	public void setKey(EntityKey key) {
		this.key = key;
	}
}
