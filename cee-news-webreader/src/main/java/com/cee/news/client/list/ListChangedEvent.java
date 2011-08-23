package com.cee.news.client.list;

import java.util.List;

import com.cee.news.model.EntityKey;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired if the content list of the {@link PagingContentModel} changed
 */
public class ListChangedEvent extends GwtEvent<ListChangedHandler> {

    public final static GwtEvent.Type<ListChangedHandler> TYPE = new Type<ListChangedHandler>();
    
    private final List<EntityKey> links;
    
    public ListChangedEvent(List<EntityKey> links) {
        this.links = links;
    }

    public List<EntityKey> getLinks() {
        return links;
    }

    @Override
    public GwtEvent.Type<ListChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ListChangedHandler handler) {
        handler.onContentListChanged(this);
    }
}