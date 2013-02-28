package com.cee.news.store.jcr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;

import org.apache.jackrabbit.util.Text;
import org.junit.Test;

import com.cee.news.model.EntityKey;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.store.StoreException;

public class TestJcrSiteStore extends JcrTestBase {

    @Test
    public void testUpdateSite() throws LoginException, RepositoryException, MalformedURLException, StoreException {
        Site site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.spiegel.de/blabla/test/test.jsp?id=52643584");
        site.setLanguage("de");
        site.setName("http://www.spiegel.de");
        site.setTitle("Title");
        List<Feed> feeds = new ArrayList<Feed>();
        Feed feed = new Feed("http://www.spiegel.de/feed1.rss", "feed1", "application/xml");
        feed.setActive(true);
        feeds.add(feed);
        feeds.add(new Feed("http://www.spiegel.de/feed2.rss", "feed2", "application/rss"));
        site.setFeeds(feeds);
        EntityKey siteKey = siteStore.update(site);
        
        assertEquals("http://www.spiegel.de", siteKey.getName());
        assertEquals("http://www.spiegel.de", siteKey.getKey());
        site = siteStore.getSite(siteKey);
        assertEquals("Description", site.getDescription());
        assertEquals("http://www.spiegel.de/blabla/test/test.jsp?id=52643584", site.getLocation());
        assertEquals("http://www.spiegel.de", site.getName());
        assertEquals("Title", site.getTitle());
        assertEquals("de", site.getLanguage());
        feeds = site.getFeeds();
        assertEquals(2, feeds.size());
        Map<String, Feed> feedMap = new HashMap<String, Feed>();
        feedMap.put(feeds.get(0).getTitle(), feeds.get(0));
        feedMap.put(feeds.get(1).getTitle(), feeds.get(1));
        Feed feed1 = feedMap.get("feed1");
        assertEquals("application/xml", feed1.getContentType());
        assertEquals("http://www.spiegel.de/feed1.rss", feed1.getLocation());
        assertTrue(feed1.isActive());
        Feed feed2 = feedMap.get("feed2");
        assertEquals("application/rss", feed2.getContentType());
        assertEquals("http://www.spiegel.de/feed2.rss", feed2.getLocation());
        assertFalse(feed2.isActive());
        
        //change site
        site.setDescription("Description123");
        site.setTitle("Title123");
        site.setLanguage("en");
        feeds = new ArrayList<Feed>();
        feeds.add(new Feed("http://www.tageschau.de/feed.rss", "feed1", "application/xml"));
        site.setFeeds(feeds);
        siteStore.update(site);
        
        site = siteStore.getSite(siteKey);
        assertEquals("Description123", site.getDescription());
        assertEquals("http://www.spiegel.de/blabla/test/test.jsp?id=52643584", site.getLocation());
        assertEquals("Title123", site.getTitle());
        assertEquals("en", site.getLanguage());
        feeds = site.getFeeds();
        assertEquals(1, feeds.size());
        feedMap = new HashMap<String, Feed>();
        feedMap.put(feeds.get(0).getTitle(), feeds.get(0));
        feed1 = feedMap.get("feed1");
        assertEquals("application/xml", feed1.getContentType());
        assertEquals("http://www.tageschau.de/feed.rss", feed1.getLocation());
        
        //create site with null description and title
        String name = "www.blablabla.com";
        site = new Site();
        site.setName(name);
        site.setLocation(name);
        siteKey = siteStore.update(site);
        
        site = siteStore.getSite(siteKey);
        assertNull(site.getDescription());
        assertNull(site.getTitle());
        assertNull(site.getLanguage());
    }
    
    @Test
    public void testGetSiteMissing() throws StoreException, MalformedURLException {
        assertNull(siteStore.getSite(EntityKey.get("http://www.blablabla.de")));
    }
    
    @Test
    public void testGetSites() throws StoreException, MalformedURLException {
    	Site site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.site1.de");
        site.setName("http://www.site1.de");
        site.setTitle("Title");
        EntityKey key1 = siteStore.update(site);
        site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.site2.de");
        site.setName("http://www.site2.de");
        site.setTitle("Title");
        EntityKey key2 = siteStore.update(site);
        
        List<EntityKey> keys = new ArrayList<EntityKey>();
        keys.add(key2);
        keys.add(key1);
        
        List<Site> sites = siteStore.getSites(keys);
        assertEquals(2, sites.size());
        assertEquals("http://www.site2.de", sites.get(0).getName());
        assertEquals("http://www.site1.de", sites.get(1).getName());
    }

    @Test
    public void testGetSitesOrderedByName() throws StoreException, MalformedURLException, LoginException, RepositoryException {
        Site site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.bbb.de");
        site.setName("http://www.bbb.de");
        site.setTitle("Title");
        siteStore.update(site);
        site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.ccc.de");
        site.setName("http://www.ccc.de");
        site.setTitle("Title");
        siteStore.update(site);
        site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.abc.de");
        site.setName("http://www.abc.de");
        site.setTitle("Title");
        siteStore.update(site);
        
        List<EntityKey> sites = siteStore.getSitesOrderedByName();
        assertEquals("http://www.abc.de", sites.get(0).getName());
        assertEquals("http://www.abc.de", sites.get(0).getKey());
        assertEquals("http://www.bbb.de", sites.get(1).getName());
        assertEquals("http://www.ccc.de", sites.get(2).getName());
    }
}
