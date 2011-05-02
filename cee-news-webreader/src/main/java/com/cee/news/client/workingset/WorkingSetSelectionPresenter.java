package com.cee.news.client.workingset;

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

    public WorkingSetSelectionPresenter(final ListModel model, final WorkingSetSelectionView view) {
        this.view = view;
        
        model.addListChangedHandler(new ListChangedHandler() {
            
            public void onContentListChanged(ListChangedEvent event) {
                view.setWorkingSets(event.getLinks());
            }
        });
        
        model.addSelectionChangedhandler(new SelectionChangedHandler() {
            
            public void onSelectionChange(SelectionChangedEvent event) {
                view.setSelectedWorkingSet(event.getSelection());
            }
        });
        
        view.addSelectionChangedHandler(new ChangeHandler() {
            
            public void onChange(ChangeEvent event) {
                model.setSelectedContent(view.getSelectedWorkingSet());
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
