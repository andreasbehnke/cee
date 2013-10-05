package org.cee.webreader.client.content;

import java.io.Serializable;
import java.util.List;

import org.cee.news.model.EntityKey;

public class LanguageList implements Serializable {

	private static final long serialVersionUID = 349743844999878876L;

	private List<EntityKey> languages;
	
	private int defaultLanguage;

	public List<EntityKey> getLanguages() {
		return languages;
	}

	public void setLanguages(List<EntityKey> languages) {
		this.languages = languages;
	}

	public int getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(int defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}
}
