package org.cee.service.language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.cee.client.language.LanguageList;
import org.cee.news.model.EntityKey;
import org.cee.search.ArticleSearchService;
import org.springframework.context.i18n.LocaleContextHolder;

public class LanguageService {

	private ArticleSearchService searchService;

	public void setSearchService(ArticleSearchService searchService) {
		this.searchService = searchService;
	}
	
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
