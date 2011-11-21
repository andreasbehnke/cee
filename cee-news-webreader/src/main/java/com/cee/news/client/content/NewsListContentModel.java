package com.cee.news.client.content;

import java.util.List;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.list.ContentModel;
import com.cee.news.client.list.DefaultListModel;
import com.cee.news.client.list.EntityKeyUtil;
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
    
    public void updateFromSite(final String siteKey) {
    	updateFromSite(siteKey, null);
    }
    
    public void updateFromSite(final String siteKey, final NotificationCallback callback) {
        service.getArticlesOfSite(siteKey, new AsyncCallback<List<EntityKey>>() {
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
    
    public void updateFromArticle(final String articleId, final String workingSet) {
    	updateFromArticle(articleId, workingSet, null);
    }
    
    public void updateFromArticle(final String articleId, final String workingSet, final NotificationCallback callback) {
    	service.getRelatedArticles(articleId, workingSet, new AsyncCallback<List<EntityKey>>() {
			
			@Override
			public void onSuccess(List<EntityKey> result) {
				setKeys(result);
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
    	final EntityKey entityKey = EntityKeyUtil.getEntityKey(keys, key);
        service.getHtmlDescription(entityKey, new AsyncCallback<EntityContent>() {
            public void onSuccess(EntityContent result) {
                target.setHTML(SafeHtmlUtil.sanitize(result.getHtmlContent()) );
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load description!");
            }
        });
    }

    public void getContent(final HasSafeHtml target, String key) {
    	final EntityKey entityKey = EntityKeyUtil.getEntityKey(keys, key);
        service.getHtmlContent(entityKey, new AsyncCallback<EntityContent>() {
            public void onSuccess(EntityContent result) {
            	target.setHTML(SafeHtmlUtil.sanitize(result.getHtmlContent()) );
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load content!");
            }
        });
    }
}
