package com.cee.news.language.impl;

import com.cee.news.SiteExtraction;
import com.cee.news.language.LanguageDetector;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;

/**
 * Language detector which uses information provided by sites feed to determine site's language
 */
public class FeedLanguageDetector implements LanguageDetector {

	@Override
	public String detect(SiteExtraction siteExtraction) {
		Site site = siteExtraction.getSite();
	    for (Feed feed : site.getFeeds()) {
	    	String language = feed.getLanguage(); 
	        if (language != null) {
	        	return language;
	        }
        }
	    return null;
	}
}
