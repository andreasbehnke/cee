package org.cee.webreader.client.list;

import com.google.gwt.event.shared.EventHandler;

public interface ListChangedHandler<T> extends EventHandler {

    void onContentListChanged(ListChangedEvent<T> event);
    
}
