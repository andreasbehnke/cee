package org.cee.webreader.client.content;

import org.cee.news.model.EntityKey;
import org.cee.webreader.client.content.SiteData.SiteRetrivalState;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Bean holding all view data of a feet
 */
public class FeedData implements IsSerializable {

	private boolean isNew = true;

	private String location;

	private String title;

	private boolean active;
	
	private SiteRetrivalState state;
	
	private EntityKey language;

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

	public boolean getIsActive() {
		return active;
	}

	public void setIsActive(boolean active) {
		this.active = active;
	}

	public EntityKey getLanguage() {
		return language;
	}

	public void setLanguage(EntityKey language) {
		this.language = language;
	}
}
