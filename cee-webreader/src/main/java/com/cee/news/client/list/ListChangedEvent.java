package com.cee.news.client.list;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired if the content list of the {@link PagingContentModel} changed
 */
public class ListChangedEvent<K> extends GwtEvent<ListChangedHandler<K>> {

    public static final GwtEvent.Type<ListChangedHandler<?>> TYPE = new Type<ListChangedHandler<?>>();
    
    private final List<K> values;
    
    public ListChangedEvent() {
        this.values = null;
    }
    
    public ListChangedEvent(final List<K> values) {
        this.values = values;
    }

    public List<K> getValues() {
        return values;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public GwtEvent.Type<ListChangedHandler<K>> getAssociatedType() {
        return (GwtEvent.Type)TYPE;
    }

    @Override
    protected void dispatch(ListChangedHandler<K> handler) {
        handler.onContentListChanged(this);
    }
}