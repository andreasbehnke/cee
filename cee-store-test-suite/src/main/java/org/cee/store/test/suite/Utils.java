package org.cee.store.test.suite;

/*
 * #%L
 * Content Extraction Engine - News Store Test Suite
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

import java.util.Calendar;
import java.util.List;

import org.cee.news.model.Article;
import org.cee.news.model.ArticleKey;
import org.cee.news.model.EntityKey;
import org.cee.news.model.Site;
import org.cee.news.model.TextBlock;
import org.cee.news.store.ArticleStore;
import org.cee.news.store.SiteStore;
import org.cee.news.store.StoreException;

public class Utils {
	
	public static EntityKey createSite(SiteStore siteStore, String name) throws StoreException {
        Site site = new Site();
        site.setName(name);
        site.setLocation("http://www.abc.de");
        site.setLanguage("en");
        return siteStore.update(site);
    }

    public static Article createArticle(String id, String location, int year, int month, int dayOfMonth) {
        Article article = new Article();
        article.setExternalId(id);
        article.setLanguage("en");
        article.setLocation(location);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        article.setPublishedDate(cal);
        return article;
    }

    public static Article createArticle(String id, String location, int year, int month, int dayOfMonth, String title, String shortText) {
        Article article = createArticle(id, location, year, month, dayOfMonth);
        article.setTitle(title);
        article.setShortText(shortText);
        return article;
    }

    public static Article createArticle(String id, String location, int year, int month, int dayOfMonth, String title, String shortText, List<TextBlock> content) {
        Article article = createArticle(id, location, year, month, dayOfMonth, title, shortText);
        article.setContent(content);
        return article;
    }

    public static ArticleKey updateArticle(ArticleStore articleStore, EntityKey site, String id, String location, int year, int month, int dayOfMonth) throws StoreException {
        return articleStore.update(site, createArticle(id, location, year, month, dayOfMonth));
    }

    public static ArticleKey updateArticle(ArticleStore articleStore, EntityKey site, String id, String location, int year, int month, int dayOfMonth, String title, String shortText) throws StoreException {
        return articleStore.update(site, createArticle(id, location, year, month, dayOfMonth, title, shortText));
    }

    public static ArticleKey updateArticle(ArticleStore articleStore, EntityKey site, String id, String location, int year, int month, int dayOfMonth, String title, String shortText, List<TextBlock> content) throws StoreException {
        return articleStore.update(site, createArticle(id, location, year, month, dayOfMonth, title, shortText, content));
    }
}
