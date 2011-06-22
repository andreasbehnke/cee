package com.cee.news.client.content;

import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;

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
	
	HasClickHandlers getButtonLocationInput();
	
	HasClickHandlers getButtonStoreSite();
	
	HasClickHandlers getButtonCancel();
	
	/**
	 * @return User's location input
	 */
	HasValue<String> getLocationInput();
	
	/**
	 * @return User's site name input
	 */
	HasValue<String> getSiteNameInput();
	
	HasText getErrorText();
	
	/**
	 * @param feeds Feeds which should be selected by user
	 */
	void setFeeds(List<FeedData> feeds);
	
	void show();
	
	void hide();
	
	void setButtonsEnabled(boolean enabled);
}
