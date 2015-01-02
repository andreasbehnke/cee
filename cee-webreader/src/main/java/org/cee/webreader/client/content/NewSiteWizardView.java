package org.cee.webreader.client.content;

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

import java.util.List;

import org.cee.client.site.FeedData;
import org.cee.news.model.EntityKey;
import org.cee.webreader.client.DialogView;
import org.cee.webreader.client.async.LoadingInfoView;

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
	
	void setAvailableLanguages(List<EntityKey> languages);
	
	EntityKey getSelectedLanguage();
	
	void showLanguageSelection(boolean visible);
}
