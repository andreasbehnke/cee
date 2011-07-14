package com.cee.news.client.content;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.list.ContentListModel;
import com.cee.news.client.list.DefaultListModel;
import com.cee.news.client.list.LinkValue;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * List content model implementation for sites
 */
public class SiteListContentModel extends DefaultListModel implements ContentListModel {

    private SiteServiceAsync service = SiteServiceAsync.Util.getInstance();
    
    protected List<String> sites;
    
    public void updateSites() {
        service.getSites(new AsyncCallback<List<String>>() {
            
            public void onSuccess(List<String> result) {
                setSites(result);
            }
            
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not retrieve site list");
            }
        });
    }
    
    public void updateSites(String workingSetName) {
    	service.getSitesOfWorkingSet(workingSetName, new AsyncCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> result) {
				setSites(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not retrieve site list");
			}
		});
    }
    
    protected void setSites(List<String> sites) {
        this.sites = sites;
        List<LinkValue> links = new ArrayList<LinkValue>();
        int count = 0;
        for (String location : sites) {
            links.add(new LinkValue(count, location));
            count++;
        }
        fireContentListChanged(links);
        setSelectedContent(0);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.cee.news.client.list.ListContentModel#getContentCount()
     */
    public int getContentCount() {
        if (sites == null) {
            return 0;
        }
        return sites.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cee.news.client.list.ListContentModel#getContentTitle(com.google.
     * gwt.safehtml.client.HasSafeHtml, int)
     */
    public void getContentTitle(final HasSafeHtml target, int index) {
        String location = sites.get(index);
        service.getTitle(location, new AsyncCallback<SafeHtml>() {
            
            public void onSuccess(SafeHtml result) {
                target.setHTML(result);
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
    public void getContentDescription(final HasSafeHtml target, int index) {
        String location = sites.get(index);
        service.getHtmlDescription(location, new AsyncCallback<SafeHtml>() {
            
            public void onSuccess(SafeHtml result) {
                target.setHTML(result);
            }
            
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not retrieve description of site");
            }
        });
    }
    
    public String getSelectedSite() {
        return sites.get(selection);
    }
    
    public String getSite(int index) {
        return sites.get(index);
    }
}
