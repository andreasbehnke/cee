package com.cee.news.client.list;

import com.google.gwt.event.shared.EventHandler;

public interface SelectionListChangedHandler<K> extends EventHandler {

    void onSelectionListChanged(SelectionListChangedEvent<K> event);
    
}
