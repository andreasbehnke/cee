package com.cee.news.client.workingset;

import java.util.List;

import com.cee.news.client.list.ListChangedEvent;
import com.cee.news.client.list.ListChangedHandler;
import com.cee.news.client.list.ListModel;
import com.cee.news.client.list.SelectionChangedEvent;
import com.cee.news.client.list.SelectionChangedHandler;
import com.cee.news.model.EntityKey;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class WorkingSetSelectionPresenter {
    
    public WorkingSetSelectionPresenter(final ListModel<EntityKey> model, final WorkingSetSelectionView view, final WorkingSetWorkflow workingSetWorkflow) {
        model.addListChangedHandler(new ListChangedHandler<EntityKey>() {
            
            public void onContentListChanged(ListChangedEvent<EntityKey> event) {
                List<EntityKey> keys = event.getValues();
            	view.setWorkingSets(keys);
            	if (keys == null || keys.size() == 0) {
            		view.setEditButtonEnabled(false);
            	} else {
            		view.setEditButtonEnabled(true);
            	}
            }
        });
        
        model.addSelectionChangedhandler(new SelectionChangedHandler<EntityKey>() {
            
            public void onSelectionChange(SelectionChangedEvent<EntityKey> event) {
            	view.setSelectedWorkingSet(model.getIndexOf(event.getKey()));
            }
        });
        
        view.addSelectionChangedHandler(new ChangeHandler() {
            
            public void onChange(ChangeEvent event) {
                model.userSelectedKey(model.getKey(view.getSelectedWorkingSet()));
            }
        });
        
        view.getNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				workingSetWorkflow.newWorkingSet();
			}
		});
		view.getEditButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				workingSetWorkflow.editWorkingSet();
			}
		});
		view.getDeleteButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				workingSetWorkflow.deleteWorkingSet();
			}
		});
    }
}
