package org.cee.webreader.client.content;

import java.util.List;

import org.cee.news.model.EntityKey;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Bean holding all view data of a site
 */
public class SiteData implements IsSerializable {

	public enum SiteRetrivalState {
		ok, malformedUrl, ioError, parserError
	}

	private SiteRetrivalState state;

	private boolean isNew = true;

	private String name;

	private String location;

	private String title;

	private String description;
	
	private EntityKey language;

	private List<FeedData> feeds;

	public boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	public SiteRetrivalState getState() {
		return state;
	}

	public void setState(SiteRetrivalState state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<FeedData> getFeeds() {
		return feeds;
	}

	public void setFeeds(List<FeedData> feeds) {
		this.feeds = feeds;
	}

	public EntityKey getLanguage() {
		return language;
	}

	public void setLanguage(EntityKey language) {
		this.language = language;
	}
}
