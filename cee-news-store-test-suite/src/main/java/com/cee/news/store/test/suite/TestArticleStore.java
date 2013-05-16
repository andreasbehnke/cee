package com.cee.news.store.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.cee.news.model.Article;
import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.cee.news.model.TextBlock;
import com.cee.news.model.WorkingSet;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;
import com.cee.news.store.WorkingSetStore;

public abstract class TestArticleStore extends TestStoreBase {

	@Test
    public void testUpdateSiteArticle() throws StoreException {
		SiteStore siteStore = getSiteStore();
		ArticleStore articleStore = getArticleStore();
		DummyArticleChangeListener listener = new DummyArticleChangeListener();
		articleStore.addArticleChangeListener(listener);
		
		EntityKey site = Utils.createSite(siteStore, "http://www.xyz.de");
        ArticleKey key = Utils.updateArticle(articleStore, site, "23/1", "http://www.xyz.de/1", 2010, 1, 12, "Title", "Short Text");
        
        assertEquals("http://www.xyz.de", key.getSiteKey());
        assertEquals("23/1", key.getKey());
        assertEquals("Title", key.getName());
        
        assertEquals("http://www.xyz.de", listener.createdSiteName);
        assertEquals("23/1", listener.createdArticleId);
        
        Article article = articleStore.getArticle(key, false);
        assertEquals("23/1", article.getExternalId());
        assertEquals("en", article.getLanguage());
        assertEquals("http://www.xyz.de/1", article.getLocation());
        assertEquals(2010, article.getPublishedDate().get(Calendar.YEAR));
        assertEquals(1, article.getPublishedDate().get(Calendar.MONTH));
        assertEquals(12, article.getPublishedDate().get(Calendar.DAY_OF_MONTH));
        assertEquals("Short Text", article.getShortText());
        assertEquals("Title", article.getTitle());
    }
	
	@Test
	public void testAddNewArticles() throws StoreException {
		SiteStore siteStore = getSiteStore();
		ArticleStore articleStore = getArticleStore();
		DummyArticleChangeListener listener = new DummyArticleChangeListener();
		articleStore.addArticleChangeListener(listener);
		
		EntityKey site = Utils.createSite(siteStore, "http://www.xyz.de");
        
        Article article1 = Utils.createArticle("1", "http://www.xyz.de/1", 2010, 1, 12, "Title", "Short Text");
        Article article2 = Utils.createArticle("2", "http://www.xyz.de/2", 2010, 1, 12, "Title", "Short Text");
        Article article3 = Utils.createArticle("3", "http://www.xyz.de/4", 2010, 1, 12, "Title", "Short Text");
        Article article4 = Utils.createArticle("4", "http://www.xyz.de/5", 2010, 1, 12, "Title", "Short Text");
        
        ArticleKey key3 = articleStore.update(site, article3);
        ArticleKey key1 = articleStore.update(site, article1);
        
        List<Article> articles = new ArrayList<Article>();
        articles.add(article1);
        articles.add(article2);
        articles.add(article3);
        articles.add(article4);
        
        List<ArticleKey> keys = articleStore.addNewArticles(site, articles);
        
        assertEquals(2, keys.size());
        assertFalse(keys.contains(key1));
        assertFalse(keys.contains(key3));
        
        article2 = articleStore.getArticle(keys.get(0), false);
        assertEquals("2", article2.getExternalId());
        article4 = articleStore.getArticle(keys.get(1), false);
        assertEquals("4", article4.getExternalId());
	}
    
    @Test
    public void testUpdateSiteArticleChangeContent() throws StoreException {
    	SiteStore siteStore = getSiteStore();
		ArticleStore articleStore = getArticleStore();
		DummyArticleChangeListener listener = new DummyArticleChangeListener();
		articleStore.addArticleChangeListener(listener);
		
		EntityKey site = Utils.createSite(siteStore, "site2");
        
        List<TextBlock> text = new ArrayList<TextBlock>();
        text.add(new TextBlock("Hello world!", 2));
        text.add(new TextBlock("Another hello world!", 3));
        ArticleKey key = Utils.updateArticle(articleStore, site, "1", "http://www.abc.de/1", 2010, 1, 12, "Short Text", "Title", text);
        
        assertEquals("site2", listener.createdSiteName);
        assertEquals("1", listener.createdArticleId);
        Article article = articleStore.getArticle(key, true);
        assertEquals(2, article.getContent().size());
        Set<String> content = new HashSet<String>();
        content.add(article.getContent().get(0).getContent());
        content.add(article.getContent().get(1).getContent());
        assertTrue(content.contains("Hello world!"));
        assertTrue(content.contains("Another hello world!"));
        
        article.getContent().remove(0);
        articleStore.update(site, article);
        
        assertEquals("site2", listener.changedSiteName);
        assertEquals("1", listener.changedArticleId);
        article = articleStore.getArticle(key, true);
        assertEquals(1, article.getContent().size());
        assertEquals(article.getContent().get(0).getContent(), "Another hello world!");
        
        article.getContent().add(new TextBlock("XYZ", 1));
        articleStore.update(site, article);
        
        article = articleStore.getArticle(key, true);
        assertEquals(2, article.getContent().size());
        content = new HashSet<String>();
        content.add(article.getContent().get(0).getContent());
        content.add(article.getContent().get(1).getContent());
        assertTrue(content.contains("XYZ"));
        assertTrue(content.contains("Another hello world!"));
    }

