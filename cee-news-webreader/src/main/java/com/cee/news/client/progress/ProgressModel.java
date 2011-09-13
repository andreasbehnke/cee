package com.cee.news.client.progress;

import com.cee.news.client.content.SiteUpdateService;
import com.cee.news.client.content.SiteUpdateServiceAsync;
import com.cee.news.client.error.ErrorSourceBase;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ProgressModel extends ErrorSourceBase {
	
	private static final int SCHEDULER_DELAY = 1000;

	private final SiteUpdateServiceAsync siteUpdateService = SiteUpdateService.Util.getInstance();

	private int max = -1;
	
	private int value;
	
	private int percentComplete;
	
	public void startMonitor() {
		max = -1;
		value = 0;
		monitorProgress();
	}
	
	protected void monitorProgress() {
		siteUpdateService.getUpdateTasks(new AsyncCallback<Integer>() {
			
			@Override
			public void onSuccess(Integer result) {
				if (max < result) {
					max = result;
				}
				value = result;
				if (value > 0) {
					percentComplete = (max - value) * (100 / max);
					fireProgress();
					Timer scheduler = new Timer() {
						@Override
						public void run() {
							monitorProgress();
						}
					};
					scheduler.schedule(SCHEDULER_DELAY);
				} else {
					percentComplete = 100;
					fireProgress();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not monitor update service");
			}
		});
	}
	
	protected void fireProgress() {
		handlerManager.fireEvent(new ProgressEvent(percentComplete));
	}
	
	public HandlerRegistration addProgressHandler(ProgressHandler handler) {
		return handlerManager.addHandler(ProgressEvent.TYPE, handler);
	}
}
