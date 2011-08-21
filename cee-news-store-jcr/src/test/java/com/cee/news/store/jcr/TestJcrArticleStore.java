package com.cee.news.store.jcr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cee.news.model.Article;
import com.cee.news.model.NamedKey;
import com.cee.news.model.Site;
import com.cee.news.model.TextBlock;
import com.cee.news.model.WorkingSet;
import com.cee.news.store.StoreException;

public class TestJcrArticleStore extends JcrTestBase {

	private static JcrWorkingSetStore workingSetStore;
	
    private static JcrSiteStore siteStore;
    
    private static JcrArticleStore articleStore;
    
    @BeforeClass
    public static void setupStores() throws LoginException, RepositoryException, StoreException {
        setupSession();
        workingSetStore = new JcrWorkingSetStore(session);
        siteStore = new JcrSiteStore(session);
        articleStore = new JcrArticleStore(siteStore, session);
    }
    
    @AfterClass
    public static void close() {
        closeSession();
    }
    
    private Site createSite(String name) throws StoreException {
        Site site = new Site();
        site.setName(name);
        site.setLocation("http://www.abc.de");
        siteStore.update(site);
        return site;
    }
    
    @Test
    public void testUpdateSiteArticle() throws StoreException, MalformedURLException {
        Site site = createSite("site1");
        
        Article article = new Article();
        article.setId("1");
        String url = "http://www.abc.de/1";
        article.setLocation(url);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, 2010);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 12);
        article.setPublishedDate(cal);
        article.setShortText("Short Text");
        article.setTitle("Title");
        articleStore.update(site, article);
        
        String path = articleStore.getArticlesOrderedByDate(site).get(0).getKey();
        
        article = articleStore.getArticle(path);
        assertEquals("1", article.getId());
        assertEquals(url, article.getLocation());
        assertEquals(2010, article.getPublishedDate().get(Calendar.YEAR));
        assertEquals(1, article.getPublishedDate().get(Calendar.MONTH));
        assertEquals(12, article.getPublishedDate().get(Calendar.DAY_OF_MONTH));
        assertEquals("Short Text", article.getShortText());
        assertEquals("Title", article.getTitle());
    }
    
    //TODO: Implement complete content path logic, so existing content can be detected
    @Test
    public void testUpdateSiteArticleChangeContent() throws StoreException, MalformedURLException {
        Site site = createSite("site2");
        
        Article article = new Article();
        article.setId("1");
        String url = "http://www.abc.de/1";
        article.setLocation(url);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, 2010);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 12);
        article.setPublishedDate(cal);
        article.setShortText("Short Text");
        article.setTitle("Title");
        article.getContent().add(new TextBlock("Hello world!", 2));
        article.getContent().add(new TextBlock("Another hello world!", 3));
        String path = articleStore.update(site, article);
        
        article = articleStore.getArticle(path);
        article.setContent(articleStore.getContent(path));
        assertEquals(2, article.getContent().size());
        Set<String> content = new HashSet<String>();
        content.add(article.getContent().get(0).getContent());
        content.add(article.getContent().get(1).getContent());
        assertTrue(content.contains("Hello world!"));
        assertTrue(content.contains("Another hello world!"));
        
        article.getContent().remove(0);
        articleStore.update(site, article);
        
        article = articleStore.getArticle(path);
        article.setContent(articleStore.getContent(path));
        assertEquals(1, article.getContent().size());
        assertEquals(article.getContent().get(0).getContent(), "Another hello world!");
        
        article.getContent().add(new TextBlock("XYZ", 1));
        articleStore.update(site, article);
        
        article = articleStore.getArticle(path);
        article.setContent(articleStore.getContent(path));
        assertEquals(2, article.getContent().size());
        content = new HashSet<String>();
        content.add(article.getContent().get(0).getContent());
        content.add(article.getContent().get(1).getContent());
        assertTrue(content.contains("XYZ"));
        assertTrue(content.contains("Another hello world!"));
    }

    @Test
    public void testGetArticlesOrderedByDate() throws StoreException, MalformedURLException {
        Site site = createSite("site3");
        
        Article article = new Article();
        article.setId("1");
        String url = "http://www.abc.de/1";
        article.setLocation(url);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, 2010);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 12);
        article.setPublishedDate(cal);
        String path1 = articleStore.update(site, article);
        
        article = new Article();
        article.setId("2");
        url = "http://www.abc.de/2";
        article.setLocation(url);
        cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, 2011);
        cal.set(Calendar.MONTH, 2);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        article.setPublishedDate(cal);
        String path2 = articleStore.update(site, article);
        
        article = new Article();
        article.setId("3");
        url = "http://www.abc.de/3";
        article.setLocation(url);
        cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, 1999);
        cal.set(Calendar.MONTH, 12);
        cal.set(Calendar.DAY_OF_MONTH, 23);
        article.setPublishedDate(cal);
        String path3 = articleStore.update(site, article);
        
        Site site2 = createSite("site4");
        site2.setDescription("Description");
        site2.setLocation("http://www.xyz.de");
        site2.setName("http://www.xyz.de");
        site2.setTitle("Title");
        siteStore.update(site2);
        
        article = new Article();
        article.setId("4");
        url = "http://www.xyz.de/4";
        article.setLocation(url);
        cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 12);
        cal.set(Calendar.DAY_OF_MONTH, 23);
        article.setPublishedDate(cal);
        String path4 = articleStore.update(site2, article);
        
        List<NamedKey> articles = articleStore.getArticlesOrderedByDate(site);
        assertEquals(3, articles.size());
        assertEquals(path2, articles.get(0).getKey());
        assertEquals(path1, articles.get(1).getKey());
        assertEquals(path3, articles.get(2).getKey());
        
        articles = articleStore.getArticlesOrderedByDate(site2);
        assertEquals(1, articles.size());
        assertEquals(path4, articles.get(0).getKey());
        
        WorkingSet workingSet = new WorkingSet();
        workingSet.setName("Default");
        workingSet.getSites().add(new NamedKey(site.getName(), JcrSiteStore.getSitePath(site.getName())));
        workingSet.getSites().add(new NamedKey(site2.getName(), JcrSiteStore.getSitePath(site2.getName())));
        workingSetStore.update(workingSet);
        
        articles = articleStore.getArticlesOrderedByDate(workingSet);
        assertEquals(4, articles.size());
        assertEquals(path4, articles.get(0).getKey());
        assertEquals(path2, articles.get(1).getKey());
        assertEquals(path1, articles.get(2).getKey());
        assertEquals(path3, articles.get(3).getKey());
    }

    @Test
    public void testGetContent() throws StoreException, MalformedURLException {
        Site site = createSite("site5");
        
        Article article = new Article();
        article.setId("1");
        String url = "http://www.abc.de/1";
        article.setLocation(url);
        article.getContent().add(new TextBlock("This are four words", 4));
        article.getContent().add(new TextBlock("foo ba", 2));
        article.getContent().add(new TextBlock("Hello world!", 2));
        String path = articleStore.update(site, article);

        List<TextBlock> content = articleStore.getContent(path);
        assertEquals(3, content.size());
        assertEquals("This are four words", content.get(0).getContent());
        assertEquals(4, content.get(0).getNumWords());
        assertEquals("foo ba", content.get(1).getContent());
        assertEquals("Hello world!", content.get(2).getContent());
    }
}
