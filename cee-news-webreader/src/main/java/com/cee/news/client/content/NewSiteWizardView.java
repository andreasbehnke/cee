package com.cee.news.client.content;

import java.util.List;

import com.cee.news.client.DialogView;
import com.cee.news.client.async.LoadingInfoView;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;

/**
 * The view of the {@link AddSiteWorkflow}
 */
public interface NewSiteWizardView extends DialogView, LoadingInfoView {

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
	
	HasValue<String> getSiteLocationInput();
	
	HasValue<String> getFeedLocationInput();
	
	/**
	 * @return User's site name input
	 */
	HasValue<String> getSiteNameInput();
	
	HasText getErrorText();
	
	/**
	 * @param feeds Feeds which should be selected by user
	 */
	void setFeeds(List<FeedData> feeds);
	
	void setButtonsEnabled(boolean enabled);
}
