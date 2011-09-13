package com.cee.news.client.progress;

import com.google.gwt.event.shared.EventHandler;

public interface ProgressHandler extends EventHandler {

	void onProgress(int percentComplete);
	
}
