package com.cee.news.client.content;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.list.ContentModel;
import com.cee.news.client.list.DefaultListModel;
import com.cee.news.client.util.SafeHtmlUtil;
import com.cee.news.model.EntityKey;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class NewsListContentModel extends DefaultListModel<EntityKey> implements ContentModel<EntityKey>, EntityKeyContentModel {
    
    private final NewsServiceAsync service = NewsServiceAsync.Util.getInstance();
    
    public void updateFromSite(final String siteKey) {
    	updateFromSite(siteKey, null);
    }
    
    public void updateFromSite(final String siteKey, final NotificationCallback callback) {
        service.getArticlesOfSite(siteKey, new AsyncCallback<List<EntityKey>>() {
            public void onSuccess(List<EntityKey> result) {
                setValues(result);
                if(callback != null) {
                	callback.finished();
                }
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load headlines!");//TODO: i18n
            }
        });
    }
    
    public void updateFromSites(final List<String> siteKeys) {
        updateFromSites(siteKeys, null);
    }
        
    public void updateFromSites(final List<String> siteKeys, final NotificationCallback callback) {
        service.getArticlesOfSites(siteKeys, new AsyncCallback<List<EntityKey>>() {
            public void onSuccess(List<EntityKey> result) {
                setValues(result);
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
				setValues(result);
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
    
    public void updateFromArticle(final String articleId, final String workingSet) {
    	updateFromArticle(articleId, workingSet, null);
    }
    
    public void updateFromArticle(final String articleId, final String workingSet, final NotificationCallback callback) {
    	service.getRelatedArticles(articleId, workingSet, new AsyncCallback<List<EntityKey>>() {
			
			@Override
			public void onSuccess(List<EntityKey> result) {
				setValues(result);
				if(callback != null) {
					callback.finished();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not load related articles!");//TODO: i18n
			}
		});
    }

    @Override
    public void getContentTitle(final HasSafeHtml target, EntityKey key) {
        if (keys == null) {
            throw new IllegalStateException("Headlines have not been set yet!");
        }
        target.setHTML(SafeHtmlUtil.sanitize(key.getName()));
    }

    @Override
    public void getContentDescription(final HasSafeHtml target, EntityKey key) {
    	service.getHtmlDescription(key, new AsyncCallback<EntityKey>() {
            public void onSuccess(EntityKey result) {
                target.setHTML(SafeHtmlUtil.sanitize(result.getHtmlContent()) );
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load description!");
            }
        });
    }

    @Override
    public void getContent(final HasSafeHtml target, EntityKey key) {
        service.getHtmlContent(key, new AsyncCallback<EntityKey>() {
            public void onSuccess(EntityKey result) {
            	target.setHTML(SafeHtmlUtil.sanitize(result.getHtmlContent()) );
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load content!");
            }
        });
    }

    @Override
    public void getContent(ArrayList<EntityKey> keys, AsyncCallback<List<EntityKey>> callback) {
    	service.getHtmlDescriptions(keys, callback);
    }
}
