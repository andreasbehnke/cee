package org.cee.client.language;

/*
 * #%L
 * News Reader
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
