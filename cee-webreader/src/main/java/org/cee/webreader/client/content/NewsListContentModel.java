package org.cee.webreader.client.content;

import java.util.ArrayList;
import java.util.List;

import org.cee.news.model.ArticleKey;
import org.cee.news.model.EntityKey;
import org.cee.webreader.client.async.NotificationCallback;
import org.cee.webreader.client.list.SelectionChangedEvent;
import org.cee.webreader.client.list.SelectionChangedHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class NewsListContentModel extends NewsContentModelBase implements SelectionChangedHandler<EntityKey> {
    
    private String searchQuery;
    
    private List<EntityKey> filteredSites = new ArrayList<EntityKey>();
    
    private EntityKey selectedWorkingSet;

    private void retrieveNewsOfSites(final NotificationCallback callback) {
        newsService.getArticlesOfSites(filteredSites, new AsyncCallback<List<ArticleKey>>() {
            public void onSuccess(List<ArticleKey> result) {
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
        newsService.findArticles(filteredSites, selectedWorkingSet, searchQuery, new AsyncCallback<List<ArticleKey>>() {
            
            @Override
            public void onSuccess(List<ArticleKey> result) {
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
    
    @Override
    public void onSelectionChange(SelectionChangedEvent<EntityKey> event) {
        this.selectedWorkingSet = event.getKey();
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
    
    public void getNewsOfSite(final EntityKey siteKey, final NotificationCallback callback) {
        filteredSites.clear();
        filteredSites.add(siteKey);
        refresh(callback);
    }
    
    public void getNewsOfSite(final EntityKey siteKey) {
        getNewsOfSite(siteKey, null);
    }    
    
    public void getNewsOfSites(final List<EntityKey> siteKeys, final NotificationCallback callback) {
        filteredSites = siteKeys;
        refresh(callback);
    }
    
    public void getNewsOfSites(final List<EntityKey> siteKeys) {
        getNewsOfSites(siteKeys, null);
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
}
