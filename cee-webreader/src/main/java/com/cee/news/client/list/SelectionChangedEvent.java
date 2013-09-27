package com.cee.news.client.list;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired if the selection of content changed
 */
public class SelectionChangedEvent<K> extends GwtEvent<SelectionChangedHandler<K>> {
    
    public final static GwtEvent.Type<SelectionChangedHandler<?>> TYPE = new Type<SelectionChangedHandler<?>>();
    
    private final K key;
    
    private final boolean userAction;
    
    public SelectionChangedEvent() {
        key = null;
        userAction = false;
    }
    
    public SelectionChangedEvent(K key, boolean userAction) {
        this.key = key;
        this.userAction = userAction;
    }
    
    /**
     * @return Primary key of the selected entity
     */
    public K getKey() {
        return key;
    }
    
    /**
     * @return true if this event was triggered by an user action
     */
    public boolean isUserAction() {
		return userAction;
	}

    @SuppressWarnings({"unchecked", "rawtypes"})
	@Override
    public GwtEvent.Type<SelectionChangedHandler<K>> getAssociatedType() {
        return (GwtEvent.Type)TYPE;
    }

    @Override
    protected void dispatch(SelectionChangedHandler<K> handler) {
        handler.onSelectionChange(this);
    }    
}
