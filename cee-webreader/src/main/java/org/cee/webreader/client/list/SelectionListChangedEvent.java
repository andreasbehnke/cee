package org.cee.webreader.client.list;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired if the content list of the {@link PagingContentModel} changed
 */
public class SelectionListChangedEvent<K> extends GwtEvent<SelectionListChangedHandler<K>> {

    public static final GwtEvent.Type<SelectionListChangedHandler<?>> TYPE = new Type<SelectionListChangedHandler<?>>();

    private final List<K> keys;

    public SelectionListChangedEvent() {
        keys = null;
    }
    
    public SelectionListChangedEvent(List<K> keys) {
        this.keys = keys;
    }

    public List<K> getKeys() {
        return keys;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public GwtEvent.Type<SelectionListChangedHandler<K>> getAssociatedType() {
        return (GwtEvent.Type)TYPE;
    }

    @Override
    protected void dispatch(SelectionListChangedHandler<K> handler) {
        handler.onSelectionListChanged(this);
    }
}