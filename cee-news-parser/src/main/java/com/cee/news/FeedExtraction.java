package com.cee.news;

import com.cee.news.model.Feed;

public class FeedExtraction {
	
	private final Feed feed;
	
	private final StringBuilder feedContent;

	public FeedExtraction(final Feed feed) {
	    this.feed = feed;
	    this.feedContent = new StringBuilder();
    }

	public Feed getFeed() {
		return feed;
	}

	public StringBuilder getContent() {
		return feedContent;
	}
}
