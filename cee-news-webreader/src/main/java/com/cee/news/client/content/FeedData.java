package com.cee.news.client.content;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Bean holding all view data of a feet
 */
public class FeedData implements IsSerializable {

	private boolean isNew = true;

	private String location;

	private String contentType;

	private String title;

	private boolean active;

	public boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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
}
