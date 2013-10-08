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
