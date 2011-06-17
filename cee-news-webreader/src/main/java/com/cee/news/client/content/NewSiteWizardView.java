package com.cee.news.client.content;

import java.util.List;

import com.cee.news.model.Feed;
import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * The view of the {@link AddSiteWorkflow}
 */
public interface NewSiteWizardView {

	/**
	 * Display the location input dialog
	 */
	void showPageLocationInput();
	
	/**
	 * Display the feed selection dialog
	 */
	void showPageFeedSelection();
	
	HasClickHandlers getButtonNext();
	
	HasClickHandlers getButtonCancel();
	
	HasClickHandlers getButtonFinish();
	
	/**
	 * @return User's location input
	 */
	String getLocationInput();
	
	/**
	 * @param name The site name hint
	 */
	void setSiteName(String name);
	
	/**
	 * @return User's site name input
	 */
	String getSiteName();
	
	/**
	 * @param feeds Feeds which should be selected by user
	 */
	void setFeeds(List<Feed> feeds);
}
