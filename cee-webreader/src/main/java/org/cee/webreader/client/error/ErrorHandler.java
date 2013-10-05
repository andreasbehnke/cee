package org.cee.webreader.client.error;

import com.google.gwt.event.shared.EventHandler;

public interface ErrorHandler extends EventHandler {

    void onError(ErrorEvent event);
}
