package com.cee.news.client.progress;

import com.google.gwt.event.shared.GwtEvent;

public class ProgressEvent extends GwtEvent<ProgressHandler> {

	public static final GwtEvent.Type<ProgressHandler> TYPE = new Type<ProgressHandler>();

	private final int percentComplete;
	
	public ProgressEvent(int percentComplete) {
		this.percentComplete = percentComplete;
	}

	@Override
	public GwtEvent.Type<ProgressHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ProgressHandler handler) {
		handler.onProgress(percentComplete);
	}

}
