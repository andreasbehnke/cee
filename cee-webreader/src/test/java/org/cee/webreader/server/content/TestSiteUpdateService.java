package org.cee.webreader.server.content;

/*
 * #%L
 * News Reader
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

import org.cee.news.model.Feed;
import org.cee.news.model.Site;
import org.cee.processing.site.model.SiteData;
import org.cee.processing.site.model.SiteData.SiteRetrivalState;
import org.cee.webreader.client.content.SiteUpdateService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/testContext.xml"})
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class TestSiteUpdateService {
	
	private static final String SITE_URL = "http://www.test.com/content/index.html";

	private static final String SITE_MALFORMED_URL = "xyz://www.test.com/content/index.html";

	private static final String MISSING_SITE_URL = "http://www.test.com/content/unknown.html";
	
	private static final String FEED_TITLE = "Dev User Stories";

	private static final String FEED_URL = "http://www.test.com/content/site1/feed1.rss";

	private static final String TEST_SITE = "testSite";
	
	@Autowired
	private SiteUpdateService siteUpdateService;
	
	protected Site createSite() {
		Site site = new Site();
		site.setName(TEST_SITE);
		Feed feed = new Feed(FEED_URL, FEED_TITLE);
		feed.setActive(true);
		site.getFeeds().add(feed);
		site.setLocation(SITE_URL);
		return site;
	}
	
	@Test
	public void testRetrieveSiteDataOk() {
		SiteData siteData = siteUpdateService.retrieveSiteData(SITE_URL);
		Assert.assertEquals(SiteRetrivalState.ok, siteData.getState());
		Assert.assertEquals("Site 1", siteData.getTitle());
		Assert.assertEquals(2, siteData.getFeeds().size());
	}
	
	@Test
	public void testRetrieveSiteDataMalformedUrlError() {
		SiteData siteData = siteUpdateService.retrieveSiteData(SITE_MALFORMED_URL);
		Assert.assertEquals(SiteRetrivalState.malformedUrl, siteData.getState());
	}
	
	@Test
	public void testRetrieveSiteDataIoError() {
		SiteData siteData = siteUpdateService.retrieveSiteData(MISSING_SITE_URL);
		Assert.assertEquals(SiteRetrivalState.ioError, siteData.getState());
	}
}
