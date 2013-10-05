package org.cee.webreader.client.list;

import com.google.gwt.event.shared.EventHandler;

public interface SelectionChangedHandler<K> extends EventHandler {
    
    void onSelectionChange(SelectionChangedEvent<K> event);

}
