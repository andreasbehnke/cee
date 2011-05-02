package com.cee.news.client.list;

import java.util.List;

import com.cee.news.client.paging.PagingContentModel;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired if the content list of the {@link PagingContentModel} changed
 */
public class ListChangedEvent extends GwtEvent<ListChangedHandler> {

    public final static GwtEvent.Type<ListChangedHandler> TYPE = new Type<ListChangedHandler>();
    
    private final List<LinkValue> links;
    
    public ListChangedEvent(List<LinkValue> links) {
        this.links = links;
    }

    public List<LinkValue> getLinks() {
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