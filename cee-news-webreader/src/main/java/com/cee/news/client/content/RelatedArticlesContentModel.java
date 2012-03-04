package com.cee.news.client.content;

import java.util.List;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.model.EntityKey;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RelatedArticlesContentModel extends NewsContentModelBase {
    
    private String articleId;
    
    private String workingSet;
    
    private void performRelatedSearch(final NotificationCallback callback) {
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
    
    public void getRelatedArticles(final String articleId, final String workingSet) {
        getRelatedArticles(articleId, workingSet, null);
    }
    
    public void getRelatedArticles(final String articleId, final String workingSet, final NotificationCallback callback) {
        this.articleId = articleId;
        this.workingSet = workingSet;
        performRelatedSearch(callback);
    }
    
    public void refresh() {
        refresh(null);
    }
    
    public void refresh(NotificationCallback callback) {
        performRelatedSearch(callback);
    }
}
