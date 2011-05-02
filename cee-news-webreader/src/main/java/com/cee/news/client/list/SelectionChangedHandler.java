package com.cee.news.client.list;

import com.google.gwt.event.shared.EventHandler;

public interface SelectionChangedHandler extends EventHandler {
    
    void onSelectionChange(SelectionChangedEvent event);

}
