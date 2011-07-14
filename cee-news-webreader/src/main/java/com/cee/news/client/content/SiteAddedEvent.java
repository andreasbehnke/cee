package com.cee.news.client.content;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired if a new site was added
 */
public class SiteAddedEvent extends GwtEvent<SiteAddedHandler> {

	public final static GwtEvent.Type<SiteAddedHandler> TYPE = new Type<SiteAddedHandler>();

	private final String siteName;

	public SiteAddedEvent(final String siteName) {
		this.siteName = siteName;
	}

	public String getSiteName() {
		return siteName;
	}

	@Override
	public GwtEvent.Type<SiteAddedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SiteAddedHandler handler) {
		handler.onSiteAdded(this);
	}
}