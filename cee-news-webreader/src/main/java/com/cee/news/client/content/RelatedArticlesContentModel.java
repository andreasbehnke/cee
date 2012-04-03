package com.cee.news.client.content;

import java.util.List;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RelatedArticlesContentModel extends NewsContentModelBase {
    
    private ArticleKey articleKey;
    
    private EntityKey workingSet;
    
    private void performRelatedSearch(final NotificationCallback callback) {
        newsService.getRelatedArticles(articleKey, workingSet, new AsyncCallback<List<ArticleKey>>() {    
            @Override
            public void onSuccess(List<ArticleKey> result) {
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
    
    public void getRelatedArticles(final ArticleKey articleKey, final EntityKey workingSet) {
        getRelatedArticles(articleKey, workingSet, null);
    }
    
    public void getRelatedArticles(final ArticleKey articleKey, final EntityKey workingSet, final NotificationCallback callback) {
        this.articleKey = articleKey;
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
