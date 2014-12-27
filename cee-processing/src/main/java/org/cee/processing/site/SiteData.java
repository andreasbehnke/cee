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

import java.io.Serializable;
import java.util.List;

import org.cee.news.model.EntityKey;

/**
 * Bean holding all view data of a site
 */
public class SiteData implements Serializable {

	private static final long serialVersionUID = 1L;

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
