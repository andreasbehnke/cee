package org.cee.news.model;


public class Feed {

    private String location;
    
    private String language;

    private String title;

    private boolean active;
    
    public Feed() {}

    public Feed(String location, String title) {
        this.location = location;
        this.title = title;
        active = false;
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
    
    public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
	
	@Override
	public String toString() {
	    return "Feed [location=" + location + "; title=" + title + "]";
	}
}