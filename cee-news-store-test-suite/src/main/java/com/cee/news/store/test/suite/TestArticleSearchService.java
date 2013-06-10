package com.cee.news.store.test.suite;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.cee.news.search.ArticleSearchService;
import com.cee.news.search.SearchException;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;

public abstract class TestArticleSearchService extends TestStoreBase {
	
	private static final String TEXT = "Cars Economics Year 2011 electric energy Cars Economics Year 2011 electric energy Cars Economics Year 2011 electric energy ";

	private static final String SCORE1 = "Cars Economics Year 2011 electric energy Cars Economics Year 2011 electric energy Cars Economics Year 2011 electric energy ";

	private static final String SCORE2 = "Economics Year 2011 electric energy Cars Economics Year 2011 electric energy Economics Year 2011 electric energy ";

	private static final String SCORE3 = "Year 2011 electric energy Cars Economics Year 2011 electric energy Year 2011 electric energy ";

	private static final String SCORE5 = "Year 2011 electric energy Year 2011 electric energy Year 2011 electric energy ";

	private static final String UNRELATED = "Food Issue dog eat cats alambama";
	
	@Test
	public void testFindRelatedArticles() throws StoreException, SearchException {
		SiteStore siteStore = getSiteStore();
		ArticleStore articleStore = getArticleStore();
		ArticleSearchService articleSearchService = getArticleSearchService();
		EntityKey site = Utils.createSite(siteStore, "site1");
		EntityKey site2 = Utils.createSite(siteStore, "site2");
		ArticleKey key1 = Utils.updateArticle(articleStore, site, "1", "http://www.abc.de/1", 2010, 1, 12, "Title", TEXT);
		ArticleKey key2 = Utils.updateArticle(articleStore, site, "2", "http://www.abc.de/2", 2010, 1, 12, "Title", SCORE2);
		ArticleKey key3 = Utils.updateArticle(articleStore, site, "3", "http://www.abc.de/3", 2010, 1, 12, "Title", SCORE1);
		Utils.updateArticle(articleStore, site2, "4", "http://www.xyz.de/4", 2010, 1, 12, "Title", SCORE3);
		ArticleKey key5 = Utils.updateArticle(articleStore, site, "5", "http://www.abc.de/5", 2010, 1, 12, "Title", SCORE5);
		Utils.updateArticle(articleStore, site, "6", "http://www.abc.de/6", 2010, 1, 12, "Title", UNRELATED);
	
		List<EntityKey> sites = new ArrayList<EntityKey>();
		sites.add(site);
		List<ArticleKey> related = articleSearchService.findRelatedArticles(sites, key1, "en");
		Assert.assertEquals(3, related.size());
		Assert.assertTrue(related.contains(key2));
		Assert.assertTrue(related.contains(key3));
		Assert.assertTrue(related.contains(key5));
	}
	
	@Test
    public void testFindArticles() throws StoreException, SearchException {
		SiteStore siteStore = getSiteStore();
		ArticleStore articleStore = getArticleStore();
		ArticleSearchService articleSearchService = getArticleSearchService();
		EntityKey site = Utils.createSite(siteStore, "http://www.abc.de");
        EntityKey site2 = Utils.createSite(siteStore, "site2");
        Utils.updateArticle(articleStore, site, "1", "http://www.abc.de/1", 2010, 1, 12, "Title", TEXT);
        Utils.updateArticle(articleStore, site, "2", "http://www.abc.de/2", 2010, 1, 12, "Title", SCORE2);
        Utils.updateArticle(articleStore, site, "3", "http://www.abc.de/3", 2010, 1, 12, "Title", SCORE1);
        Utils.updateArticle(articleStore, site2, "4", "http://www.xyz.de/4", 2010, 1, 12, "Title", SCORE3);
        Utils.updateArticle(articleStore, site, "5", "http://www.abc.de/5", 2010, 1, 12, "Title", SCORE5);
        ArticleKey path6 = Utils.updateArticle(articleStore, site, "6", "http://www.abc.de/6", 2010, 1, 12, "Title", UNRELATED);
    
        List<EntityKey> sites = new ArrayList<EntityKey>();
        sites.add(site);
        sites.add(site2);
        List<ArticleKey> result = articleSearchService.findArticles(sites, "Food Issue", "en");
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(path6));
        result = articleSearchService.findArticles(sites, "Year electric", "en");
        Assert.assertEquals(5, result.size());
        result = articleSearchService.findArticles(sites, "Year OR Dog", "en");
        Assert.assertEquals(6, result.size());
	}
    
}
