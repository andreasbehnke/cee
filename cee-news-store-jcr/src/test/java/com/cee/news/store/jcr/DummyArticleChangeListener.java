package com.cee.news.store.jcr;

import com.cee.news.model.Article;
import com.cee.news.model.Site;
import com.cee.news.store.ArticleChangeListener;

public class DummyArticleChangeListener implements ArticleChangeListener {
	
	public String createdSiteName;

	public String createdArticleId;
	
	public String changedSiteName;

	public String changedArticleId;
	
	@Override
	public void onArticleCreated(Site site, Article article) {
		createdSiteName = site.getName();
		createdArticleId = article.getId();
	}

	@Override
	public void onArticleChanged(Site site, Article article) {
		changedSiteName = site.getName();
		changedArticleId = article.getId();			
	}
	
	public void reset() {
		createdSiteName = null;
		createdArticleId = null;
		changedSiteName = null;
		changedArticleId = null;
    }
}