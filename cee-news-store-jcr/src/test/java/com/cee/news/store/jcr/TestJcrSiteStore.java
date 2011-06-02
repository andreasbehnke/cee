package com.cee.news.store.jcr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.store.StoreException;

public class TestJcrSiteStore extends JcrTestBase {

    private static JcrSiteStore siteStore;
    
    @BeforeClass
    public static void setupStores() throws LoginException, RepositoryException, StoreException {
        setupSession();
        siteStore = new JcrSiteStore(session);
    }
    
    @AfterClass
    public static void close() {
        closeSession();
    }
    
    @Test
    public void testUpdateSite() throws LoginException, RepositoryException, MalformedURLException, StoreException {
        Site site = new Site();
        site.setDescription("Description");
        String url = "http://www.spiegel.de/blabla/test/test.jsp?id=52643584";
        site.setLocation(url);
        site.setTitle("Title");
        List<Feed> feeds = new ArrayList<Feed>();
        feeds.add(new Feed("http://www.spiegel.de/feed1.rss", "feed1", "application/xml"));
        feeds.add(new Feed("http://www.spiegel.de/feed2.rss", "feed2", "application/rss"));
        site.setFeeds(feeds);
        siteStore.update(site);
        
        site = siteStore.getSite(url);
        assertEquals("Description", site.getDescription());
        assertEquals(url, site.getLocation());
        assertEquals("Title", site.getTitle());
        feeds = site.getFeeds();
        assertEquals(2, feeds.size());
        Map<String, Feed> feedMap = new HashMap<String, Feed>();
        feedMap.put(feeds.get(0).getTitle(), feeds.get(0));
        feedMap.put(feeds.get(1).getTitle(), feeds.get(1));
        Feed feed1 = feedMap.get("feed1");
        assertEquals("application/xml", feed1.getContentType());
        assertEquals("http://www.spiegel.de/feed1.rss", feed1.getLocation());
        Feed feed2 = feedMap.get("feed2");
        assertEquals("application/rss", feed2.getContentType());
        assertEquals("http://www.spiegel.de/feed2.rss", feed2.getLocation());
        
        //change site
        site.setDescription("Description123");
        site.setTitle("Title123");
        feeds = new ArrayList<Feed>();
        feeds.add(new Feed("http://www.tageschau.de/feed.rss", "feed1", "application/xml"));
        site.setFeeds(feeds);
        siteStore.update(site);
        
        site = siteStore.getSite(url);
        assertEquals("Description123", site.getDescription());
        assertEquals(url, site.getLocation());
        assertEquals("Title123", site.getTitle());
        feeds = site.getFeeds();
        assertEquals(1, feeds.size());
        feedMap = new HashMap<String, Feed>();
        feedMap.put(feeds.get(0).getTitle(), feeds.get(0));
        feed1 = feedMap.get("feed1");
        assertEquals("application/xml", feed1.getContentType());
        assertEquals("http://www.tageschau.de/feed.rss", feed1.getLocation());
        
        //create site with null description and title
        url = "www.blablabla.com";
        site = new Site();
        site.setLocation(url);
        siteStore.update(site);
        
        site = siteStore.getSite(url);
        assertNull(site.getDescription());
        assertNull(site.getTitle());
    }
    
    @Test
    public void testGetSite() throws StoreException, MalformedURLException {
        assertNull(siteStore.getSite("http://www.blablabla.de"));
    }

    @Test
    public void testGetSitesOrderedByUrl() throws StoreException, MalformedURLException, LoginException, RepositoryException {
        Site site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.bbb.de");
        site.setTitle("Title");
        siteStore.update(site);
        site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.ccc.de");
        site.setTitle("Title");
        siteStore.update(site);
        site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.abc.de");
        site.setTitle("Title");
        siteStore.update(site);
        
        List<String> sites = siteStore.getSitesOrderedByLocation();
        assertEquals("http://www.abc.de", sites.get(0));
        assertEquals("http://www.bbb.de", sites.get(1));
        assertEquals("http://www.ccc.de", sites.get(2));
    }
}
