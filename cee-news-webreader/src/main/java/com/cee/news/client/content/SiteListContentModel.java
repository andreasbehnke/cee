package com.cee.news.client.content;

import java.util.List;

import com.cee.news.client.list.ContentModel;
import com.cee.news.client.list.DefaultListModel;
import com.cee.news.client.list.EntityKey;
import com.cee.news.client.util.SafeHtmlUtil;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * List content model implementation for sites
 */
public class SiteListContentModel extends DefaultListModel implements ContentModel {

    private SiteServiceAsync service = SiteServiceAsync.Util.getInstance();
    
    public void updateSites() {
        service.getSites(new AsyncCallback<List<EntityKey>>() {
            
            public void onSuccess(List<EntityKey> result) {
                setKeys(result);
            }
            
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not retrieve site list");
            }
        });
    }
    
    public void updateSites(String workingSetName) {
    	service.getSitesOfWorkingSet(workingSetName, new AsyncCallback<List<EntityKey>>() {
			
			@Override
			public void onSuccess(List<EntityKey> result) {
				setKeys(result);
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
        service.getHtmlTitle(key, new AsyncCallback<String>() {
            
            public void onSuccess(String result) {
                target.setHTML(SafeHtmlUtil.sanitize(result));
            }
            
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not retrieve site title");
            }
        });
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
        service.getHtmlDescription(key, new AsyncCallback<String>() {
            
            public void onSuccess(String result) {
                target.setHTML(SafeHtmlUtil.sanitize(result));
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
}
