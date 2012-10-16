package com.cee.news.client.content;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.list.CellListContentModel;
import com.cee.news.client.list.ContentModel;
import com.cee.news.client.list.DefaultListModel;
import com.cee.news.client.util.SafeHtmlUtil;
import com.cee.news.model.EntityKey;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * List content model implementation for sites
 */
public class SiteListContentModel extends DefaultListModel<EntityKey> implements ContentModel<EntityKey>, CellListContentModel<EntityKey> {

    private SiteServiceAsync service = SiteServiceAsync.Util.getInstance();
    
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
