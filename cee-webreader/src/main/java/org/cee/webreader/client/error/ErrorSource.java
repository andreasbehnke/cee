package org.cee.webreader.client.error;

import com.google.gwt.event.shared.HandlerRegistration;

public interface ErrorSource {
    
    HandlerRegistration addErrorHandler(ErrorHandler handler);

}
