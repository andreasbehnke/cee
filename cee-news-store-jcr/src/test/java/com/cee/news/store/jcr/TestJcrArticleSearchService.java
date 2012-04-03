package com.cee.news.store.jcr;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;
import com.cee.news.search.SearchException;
import com.cee.news.store.StoreException;

public class TestJcrArticleSearchService extends JcrTestBase {
	
	private static final String TEXT = "Cars Economics Year 2011 electric energy Cars Economics Year 2011 electric energy Cars Economics Year 2011 electric energy ";

	private static final String SCORE1 = "Cars Economics Year 2011 electric energy Cars Economics Year 2011 electric energy Cars Economics Year 2011 electric energy ";

	private static final String SCORE2 = "Economics Year 2011 electric energy Cars Economics Year 2011 electric energy Economics Year 2011 electric energy ";

	private static final String SCORE3 = "Year 2011 electric energy Cars Economics Year 2011 electric energy Year 2011 electric energy ";

	private static final String SCORE5 = "Year 2011 electric energy Year 2011 electric energy Year 2011 electric energy ";

	private static final String UNRELATED = "Food Issue dog eat cats alambama";

	@Test
	public void testFindRelatedArticles() throws StoreException, SearchException {
		EntityKey site = createSite("site1");
		EntityKey site2 = createSite("site2");
		ArticleKey path1 = updateArticle(site, "1", "http://www.abc.de/1", 2010, 1, 12, "Title", TEXT);
		ArticleKey path2 = updateArticle(site, "2", "http://www.abc.de/2", 2010, 1, 12, "Title", SCORE2);
		ArticleKey path3 = updateArticle(site, "3", "http://www.abc.de/3", 2010, 1, 12, "Title", SCORE1);
		updateArticle(site2, "4", "http://www.xyz.de/4", 2010, 1, 12, "Title", SCORE3);
		ArticleKey path5 = updateArticle(site, "5", "http://www.abc.de/5", 2010, 1, 12, "Title", SCORE5);
		updateArticle(site, "6", "http://www.abc.de/6", 2010, 1, 12, "Title", UNRELATED);
	
		List<EntityKey> sites = new ArrayList<EntityKey>();
		sites.add(site);
		List<ArticleKey> related = articleSearchService.findRelatedArticles(sites, path1);
		Assert.assertEquals(3, related.size());
		Assert.assertTrue(related.contains(path2));
		Assert.assertTrue(related.contains(path3));
		Assert.assertTrue(related.contains(path5));
	}
	
	@Test
    public void testFindArticles() throws StoreException, SearchException {
        EntityKey site = createSite("http://www.abc.de");
        EntityKey site2 = createSite("site2");
        ArticleKey key1 = updateArticle(site, "1", "http://www.abc.de/1", 2010, 1, 12, "Title", TEXT);
        ArticleKey key2 = updateArticle(site, "2", "http://www.abc.de/2", 2010, 1, 12, "Title", SCORE2);
        ArticleKey key3 = updateArticle(site, "3", "http://www.abc.de/3", 2010, 1, 12, "Title", SCORE1);
        updateArticle(site2, "4", "http://www.xyz.de/4", 2010, 1, 12, "Title", SCORE3);
        ArticleKey key5 = updateArticle(site, "5", "http://www.abc.de/5", 2010, 1, 12, "Title", SCORE5);
        ArticleKey path6 = updateArticle(site, "6", "http://www.abc.de/6", 2010, 1, 12, "Title", UNRELATED);
    
        List<EntityKey> sites = new ArrayList<EntityKey>();
        sites.add(site);
        sites.add(site2);
        List<ArticleKey> result = articleSearchService.findArticles(sites, "Food Issue");
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(path6));
        result = articleSearchService.findArticles(sites, "Year electric");
        Assert.assertEquals(5, result.size());
        result = articleSearchService.findArticles(sites, "Year OR Dog");
        Assert.assertEquals(6, result.size());
	}
    
}
