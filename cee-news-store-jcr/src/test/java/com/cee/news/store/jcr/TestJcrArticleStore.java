package com.cee.news.store.jcr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jcr.RepositoryException;

import org.junit.Test;

import com.cee.news.model.Article;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;
import com.cee.news.model.TextBlock;
import com.cee.news.model.WorkingSet;
import com.cee.news.store.StoreException;

public class TestJcrArticleStore extends JcrTestBase {

	@Test
    public void testUpdateSiteArticle() throws StoreException, MalformedURLException {
        Site site = createSite("site1");
        String path = updateArticle(site, "1", "http://www.abc.de/1", 2010, 1, 12, "Title", "Short Text");
        
        assertEquals("site1", listener.createdSiteName);
        assertEquals("1", listener.createdArticleId);
        Article article = articleStore.getArticle(path);
        assertEquals("1", article.getExternalId());
        assertEquals("http://www.abc.de/1", article.getLocation());
        assertEquals(2010, article.getPublishedDate().get(Calendar.YEAR));
        assertEquals(1, article.getPublishedDate().get(Calendar.MONTH));
        assertEquals(12, article.getPublishedDate().get(Calendar.DAY_OF_MONTH));
        assertEquals("Short Text", article.getShortText());
        assertEquals("Title", article.getTitle());
    }
    
    @Test
    public void testUpdateSiteArticleChangeContent() throws StoreException, MalformedURLException {
        Site site = createSite("site2");
        
        List<TextBlock> text = new ArrayList<TextBlock>();
        text.add(new TextBlock("Hello world!", 2));
        text.add(new TextBlock("Another hello world!", 3));
        String path = updateArticle(site, "1", "http://www.abc.de/1", 2010, 1, 12, "Short Text", "Title", text);
        
        assertEquals("site2", listener.createdSiteName);
        assertEquals("1", listener.createdArticleId);
        Article article = articleStore.getArticle(path);
        article.setContent(articleStore.getContent(path));
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
    public void testGetArticlesOrderedByDate() throws StoreException, MalformedURLException, RepositoryException {
        Site site = createSite("http://www.abc.de");
        
        String path1 = updateArticle(site, "http://www.abc.de/ID_1", "http://www.abc.de/1", 2010, 1, 12);
        String path2 = updateArticle(site, "http://www.abc.de/ID_2", "http://www.abc.de/2", 2011, 2, 1);
        String path3 = updateArticle(site, "http://www.abc.de/ID_3", "http://www.abc.de/3", 1999, 12, 23);
        
        Site site2 = createSite("site4");
        String path4 = updateArticle(site2, "http://www.xyz.de/ID_4", "http://www.xyz.de/4", 2012, 12, 23);
        
        List<EntityKey> articles = articleStore.getArticlesOrderedByDate(site);
        assertEquals(3, articles.size());
        assertEquals(path2, articles.get(0).getKey());
        assertEquals(path1, articles.get(1).getKey());
        assertEquals(path3, articles.get(2).getKey());
        
        articles = articleStore.getArticlesOrderedByDate(site2);
        assertEquals(1, articles.size());
        assertEquals(path4, articles.get(0).getKey());
        
        WorkingSet workingSet = new WorkingSet();
        workingSet.setName("Default");
        workingSet.getSites().add(new EntityKey(site.getName(), JcrSiteStore.getSitePath(site.getName())));
        workingSet.getSites().add(new EntityKey(site2.getName(), JcrSiteStore.getSitePath(site2.getName())));
        workingSetStore.update(workingSet);
        
        articles = articleStore.getArticlesOrderedByDate(workingSet);
        assertEquals(4, articles.size());
        assertEquals(path4, articles.get(0).getKey());
        assertEquals(path2, articles.get(1).getKey());
        assertEquals(path1, articles.get(2).getKey());
        assertEquals(path3, articles.get(3).getKey());
    }
    
/*    @Test
    public void testGetRelatedArticlesOrderedByRelevance() throws StoreException, RepositoryException {
    	Site site = createSite("http://www.abc.de");
    	String path1 = updateArticle(site, "1", "http://www.abc.de/1", 2010, 1, 12, "Exception Store Car Cars Store Taxi", "Title");
    	String path2 = updateArticle(site, "2", "http://www.abc.de/2", 2010, 1, 12, "Exception Store Car Cars Store Taxi", "Title");
    	String path3 = updateArticle(site, "3", "http://www.abc.de/3", 2010, 1, 12, "Exception Store Car Cars Store Taxi", "Title");
    	String path4 = updateArticle(site, "4", "http://www.abc.de/4", 2010, 1, 12, "Exception Store Car Cars Store Taxi", "Title");
    	String path5 = updateArticle(site, "5", "http://www.abc.de/5", 2010, 1, 12, "Exception Store Car Cars Store Taxi", "Title");
    	String path6 = updateArticle(site, "6", "http://www.abc.de/6", 2010, 1, 12, "Exception Store Car Cars Store Taxi", "Title");

    	articleStore.dumpContent();
    	
    	List<EntityKey> related = articleStore.selectSimilarArticles(path1);
    	for (EntityKey entityKey : related) {
			System.out.println(entityKey);
		}
    }*/

    @Test
    public void testGetContent() throws StoreException, MalformedURLException {
        Site site = createSite("site5");
        
        List<TextBlock> content = new ArrayList<TextBlock>();
        content.add(new TextBlock("This are four words", 4));
        content.add(new TextBlock("foo ba", 2));
        content.add(new TextBlock("Hello world!", 2));
        String path = updateArticle(site, "1", "http://www.abc.de/1", 2012, 12, 23, null, null, content);
        
        content = articleStore.getContent(path);
        assertEquals(3, content.size());
        assertEquals("This are four words", content.get(0).getContent());
        assertEquals(4, content.get(0).getNumWords());
        assertEquals("foo ba", content.get(1).getContent());
        assertEquals("Hello world!", content.get(2).getContent());
    }
}
