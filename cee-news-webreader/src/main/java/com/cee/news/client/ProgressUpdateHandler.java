package com.cee.news.client;

import com.cee.news.client.content.NewsListContentModel;
import com.cee.news.client.list.SelectionChangedEvent;
import com.cee.news.client.list.SelectionChangedHandler;
import com.cee.news.client.progress.ProgressHandler;
import com.cee.news.model.EntityKey;

public class ProgressUpdateHandler implements ProgressHandler,
		SelectionChangedHandler<EntityKey> {
	
	private final NewsListContentModel listModel;
	
	private EntityKey selectedWorkingSet;
	
	public ProgressUpdateHandler(NewsListContentModel listModel) {
		this.listModel = listModel;
	}

	@Override
	public void onSelectionChange(SelectionChangedEvent<EntityKey> event) {
		selectedWorkingSet = event.getKey();
	}

	@Override
	public void onProgress(int percentComplete) {
		if (percentComplete == 100) {
			listModel.updateFromWorkingSet(selectedWorkingSet.getKey());
		}
	}

}
