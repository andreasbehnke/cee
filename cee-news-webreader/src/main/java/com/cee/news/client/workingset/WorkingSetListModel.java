package com.cee.news.client.workingset;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.list.DefaultListModel;
import com.cee.news.client.list.LinkValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class WorkingSetListModel extends DefaultListModel {
    
    private final WorkingSetServiceAsync workingSetService = WorkingSetServiceAsync.Util.getInstance();
    
    private List<LinkValue> workingSets;
    
    public void update(final NotificationCallback callback) {
    	workingSetService.getWorkingSetsOrderedByName(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not retrieve list of working sets");
			}

			@Override
			public void onSuccess(List<String> result) {
				fillWorkingSetList(result);
				if (callback != null) {
					callback.finished();
				}
			}
    		
		});
    }
    
    protected void fillWorkingSetList(List<String> workingSetNames) {
    	workingSets = new ArrayList<LinkValue>();
    	for(int i=0; i<workingSetNames.size(); i++) {
    		this.workingSets.add(new LinkValue(i, workingSetNames.get(i)));
    	}
    	fireContentListChanged(workingSets);
    }

    public int getContentCount() {
    	if (workingSets == null) {
    		return 0;
    	}
    	return workingSets.size();
    }

	public void setSelectedWorkingSet(String name) {
		for (LinkValue link : workingSets) {
			if (link.getText().equals(name)) {
				setSelectedContent(link.getValue());
				return;
			}
		}
	}
	
	public String getWorkingSetName(int index) {
		return workingSets.get(index).getText();
	}
}
