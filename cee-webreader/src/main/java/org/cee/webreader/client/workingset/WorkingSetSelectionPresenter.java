package org.cee.webreader.client.workingset;

/*
 * #%L
 * News Reader
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.List;

import org.cee.store.EntityKey;
import org.cee.webreader.client.list.ListChangedEvent;
import org.cee.webreader.client.list.ListChangedHandler;
import org.cee.webreader.client.list.ListModel;
import org.cee.webreader.client.list.SelectionChangedEvent;
import org.cee.webreader.client.list.SelectionChangedHandler;

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
            		view.setDeleteButtonEnabled(false);
            	} else {
            		view.setEditButtonEnabled(true);
            		view.setDeleteButtonEnabled(true);
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
