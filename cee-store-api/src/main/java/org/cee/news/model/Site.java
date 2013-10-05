package org.cee.news.model;

import java.util.ArrayList;
import java.util.List;

public class Site {
	
	private String name;
	
	private String language;

    private String location;

    private String title;

    private String description;

    private List<Feed> feeds = new ArrayList<Feed>();

    /**
     * @return The unique name of the site
     */
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

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }
    
    /**
     * @return the ISO 639-1 Language Code of this working set
     */
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	@Override
	public String toString() {
	    return "Site [location=" + location + "; name=" + name + "; language=" + language + "]";
	}
}