package com.cee.news.client.list;

import com.google.gwt.event.shared.EventHandler;

public interface ListChangedHandler extends EventHandler {

    void onContentListChanged(ListChangedEvent event);
    
}
