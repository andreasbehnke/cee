package com.cee.news.client.list;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired if the content list of the {@link PagingContentModel} changed
 */
public class SelectionListChangedEvent extends GwtEvent<SelectionListChangedHandler> {

    public final static GwtEvent.Type<SelectionListChangedHandler> TYPE = new Type<SelectionListChangedHandler>();
    
    private final List<EntityKey> links;
    
    public SelectionListChangedEvent(List<EntityKey> links) {
        this.links = links;
    }

    public List<EntityKey> getLinks() {
        return links;
    }

    @Override
    public GwtEvent.Type<SelectionListChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SelectionListChangedHandler handler) {
        handler.onSelectionListChanged(this);
    }
}