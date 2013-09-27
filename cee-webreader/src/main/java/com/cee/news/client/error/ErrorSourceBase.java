package com.cee.news.client.error;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

public abstract class ErrorSourceBase implements ErrorSource {

	protected final EventBus handlerManager = new SimpleEventBus();

	protected void fireErrorEvent(Throwable cause, String description) {
		handlerManager.fireEvent(new ErrorEvent(cause, description));
	}

	@Override
	public HandlerRegistration addErrorHandler(ErrorHandler handler) {
		return handlerManager.addHandler(ErrorEvent.TYPE, handler);
	}
}