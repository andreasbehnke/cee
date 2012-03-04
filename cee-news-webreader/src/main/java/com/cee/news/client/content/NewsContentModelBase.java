package com.cee.news.client.content;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.list.ContentModel;
import com.cee.news.client.list.DefaultListModel;
import com.cee.news.client.util.SafeHtmlUtil;
import com.cee.news.model.EntityKey;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class NewsContentModelBase extends DefaultListModel<EntityKey> implements ContentModel<EntityKey>, EntityKeyContentModel {

    protected final NewsServiceAsync newsService = NewsServiceAsync.Util.getInstance();

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