package com.cee.news.client.content;

import java.util.List;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.list.ContentModel;
import com.cee.news.client.list.DefaultListModel;
import com.cee.news.client.util.SafeHtmlUtil;
import com.cee.news.model.EntityKey;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * {@link PagingContentModel} which calls remote news service asynchronously
 *
 *
 **/
 
 //TODO Test methods!
public class NewsListContentModel extends DefaultListModel implements ContentModel {
    
    private final NewsServiceAsync service = NewsService.Util.getInstance();
    
    public void updateFromSite(final String siteLocation) {
    	updateFromSite(siteLocation, null);
    }
    
    public void updateFromSite(final String siteLocation, final NotificationCallback callback) {
        service.getArticlesOfSite(siteLocation, new AsyncCallback<List<EntityKey>>() {
            public void onSuccess(List<EntityKey> result) {
                setKeys(result);
                if(callback != null) {
                	callback.finished();
                }
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load headlines!");//TODO: i18n
            }
        });
    }
    
    public void updateFromWorkingSet(final String workingSetName) {
    	updateFromWorkingSet(workingSetName, null);
    }
    
    public void updateFromWorkingSet(final String workingSetName, final NotificationCallback callback) {
    	service.getArticlesOfWorkingSet(workingSetName, new AsyncCallback<List<EntityKey>>() {
			
			@Override
			public void onSuccess(List<EntityKey> result) {
				setKeys(result);
				if(callback != null) {
                	callback.finished();
                }
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not load headlines!");//TODO: i18n
			}
		});
    }

    public void getContentTitle(final HasSafeHtml target, String key) {
        if (keys == null) {
            throw new IllegalStateException("Headlines have not been set yet!");
        }
        for (EntityKey entityKey : keys) {
			if (entityKey.getKey().equals(key)) {
				target.setHTML(SafeHtmlUtil.sanitize(entityKey.getName()));
				return;
			}
		}
        throw new IllegalArgumentException("Could not find headline for key " + key);
    }

    public void getContentDescription(final HasSafeHtml target, String key) {
        service.getHtmlDescription(key, new AsyncCallback<String>() {
            public void onSuccess(String result) {
                target.setHTML(SafeHtmlUtil.sanitize(result) );
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load description!");//TODO: i18n
            }
        });
    }

    public void getContent(final HasSafeHtml target, String key) {
        service.getHtmlContent(key, new AsyncCallback<String>() {
            public void onSuccess(String result) {
            	target.setHTML(SafeHtmlUtil.sanitize(result) );
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load content!");//TODO: i18n
            }
        });
    }
}
