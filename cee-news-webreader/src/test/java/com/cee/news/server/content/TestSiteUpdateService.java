package com.cee.news.server.content;

import java.util.List;

import javax.jcr.RepositoryException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cee.news.client.content.SiteUpdateService;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;
import com.cee.news.store.jcr.JcrArticleStore;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/testContext.xml"})
public class TestSiteUpdateService {

	private static final String SITE_URL = "http://www.test.com/content/site1.html";

	private static final String FEED_TYPE = "application/rss+xml";

	private static final String FEED_TITLE = "Dev User Stories";

	private static final String FEED_URL = "http://www.test.com/content/site1/feed1.rss";

	private static final String TEST_SITE = "testSite";
	
	@Autowired
	private SiteUpdateService siteUpdateService;
	
	@Autowired
	@Qualifier("siteStore")
	private SiteStore siteStore;
	
	@Autowired
	@Qualifier("articleStore")
	private JcrArticleStore articleStore;
	
	protected Site createSite() {
		Site site = new Site();
		site.setName(TEST_SITE);
		Feed feed = new Feed(FEED_URL, FEED_TITLE, FEED_TYPE);
		feed.setActive(true);
		site.getFeeds().add(feed);
		site.setLocation(SITE_URL);
		return site;
	}
	
	@Test
	public void testSiteUpdate() throws StoreException, InterruptedException, RepositoryException {
		Site site = createSite();
		siteStore.update(site);
		
		Assert.assertEquals(1, siteUpdateService.addSiteToUpdateQueue(TEST_SITE));
		
		while(siteUpdateService.getUpdateTasks() > 0) {
			Thread.sleep(100);
		}
		 
		articleStore.dumpContent();
		 
		List<EntityKey> keys = articleStore.getArticlesOrderedByDate(site);
		Assert.assertEquals(4, keys.size());
	}
}
