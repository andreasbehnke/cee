package org.cee.webreader.client.content;

import java.util.ArrayList;
import java.util.List;

import org.cee.news.model.ArticleKey;
import org.cee.webreader.client.list.CellListContentModel;
import org.cee.webreader.client.list.ContentModel;
import org.cee.webreader.client.list.DefaultListModel;
import org.cee.webreader.client.util.SafeHtmlUtil;

import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class NewsContentModelBase extends DefaultListModel<ArticleKey> implements ContentModel<ArticleKey>, CellListContentModel<ArticleKey> {

    protected final NewsServiceAsync newsService = NewsServiceAsync.Util.getInstance();

    @Override
    public void getContentTitle(final HasSafeHtml target, ArticleKey key) {
        if (keys == null) {
            throw new IllegalStateException("Headlines have not been set yet!");
        }
        target.setHTML(SafeHtmlUtil.sanitize(key.getName()));
    }

    @Override
    public void getContentDescription(final HasSafeHtml target, ArticleKey key) {
    	newsService.getHtmlDescription(key, new AsyncCallback<EntityContent<ArticleKey>>() {
            public void onSuccess(EntityContent<ArticleKey> result) {
                target.setHTML(SafeHtmlUtil.sanitize(result.getContent()) );
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load description!");
            }
        });
    }

    @Override
    public void getContent(final HasSafeHtml target, ArticleKey key) {
        newsService.getHtmlContent(key, new AsyncCallback<EntityContent<ArticleKey>>() {
            public void onSuccess(EntityContent<ArticleKey> result) {
            	target.setHTML(SafeHtmlUtil.sanitize(result.getContent()) );
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load content!");
            }
        });
    }
    
    @Override
    public void getContent(ArrayList<ArticleKey> keys, AsyncCallback<List<EntityContent<ArticleKey>>> callback) {
        newsService.getHtmlDescriptions(keys, callback);
    }
}
