package com.cee.news;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.cee.news.model.Site;

/**
 * Holds site information extracted from site resource.
 */
public class SiteExtraction implements HasContent {

	private final Site site;
	
	private final StringBuilder siteContent;
	
	private final List<URL> feedLocations;

	public SiteExtraction() {
		this.site = new Site();
		this.siteContent = new StringBuilder();
		this.feedLocations = new ArrayList<URL>();
	}
	
	public Site getSite() {
		return site;
	}

	public StringBuilder getContent() {
		return siteContent;
	}
	
	public List<URL> getFeedLocations() {
	    return feedLocations;
    }
}
