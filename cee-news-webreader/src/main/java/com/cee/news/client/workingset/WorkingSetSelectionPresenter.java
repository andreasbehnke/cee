package com.cee.news.client.workingset;

import java.util.List;

import com.cee.news.client.list.EntityKey;
import com.cee.news.client.list.EntityKeyUtil;
import com.cee.news.client.list.ListChangedEvent;
import com.cee.news.client.list.ListChangedHandler;
import com.cee.news.client.list.ListModel;
import com.cee.news.client.list.SelectionChangedEvent;
import com.cee.news.client.list.SelectionChangedHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;

public class WorkingSetSelectionPresenter {
    
    private final WorkingSetSelectionView view;
    
    private List<EntityKey> keys;

    public WorkingSetSelectionPresenter(final ListModel model, final WorkingSetSelectionView view) {
        this.view = view;
        
        model.addListChangedHandler(new ListChangedHandler() {
            
            public void onContentListChanged(ListChangedEvent event) {
                keys = event.getLinks();
            	view.setWorkingSets(keys);
            }
        });
        
        model.addSelectionChangedhandler(new SelectionChangedHandler() {
            
            public void onSelectionChange(SelectionChangedEvent event) {
            	int index = EntityKeyUtil.getIndexOfEntityKey(keys, event.getKey());
                view.setSelectedWorkingSet(index);
            }
        });
        
        view.addSelectionChangedHandler(new ChangeHandler() {
            
            public void onChange(ChangeEvent event) {
                model.setSelectedKey(keys.get(view.getSelectedWorkingSet()).getKey());
            }
        });
    }
    
    public void addNewWorkingSetHandler(ClickHandler handler) {
        view.getNewButton().addClickHandler(handler);
    }
    
    public void addEditWorkingSetHandler(ClickHandler handler) {
        view.getEditButton().addClickHandler(handler);
    }
}
