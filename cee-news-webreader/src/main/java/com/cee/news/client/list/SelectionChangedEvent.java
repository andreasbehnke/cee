package com.cee.news.client.list;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired if the selection of content changed
 */
public class SelectionChangedEvent extends GwtEvent<SelectionChangedHandler> {
    
    public static final GwtEvent.Type<SelectionChangedHandler> TYPE = new Type<SelectionChangedHandler>();
    
    private final String key;
    
    public SelectionChangedEvent(String key) {
        this.key = key;
    }
    
    /**
     * @return Primary key of the selected entity
     */
    public String getKey() {
        return key;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelectionChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SelectionChangedHandler handler) {
        handler.onSelectionChange(this);
    }    
}
