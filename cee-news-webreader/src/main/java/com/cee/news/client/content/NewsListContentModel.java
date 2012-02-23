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
    
    private final NewsServiceAsync newsService = NewsServiceAsync.Util.getInstance();
    
    private String searchQuery;
    
    private List<String> filteredSites = new ArrayList<String>();

    private void retrieveNewsOfSites(final NotificationCallback callback) {
        newsService.getArticlesOfSites(filteredSites, new AsyncCallback<List<EntityKey>>() {
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
    
    private void performSearch(final NotificationCallback callback) {
        newsService.findArticles(filteredSites, searchQuery, new AsyncCallback<List<EntityKey>>() {
            
            @Override
            public void onSuccess(List<EntityKey> result) {
                setValues(result);
                if (callback != null) {
                    callback.finished();
                }
            }
            
            @Override
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not find articles!");//TODO: i18n
            }
        });
    }
    
    public void refresh(final NotificationCallback callback) {
        if (searchQuery == null) {
            retrieveNewsOfSites(callback);
        } else {
            performSearch(callback);
        }
    }
    
    public void refresh() {
        refresh(null);
    }
    
    public void getNewsOfSite(final String siteKey, final NotificationCallback callback) {
        filteredSites.clear();
        filteredSites.add(siteKey);
        refresh(callback);
    }
    
    public void getNewsOfSite(final String siteKey) {
        getNewsOfSite(siteKey, null);
    }    
    
    public void getNewsOfSites(final List<String> siteKeys, final NotificationCallback callback) {
        filteredSites = siteKeys;
        refresh(callback);
    }
    
    public void getNewsOfSites(final List<String> siteKeys) {
        getNewsOfSites(siteKeys, null);
    }
    
    public void getRelatedArticles(final String articleId, final String workingSet) {
    	getRelatedArticles(articleId, workingSet, null);
    }
    
    public void getRelatedArticles(final String articleId, final String workingSet, final NotificationCallback callback) {
    	newsService.getRelatedArticles(articleId, workingSet, new AsyncCallback<List<EntityKey>>() {
			
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
    
    public void findArticles(final String searchQuery, final NotificationCallback callback) {
        this.searchQuery = searchQuery;
        performSearch(callback);
    }

    public void findArticles(final String searchQuery) {
        findArticles(searchQuery, null);
    }
    
    public void clearSearch(final NotificationCallback callback) {
        searchQuery = null;
        retrieveNewsOfSites(callback);
    }
    
    public void clearSearch() {
        clearSearch(null);
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
    	newsService.getHtmlDescription(key, new AsyncCallback<EntityKey>() {
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
        newsService.getHtmlContent(key, new AsyncCallback<EntityKey>() {
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
    	newsService.getHtmlDescriptions(keys, callback);
    }
}
