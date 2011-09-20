package com.cee.news.store.jcr;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;
import com.cee.news.search.SearchException;
import com.cee.news.store.StoreException;

public class TestJcrArticleSearchService extends JcrTestBase {
	
	private static final String SIMILAR_TERM = "Cars Economics Year 2011 electric energy Cars Economics Year 2011 electric energy Cars Economics Year 2011 electric energy ";

	@Test
	public void testGetRelatedArticles() throws StoreException, SearchException {
		Site site = createSite("site1");
		String path1 = updateArticle(site, "1", "http://www.abc.de/1", 2010, 1, 12, "Title", SIMILAR_TERM);
		String path2 = updateArticle(site, "2", "http://www.abc.de/2", 2010, 1, 12, "Title", SIMILAR_TERM);
		String path3 = updateArticle(site, "3", "http://www.abc.de/3", 2010, 1, 12, "Title", SIMILAR_TERM);
		String path4 = updateArticle(site, "4", "http://www.abc.de/4", 2010, 1, 12, "Title", SIMILAR_TERM);
		String path5 = updateArticle(site, "5", "http://www.abc.de/5", 2010, 1, 12, "Title", SIMILAR_TERM);
		String path6 = updateArticle(site, "6", "http://www.abc.de/6", 2010, 1, 12, "Title", SIMILAR_TERM);
	
		List<String> sites = new ArrayList<String>();
		sites.add(site.getName());
		List<EntityKey> related = articleSearchService.findRelatedArticles(sites, path1);
		Assert.assertEquals(5, related.size());
	}
}
