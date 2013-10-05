package org.cee.webreader.server.content;

import java.util.ArrayList;
import java.util.List;

import org.cee.news.model.EntityKey;
import org.cee.news.model.Feed;
import org.cee.news.model.Site;
import org.cee.webreader.client.content.FeedData;
import org.cee.webreader.client.content.SiteData;

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
