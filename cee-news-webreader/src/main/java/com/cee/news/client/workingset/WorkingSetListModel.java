package com.cee.news.client.workingset;

import com.cee.news.client.list.ListChangedHandler;
import com.cee.news.client.list.ListModel;
import com.cee.news.client.list.SelectionChangedHandler;

public class WorkingSetListModel implements ListModel {
    
    private final WorkingSetServiceAsync workingSetService = WorkingSetServiceAsync.Util.getInstance();

    public int getContentCount() {
        return 0;
    }

    public int getSelectedContent() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setSelectedContent(int index) {
        // TODO Auto-generated method stub

    }

    public void addSelectionChangedhandler(SelectionChangedHandler handler) {
        // TODO Auto-generated method stub

    }

    public void addListChangedHandler(ListChangedHandler handler) {
        // TODO Auto-generated method stub

    }

}
