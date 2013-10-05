package org.cee.webreader.client.content;

import org.cee.news.model.EntityKey;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired if a new site was added
 */
public class SiteAddedEvent extends GwtEvent<SiteAddedHandler> {

	public final static GwtEvent.Type<SiteAddedHandler> TYPE = new Type<SiteAddedHandler>();

	private final EntityKey entityKey;

	public SiteAddedEvent(final EntityKey entityKey) {
		this.entityKey = entityKey;
	}

	public EntityKey getEntityKey() {
		return entityKey;
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