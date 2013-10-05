package org.cee.webreader.client.workingset;

import java.util.List;

import org.cee.news.model.EntityKey;
import org.cee.webreader.client.async.NotificationCallback;
import org.cee.webreader.client.list.DefaultListModel;
import org.cee.webreader.client.workingset.WorkingSetServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class WorkingSetListModel extends DefaultListModel<EntityKey> {
    
    private final WorkingSetServiceAsync workingSetService = WorkingSetServiceAsync.Util.getInstance();
    
    public void findAllWorkingSets() {
    	this.findAllWorkingSets(null);
    }
    
    public void findAllWorkingSets(final NotificationCallback callback) {
    	workingSetService.getWorkingSetsOrderedByName(new AsyncCallback<List<EntityKey>>() {

			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not retrieve list of working sets");
			}

			@Override
			public void onSuccess(List<EntityKey> result) {
				setValues(result);
				if (callback != null) {
					callback.finished();
				}
			}
    		
		});
    }
}
