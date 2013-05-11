package com.cee.news.store.test.suite;

import java.util.Calendar;
import java.util.List;

import com.cee.news.model.Article;
import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;
import com.cee.news.model.TextBlock;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;

public class Utils {
	
	public static EntityKey createSite(SiteStore siteStore, String name) throws StoreException {
        Site site = new Site();
        site.setName(name);
        site.setLocation("http://www.abc.de");
        site.setLanguage("de");
        return siteStore.update(site);
    }

    private static Article createArticle(String id, String location, int year, int month, int dayOfMonth) {
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

    private static Article createArticle(String id, String location, int year, int month, int dayOfMonth, String title, String shortText) {
        Article article = createArticle(id, location, year, month, dayOfMonth);
        article.setTitle(title);
        article.setShortText(shortText);
        return article;
    }

    private static Article createArticle(String id, String location, int year, int month, int dayOfMonth, String title, String shortText, List<TextBlock> content) {
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
