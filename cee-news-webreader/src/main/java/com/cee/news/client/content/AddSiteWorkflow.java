package com.cee.news.client.content;

import com.cee.news.client.async.EntityUpdateResult;
import com.cee.news.client.error.ErrorSourceBase;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddSiteWorkflow extends ErrorSourceBase {

	private static final String SCHEME_SEPARATOR = "://";

	private static final String HTTP_SCHEME = "http://";

	private final NewSiteWizardView wizard;

	private final SiteUpdateServiceAsync siteUpdateService = SiteUpdateServiceAsync.Util.getInstance();

	private final SiteServiceAsync siteService = SiteServiceAsync.Util.getInstance();
	
	private SiteData site;

	public AddSiteWorkflow(final NewSiteWizardView wizard) {
		this.wizard = wizard;
		wizard.getButtonCancel().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				wizard.hide();
			}
		});
		wizard.getButtonLocationInput().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				validateLocationInput(wizard.getLocationInput().getValue());
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
		wizard.showPageLocationInput();
		wizard.setButtonsEnabled(true);
		wizard.center();
	}

	public void validateLocationInput(String location) {
		wizard.setButtonsEnabled(false);
		if (location.isEmpty()) {
			showErrorMessage("Provide a site URL");
			return;
		}
		if (location.indexOf(SCHEME_SEPARATOR) < 0) {
			location = HTTP_SCHEME + location;
		}
		wizard.getLocationInput().setValue(location);
		siteUpdateService.retrieveSiteData(location,
				new AsyncCallback<SiteData>() {

					@Override
					public void onSuccess(SiteData result) {
						switch (result.getState()) {
						case ok:
							site = result;
							guessUniqueSiteName();
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

					@Override
					public void onFailure(Throwable caught) {
						wizard.setButtonsEnabled(true);
						fireErrorEvent(caught,
								"Could not validate location input");
					}
				});
	}

	private void guessUniqueSiteName() {
		String location = site.getLocation();
		siteService.guessUniqueSiteName(location, new AsyncCallback<String>() {
			
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
		wizard.getSiteNameInput().setValue(site.getName());
		wizard.setFeeds(site.getFeeds());
		wizard.showPageFeedSelection();
		wizard.setButtonsEnabled(true);
	}

	private void storeSite() {
		wizard.setButtonsEnabled(false);
		site.setName(wizard.getSiteNameInput().getValue());
		siteService.update(site, new AsyncCallback<EntityUpdateResult>() {
			
			@Override
			public void onSuccess(EntityUpdateResult result) {
				wizard.hide();
				fireSiteAdded(site.getName());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not store new site");
			}
		});
	}
	private void showErrorMessage(String message) {
		wizard.getErrorText().setText(message);
		wizard.setButtonsEnabled(true);
	}
	
	protected void fireSiteAdded(String siteName) {
		handlerManager.fireEvent(new SiteAddedEvent(siteName));
	}
	
	public HandlerRegistration addSiteAddedHandler(SiteAddedHandler handler) {
		return handlerManager.addHandler(SiteAddedEvent.TYPE, handler);
	}
}