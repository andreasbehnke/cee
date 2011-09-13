package com.cee.news.client;

import com.cee.news.client.content.NewsListContentModel;
import com.cee.news.client.list.SelectionChangedEvent;
import com.cee.news.client.list.SelectionChangedHandler;
import com.cee.news.client.progress.ProgressHandler;

public class ProgressUpdateHandler implements ProgressHandler,
		SelectionChangedHandler {
	
	private final NewsListContentModel listModel;
	
	private String selectedWorkingSet;
	
	private int lastPercentComplete = 0;
	
	public ProgressUpdateHandler(NewsListContentModel listModel) {
		this.listModel = listModel;
	}

	@Override
	public void onSelectionChange(SelectionChangedEvent event) {
		selectedWorkingSet = event.getKey();
	}

	@Override
	public void onProgress(int percentComplete) {
		if (lastPercentComplete != percentComplete) {
			lastPercentComplete = percentComplete;
			listModel.updateFromWorkingSet(selectedWorkingSet);
		}
	}

}
