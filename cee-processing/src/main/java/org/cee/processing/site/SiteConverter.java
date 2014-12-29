package org.cee.processing.site;

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

import java.util.ArrayList;
import java.util.List;

import org.cee.news.model.EntityKey;
import org.cee.news.model.Feed;
import org.cee.news.model.Site;
import org.cee.processing.site.model.FeedData;
import org.cee.processing.site.model.SiteData;

public class SiteConverter {

	private SiteConverter() {}
	
	public static FeedData createFromFeed(final Feed feed) {
		FeedData data = new FeedData();
		data.setIsNew(true);
		data.setIsActive(feed.isActive());
		data.setLocation(feed.getLocation());
		data.setTitle(feed.getTitle());
		String language = feed.getLanguage();
		if (language != null) {
			data.setLanguage(EntityKey.get(language));
		}
		return data;
	}
	
	public static SiteData createFromSite(final Site site) {
		SiteData data = new SiteData();
		data.setIsNew(true);
		data.setDescription(site.getDescription());
		data.setLocation(site.getLocation());
		data.setName(site.getName());
		data.setTitle(site.getTitle());
		String language = site.getLanguage();
		if (language != null) {
			data.setLanguage(EntityKey.get(language));
		}
		List<FeedData> feeds = new ArrayList<FeedData>();
		for (Feed feed : site.getFeeds()) {
			feeds.add(createFromFeed(feed));
		}
		data.setFeeds(feeds);
		return data;
	}
	
	public static Feed createFromFeedData(FeedData data) {
		Feed feed = new Feed();
		feed.setActive(data.getIsActive());
		feed.setLocation(data.getLocation());
		feed.setTitle(data.getTitle());
		feed.setLanguage(data.getLanguage().getKey());
		return feed;
	}
	
	public static Site createFromSiteData(SiteData data) {
		Site site = new Site();
		site.setDescription(data.getDescription());
		site.setLocation(data.getLocation());
		site.setName(data.getName());
		site.setTitle(data.getTitle());
		site.setLanguage(data.getLanguage().getKey());
		List<Feed> feeds = new ArrayList<Feed>();
		for (FeedData feed : data.getFeeds()) {
			feeds.add(createFromFeedData(feed));
		}
		site.setFeeds(feeds);
		return site;
	}
	
}
