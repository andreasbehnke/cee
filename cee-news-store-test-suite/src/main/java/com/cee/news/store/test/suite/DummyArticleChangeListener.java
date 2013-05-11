package com.cee.news.store.test.suite;

import com.cee.news.model.Article;
import com.cee.news.model.EntityKey;
import com.cee.news.store.ArticleChangeListener;

public class DummyArticleChangeListener implements ArticleChangeListener {
	
	public String createdSiteName;

	public String createdArticleId;
	
	public String changedSiteName;

	public String changedArticleId;
	
	@Override
	public void onArticleCreated(EntityKey site, Article article) {
		createdSiteName = site.getName();
		createdArticleId = article.getExternalId();
	}

	@Override
	public void onArticleChanged(EntityKey site, Article article) {
		changedSiteName = site.getName();
		changedArticleId = article.getExternalId();			
	}
	
	public void reset() {
		createdSiteName = null;
		createdArticleId = null;
		changedSiteName = null;
		changedArticleId = null;
    }
}