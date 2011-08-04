package com.cee.news.client.workingset;

import java.util.List;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.list.DefaultListModel;
import com.cee.news.client.list.EntityKey;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class WorkingSetListModel extends DefaultListModel {
    
    private final WorkingSetServiceAsync workingSetService = WorkingSetServiceAsync.Util.getInstance();
    
    public void update() {
    	update(null);
    }
    
    public void update(final NotificationCallback callback) {
    	workingSetService.getWorkingSetsOrderedByName(new AsyncCallback<List<EntityKey>>() {

			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not retrieve list of working sets");
			}

			@Override
			public void onSuccess(List<EntityKey> result) {
				setKeys(result);
				if (callback != null) {
					callback.finished();
				}
			}
    		
		});
    }
}
