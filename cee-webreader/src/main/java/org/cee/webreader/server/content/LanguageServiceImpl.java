package org.cee.webreader.server.content;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.cee.client.language.LanguageList;
import org.cee.news.model.EntityKey;
import org.cee.search.ArticleSearchService;
import org.cee.webreader.client.content.GwtLanguageService;
import org.springframework.context.i18n.LocaleContextHolder;

public class LanguageServiceImpl implements GwtLanguageService {
	
	private ArticleSearchService searchService;
	
	public ArticleSearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(ArticleSearchService searchService) {
		this.searchService = searchService;
	}
	
	@Override
	public LanguageList getSupportedLanguages() {
		List<String> langIds = searchService.getSupportedLanguages();
		if (langIds == null) {
			//search service does not support languages at all, provide user with full list of languages
			langIds = Arrays.asList(Locale.getISOLanguages());
		}
		List<EntityKey> languages = new ArrayList<EntityKey>();
		int defaultLanguageIndex = 0;
		String defaultLanguage = "en";
		Locale currentUserLocale = LocaleContextHolder.getLocaleContext().getLocale();
		if (currentUserLocale != null) {
			defaultLanguage = currentUserLocale.getLanguage();
		}
		for (String langId : langIds) {
			Locale locale = Locale.forLanguageTag(langId);
			if (locale != null) {
				if (locale.getLanguage().startsWith(defaultLanguage) || defaultLanguage.startsWith(locale.getLanguage())) {
					defaultLanguageIndex = languages.size();
				}
				languages.add(EntityKey.get(locale.getDisplayLanguage(), langId));
			} else {
				languages.add(EntityKey.get(langId, langId));
			}
		}
		LanguageList languageList = new LanguageList();
		languageList.setLanguages(languages);
		languageList.setDefaultLanguage(defaultLanguageIndex);
		return languageList;
	}
}
