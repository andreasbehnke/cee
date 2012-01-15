package com.cee.news.client.content;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.list.ContentModel;
import com.cee.news.client.list.DefaultListModel;
import com.cee.news.client.list.EntityContent;
import com.cee.news.client.list.EntityContentModel;
import com.cee.news.client.list.EntityKeyUtil;
import com.cee.news.client.util.SafeHtmlUtil;
import com.cee.news.model.EntityKey;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * List content model implementation for sites
 */
public class SiteListContentModel extends DefaultListModel implements ContentModel, EntityContentModel {

    private SiteServiceAsync service = SiteServiceAsync.Util.getInstance();
    
    public void update() {
        update((NotificationCallback)null);
    }
    
    public void update(final NotificationCallback callback) {
        service.getSites(new AsyncCallback<List<EntityKey>>() {
            
            public void onSuccess(List<EntityKey> result) {
                setKeys(result);
                if (callback != null) {
                	callback.finished();
                }
            }
            
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not retrieve site list");
            }
        });
    }
    
    public void update(String workingSetName) {
        update(workingSetName, null);
    }
    
    public void update(String workingSetName, final NotificationCallback callback) {
    	service.getSitesOfWorkingSet(workingSetName, new AsyncCallback<List<EntityKey>>() {
			
			@Override
			public void onSuccess(List<EntityKey> result) {
				setKeys(result);
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cee.news.client.list.ListContentModel#getContentTitle(com.google.
     * gwt.safehtml.client.HasSafeHtml, int)
     */
    @Override
    public void getContentTitle(final HasSafeHtml target, String key) {
    	String title = EntityKeyUtil.getEntityKey(keys, key).getName();
        target.setHTML(SafeHtmlUtil.sanitize(title));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cee.news.client.list.ListContentModel#getContentDescription(com.google
     * .gwt.safehtml.client.HasSafeHtml, int)
     */
    @Override
    public void getContentDescription(final HasSafeHtml target, String key) {
    	EntityKey entityKey = EntityKeyUtil.getEntityKey(keys, key);
        service.getHtmlDescription(entityKey, new AsyncCallback<EntityContent>() {
            
            public void onSuccess(EntityContent result) {
                target.setHTML(SafeHtmlUtil.sanitize(result.getHtmlContent()));
            }
            
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not retrieve description of site");
            }
        });
    }

	@Override
	public void getContent(HasSafeHtml target, String key) {
		//no main content available for sites
	}
	
	@Override
	public void getContent(ArrayList<EntityKey> keys, AsyncCallback<List<EntityContent>> callback) {
		service.getHtmlDescriptions(keys, callback);
	}
}
