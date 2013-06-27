package com.cee.news.client.content;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.async.LoadingInfoAsyncCallback;
import com.cee.news.client.content.SiteUpdateResult.State;
import com.cee.news.client.error.ErrorSourceBase;
import com.cee.news.model.EntityKey;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddSiteWorkflow extends ErrorSourceBase {

	private static final String SCHEME_SEPARATOR = "://";

	private static final String HTTP_SCHEME = "http://";

	protected final NewSiteWizardView wizard;

	protected final SiteUpdateServiceAsync siteUpdateService = SiteUpdateServiceAsync.Util.getInstance();

	protected final SiteServiceAsync siteService = SiteServiceAsync.Util.getInstance();
	
	protected final LoadingInfoAsyncCallback loadingCallback;
	
	protected SiteData site;

	public AddSiteWorkflow(final NewSiteWizardView wizard) {
		this.wizard = wizard;
		this.loadingCallback = new LoadingInfoAsyncCallback(wizard);
		wizard.getButtonCancel().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				wizard.hide();
			}
		});
		wizard.getButtonLocationInput().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				validateLocationInput();
			}
		});
		wizard.getButtonStoreSite().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				storeSite();
			}
		});
	}

	public void start() {
		clearErrorMessage();
		wizard.showPageLocationInput();
		wizard.setButtonsEnabled(true);
		wizard.center();
	}

	public void validateLocationInput() {
		wizard.setButtonsEnabled(false);
		clearErrorMessage();
		String siteLocation = wizard.getSiteLocationInput().getValue().trim();
		String feedLocation = wizard.getFeedLocationInput().getValue().trim();
		if (siteLocation.isEmpty() && feedLocation.isEmpty()) {
			showErrorMessage("Please provide a site or feed URL");
			return;
		}
		boolean useSiteLocation = feedLocation.isEmpty();
		if (useSiteLocation) {
			validateSiteLocation(addHttpScheme(siteLocation));
		} else {
			validateFeedLocation(addHttpScheme(feedLocation));
		}
	}
	
	private String addHttpScheme(String location) {
		if (location.indexOf(SCHEME_SEPARATOR) < 0) {
			return HTTP_SCHEME + location;
		}
		return location;
	}
	
	private void validateSiteLocation(String location) {
		wizard.getSiteLocationInput().setValue(location);
		wizard.getFeedLocationInput().setValue("");
		siteUpdateService.retrieveSiteData(location,
			loadingCallback.call(
				"Loading site information...", 
				new AsyncCallback<SiteData>() {
		
					@Override
					public void onSuccess(SiteData result) {
						retrieveSiteDataSuccess(result);
					}
		
					@Override
					public void onFailure(Throwable caught) {
						wizard.setButtonsEnabled(true);
						fireErrorEvent(caught, "Could not validate location input");
					}
				})
			);
	}
	
	private void retrieveSiteDataSuccess(SiteData result) {
	    switch (result.getState()) {
	    case ok:
	    	if (result.getFeeds().size() < 1) {
	    		showErrorMessage("This site does not provide any feed information");
	    	} else {
	    		site = result;
	    		guessUniqueSiteName(site.getLocation());
	    	}
	    	break;
	    case ioError:
	    	showErrorMessage("Could not load site");
	    	break;
	    case malformedUrl:
	    	showErrorMessage("The input does not represent a valid URL");
	    	break;
	    case parserError:
	    	showErrorMessage("Could not extract feed information from remote site");
	    	break;
	    default:
	    	break;
	    }
	}
	
	private void validateFeedLocation(String location) {
		wizard.getSiteLocationInput().setValue("");
		wizard.getFeedLocationInput().setValue(location);
		
		siteUpdateService.retrieveFeedData(location, 
			loadingCallback.call(
				"Loading site information...", 
				new AsyncCallback<FeedData>() {
				
					@Override
					public void onSuccess(FeedData result) {
						retrieveFeedDataSuccess(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						wizard.setButtonsEnabled(true);
						fireErrorEvent(caught, "Could not validate location input");
					}
				})
			);
	}
	
	private void retrieveFeedDataSuccess(FeedData result) {
	    switch (result.getState()) {
	    case ok:
	    	site = new SiteData();
	    	List<FeedData> feeds = new ArrayList<FeedData>();
	    	feeds.add(result);
	    	site.setFeeds(feeds);
	    	site.setLocation(result.getLocation());
	    	guessUniqueSiteName(result.getTitle());
	    	break;
	    case ioError:
	    	showErrorMessage("Could not load site");
	    	break;
	    case malformedUrl:
	    	showErrorMessage("The input does not represent a valid URL");
	    	break;
	    case parserError:
	    	showErrorMessage("Could not extract feed information from remote site");
	    	break;
	    default:
	    	break;
	    }
	}
	
	private void guessUniqueSiteName(String name) {
		siteService.guessUniqueSiteName(name, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				site.setName(result);
				showFeedSelection();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not guess unique site name");
			}
		});
	}
	
	private void showFeedSelection() {
		clearErrorMessage();
		wizard.getSiteNameInput().setValue(site.getName());
		wizard.setFeeds(site.getFeeds());
		wizard.showPageFeedSelection();
		wizard.setButtonsEnabled(true);
	}

	private void storeSite() {
		wizard.setButtonsEnabled(false);
		clearErrorMessage();
		
		//check feed selection
		boolean isFeedSelected = false;
		for (FeedData feed: site.getFeeds()) {
			if (feed.getIsActive()) {
				isFeedSelected = true;
			}
		}
		if (!isFeedSelected) {
			showErrorMessage("Select at least one feed!");//TODO: localization
			wizard.setButtonsEnabled(true);
			return;
		}
		
		//get site's language from feeds. Default language is en.
		site.setLanguage(EntityKey.get("en"));
		for (FeedData feed : site.getFeeds()) {
			if (feed.getIsActive()) {
				site.setLanguage(feed.getLanguage());
				break;
			}
		}
		
		site.setName(wizard.getSiteNameInput().getValue());
		siteService.update(site, new AsyncCallback<SiteUpdateResult>() {
			
			@Override
			public void onSuccess(SiteUpdateResult result) {
				if (result.getState() == State.entityExists) {
					showErrorMessage("Site with name " + site.getName() + " already exists!");//TODO: localization
				} else {
					queueSiteUpdate(result.getKey());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not store new site");
			}
		});
	}
	
	private void queueSiteUpdate(final EntityKey siteKey) {
		siteUpdateService.addSiteToUpdateQueue(siteKey, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				wizard.hide();
				fireSiteAdded(siteKey);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not add site to update queue");
			}
		});
	}
	
	protected void clearErrorMessage() {
		wizard.getErrorText().setText("");
	}
	
	protected void showErrorMessage(String message) {
		wizard.getErrorText().setText(message);
		wizard.setButtonsEnabled(true);
	}
	
	protected void fireSiteAdded(EntityKey entityKey) {
		handlerManager.fireEvent(new SiteAddedEvent(entityKey));
	}
	
	public HandlerRegistration addSiteAddedHandler(SiteAddedHandler handler) {
		return handlerManager.addHandler(SiteAddedEvent.TYPE, handler);
	}
}