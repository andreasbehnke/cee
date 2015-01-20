package org.cee.webreader.client.content;

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


import java.util.ArrayList;
import java.util.List;

import org.cee.store.EntityKey;
import org.cee.webreader.client.async.NotificationCallback;
import org.cee.webreader.client.list.CellListContentModel;
import org.cee.webreader.client.list.ContentModel;
import org.cee.webreader.client.list.DefaultListModel;
import org.cee.webreader.client.util.SafeHtmlUtil;

import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * List content model implementation for sites
 */
public class SiteListContentModel extends DefaultListModel<EntityKey> implements ContentModel<EntityKey>, CellListContentModel<EntityKey> {

    private GwtSiteServiceAsync service = GwtSiteServiceAsync.Util.getInstance();
    
    public void findAllSites() {
        findAllSites((NotificationCallback)null);
    }
    
    public void findAllSites(final NotificationCallback callback) {
        service.getSites(new AsyncCallback<List<EntityKey>>() {
            
            public void onSuccess(List<EntityKey> result) {
                setValues(result);
                if (callback != null) {
                	callback.finished();
                }
            }
            
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not retrieve site list");
            }
        });
    }
    
    public void findSitesOfWorkingSet(EntityKey workingSetKey) {
        findSitesOfWorkingSet(workingSetKey, null);
    }
    
    public void findSitesOfWorkingSet(EntityKey workingSetKey, final NotificationCallback callback) {
    	service.getSitesOfWorkingSet(workingSetKey, new AsyncCallback<List<EntityKey>>() {
			
			@Override
			public void onSuccess(List<EntityKey> result) {
				setValues(result);
				if (callback != null) {
                	callback.finished();
                }
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not retrieve site list");
			}
		});
    }
    
    public void clear() {
    	clearSelection();
    	setValues(new ArrayList<EntityKey>());
    }

    @Override
    public void getContentTitle(final HasSafeHtml target, EntityKey key) {
    	String title = key.getName();
        target.setHTML(SafeHtmlUtil.sanitize(title));
    }

    @Override
    public void getContentDescription(final HasSafeHtml target, EntityKey key) {
    	service.getHtmlDescription(key, new AsyncCallback<EntityContent<EntityKey>>() {
            
            public void onSuccess(EntityContent<EntityKey> result) {
                target.setHTML(SafeHtmlUtil.sanitize(result.getContent()));
            }
            
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not retrieve description of site");
            }
        });
    }

	@Override
	public void getContent(HasSafeHtml target, EntityKey key) {
		//no main content available for sites
	}
	
	@Override
	public void getContent(ArrayList<EntityKey> keys, AsyncCallback<List<EntityContent<EntityKey>>> callback) {
	    service.getHtmlDescriptions(keys, callback);
	};
	
}