    @Test
    public void testGetArticlesOrderedByDate() throws StoreException {
    	SiteStore siteStore = getSiteStore();
		ArticleStore articleStore = getArticleStore();
		WorkingSetStore workingSetStore = getWorkingSetStore();
		
		EntityKey site = Utils.createSite(siteStore, "http://www.abc.de");
        
        ArticleKey key1 = Utils.updateArticle(articleStore, site, "http://www.abc.de/ID_1", "http://www.abc.de/1", 2010, 1, 12);
        ArticleKey key2 = Utils.updateArticle(articleStore, site, "http://www.abc.de/ID_2", "http://www.abc.de/2", 2011, 2, 1);
        ArticleKey key3 = Utils.updateArticle(articleStore, site, "http://www.abc.de/ID_3", "http://www.abc.de/3", 1999, 12, 23);
        
        EntityKey site2 = Utils.createSite(siteStore, "site4");
        ArticleKey key4 = Utils.updateArticle(articleStore, site2, "http://www.xyz.de/ID_4", "http://www.xyz.de/4", 2012, 12, 23);
        
        List<ArticleKey> articles = articleStore.getArticlesOrderedByDate(site);
        assertEquals(3, articles.size());
        assertEquals(key2, articles.get(0));
        assertEquals(key1, articles.get(1));
        assertEquals(key3, articles.get(2));
        
        articles = articleStore.getArticlesOrderedByDate(site2);
        assertEquals(1, articles.size());
        assertEquals(key4, articles.get(0));
        
        assertEquals(0, articleStore.getArticlesOrderedByDate(new ArrayList<EntityKey>()).size());
        
        List<EntityKey> sites = new ArrayList<>();
        sites.add(site2);
        sites.add(site);
        articles = articleStore.getArticlesOrderedByDate(sites);
        assertEquals(4, articles.size());
        assertEquals(key4, articles.get(0));
        assertEquals(key2, articles.get(1));
        assertEquals(key1, articles.get(2));
        assertEquals(key3, articles.get(3));
        
        WorkingSet workingSet = new WorkingSet();
        workingSet.setName("Default");
        workingSet.getSites().add(site);
        workingSet.getSites().add(site2);
        workingSetStore.update(workingSet);
        
        articles = articleStore.getArticlesOrderedByDate(workingSet);
        assertEquals(4, articles.size());
        assertEquals(key4, articles.get(0));
        assertEquals(key2, articles.get(1));
        assertEquals(key1, articles.get(2));
        assertEquals(key3, articles.get(3));
    }
    
    @Test
    public void testGetArticles() throws StoreException {
    	SiteStore siteStore = getSiteStore();
		ArticleStore articleStore = getArticleStore();
		
		EntityKey site = Utils.createSite(siteStore, "site5");
    	List<TextBlock> text = new ArrayList<TextBlock>();
        text.add(new TextBlock("Hello world!", 2));
        text.add(new TextBlock("Another hello world!", 3));
        ArticleKey key1 = Utils.updateArticle(articleStore, site, "1", "http://www.abc.de/1", 2010, 1, 12, "Short Text", "Title", text);
        text = new ArrayList<TextBlock>();
        text.add(new TextBlock("A second hello world!", 2));
        text.add(new TextBlock("Another second hello world!", 3));
        ArticleKey key2 = Utils.updateArticle(articleStore, site, "2", "http://www.abc.de/2", 2012, 1, 12, "Short Text", "Title", text);
        
        List<ArticleKey> keys = new ArrayList<ArticleKey>();
        keys.add(key1);
        keys.add(key2);
        List<Article> articles = articleStore.getArticles(keys, true);
        
        assertEquals(2, articles.size());
        assertEquals("1", articles.get(0).getExternalId());
        assertEquals(2, articles.get(0).getContent().size());
        assertEquals("Hello world!", articles.get(0).getContent().get(0).getContent());
        assertEquals("Another hello world!", articles.get(0).getContent().get(1).getContent());
        assertEquals("2", articles.get(1).getExternalId());
        assertEquals(2, articles.get(1).getContent().size());
        assertEquals("A second hello world!", articles.get(1).getContent().get(0).getContent());
        assertEquals("Another second hello world!", articles.get(1).getContent().get(1).getContent());
    }
    
    @Test
    public void testGetContent() throws StoreException {
    	SiteStore siteStore = getSiteStore();
		ArticleStore articleStore = getArticleStore();
		
		EntityKey site = Utils.createSite(siteStore, "site6");
        
        List<TextBlock> content = new ArrayList<TextBlock>();
        content.add(new TextBlock("This are four words", 4));
        content.add(new TextBlock("foo ba", 2));
        content.add(new TextBlock("Hello world!", 2));
        ArticleKey key = Utils.updateArticle(articleStore, site, "1", "http://www.abc.de/1", 2012, 12, 23, null, null, content);
        
        content = articleStore.getArticle(key, true).getContent();
        assertEquals(3, content.size());
        assertEquals("This are four words", content.get(0).getContent());
        assertEquals(4, content.get(0).getNumWords());
        assertEquals("foo ba", content.get(1).getContent());
        assertEquals("Hello world!", content.get(2).getContent());
    }
}
