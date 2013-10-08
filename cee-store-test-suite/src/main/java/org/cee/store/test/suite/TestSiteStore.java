package org.cee.store.test.suite;

/*
 * #%L
 * Content Extraction Engine - News Store Test Suite
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cee.news.model.EntityKey;
import org.cee.news.model.Feed;
import org.cee.news.model.Site;
import org.cee.news.store.SiteStore;
import org.cee.news.store.StoreException;
import org.junit.Test;

public abstract class TestSiteStore extends TestStoreBase {

	@Test
    public void testUpdateSite() throws StoreException {
		SiteStore siteStore = getSiteStore();
		
        Site site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.spiegel.de/blabla/test/test.jsp?id=52643584");
        site.setLanguage("de");
        site.setName("http://www.spiegel.de");
        site.setTitle("Title");
        List<Feed> feeds = new ArrayList<Feed>();
        Feed feed = new Feed("http://www.spiegel.de/feed1.rss", "feed1");
        feed.setActive(true);
        feeds.add(feed);
        feeds.add(new Feed("http://www.spiegel.de/feed2.rss", "feed2"));
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
        assertEquals("http://www.spiegel.de/feed1.rss", feed1.getLocation());
        assertTrue(feed1.isActive());
        Feed feed2 = feedMap.get("feed2");
        assertEquals("http://www.spiegel.de/feed2.rss", feed2.getLocation());
        assertFalse(feed2.isActive());
        
        //change site
        site.setDescription("Description123 Text");
        site.setTitle("Title123 Text");
        site.setLanguage("en");
        feeds = new ArrayList<Feed>();
        feeds.add(new Feed("http://www.tageschau.de/feed.rss", "feed1"));
        site.setFeeds(feeds);
        siteStore.update(site);
        
        site = siteStore.getSite(siteKey);
        assertEquals("Description123 Text", site.getDescription());
        assertEquals("http://www.spiegel.de/blabla/test/test.jsp?id=52643584", site.getLocation());
        assertEquals("Title123 Text", site.getTitle());
        assertEquals("en", site.getLanguage());
        feeds = site.getFeeds();
        assertEquals(1, feeds.size());
        feedMap = new HashMap<String, Feed>();
        feedMap.put(feeds.get(0).getTitle(), feeds.get(0));
        feed1 = feedMap.get("feed1");
        assertEquals("http://www.tageschau.de/feed.rss", feed1.getLocation());
        
        //create site with null description and title
        String name = "www.blablabla.com";
        site = new Site();
        site.setName(name);
        site.setLocation(name);
        site.setLanguage("de");
        siteKey = siteStore.update(site);
        
        site = siteStore.getSite(siteKey);
        assertNull(site.getDescription());
        assertNull(site.getTitle());
    }
    
    @Test
    public void testGetSiteMissing() throws StoreException {
    	SiteStore siteStore = getSiteStore();
        assertNull(siteStore.getSite(EntityKey.get("http://www.blablabla.de")));
    }
    
    @Test
    public void testGetSites() throws StoreException {
    	SiteStore siteStore = getSiteStore();
    	Site site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.site1.de");
        site.setName("http://www.site1.de");
        site.setTitle("Title");
        site.setLanguage("en");
        EntityKey key1 = siteStore.update(site);
        site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.site2.de");
        site.setName("http://www.site2.de");
        site.setTitle("Title");
        site.setLanguage("en");
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
    public void testGetSitesOrderedByName() throws StoreException {
    	SiteStore siteStore = getSiteStore();
        Site site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.bbb.de");
        site.setName("http://www.bbb.de");
        site.setTitle("Title");
        site.setLanguage("en");
        siteStore.update(site);
        site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.ccc.de");
        site.setName("http://www.ccc.de");
        site.setTitle("Title");
        site.setLanguage("en");
        siteStore.update(site);
        site = new Site();
        site.setDescription("Description");
        site.setLocation("http://www.abc.de");
        site.setName("http://www.abc.de");
        site.setTitle("Title");
        site.setLanguage("en");
        siteStore.update(site);
        
        List<EntityKey> sites = siteStore.getSitesOrderedByName();
        assertEquals("http://www.abc.de", sites.get(0).getName());
        assertEquals("http://www.abc.de", sites.get(0).getKey());
        assertEquals("http://www.bbb.de", sites.get(1).getName());
        assertEquals("http://www.ccc.de", sites.get(2).getName());
    }
}
