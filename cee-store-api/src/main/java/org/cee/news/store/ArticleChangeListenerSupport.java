package org.cee.news.store;

import java.util.ArrayList;
import java.util.List;

import org.cee.news.model.Article;
import org.cee.news.model.EntityKey;

public class ArticleChangeListenerSupport {

	private List<ArticleChangeListener> changeListeners;
    
	public void fireArticleChanged(EntityKey site, Article article) {
    	if (changeListeners == null) return;
    	for (ArticleChangeListener changeListener : changeListeners) {
			changeListener.onArticleChanged(site, article);
		}
    }
    
    public void addArticleChangeListener(ArticleChangeListener listener) {
    	if (changeListeners == null) {
    		changeListeners = new ArrayList<ArticleChangeListener>();
    	}
    	changeListeners.add(listener);
    }
}
