package org.cee.store.test.suite;

import java.util.ArrayList;
import java.util.List;

import org.cee.news.model.Article;
import org.cee.news.model.EntityKey;
import org.cee.news.store.ArticleChangeListener;

public class DummyArticleChangeListener implements ArticleChangeListener {
	
	public List<String> changedSiteNames = new ArrayList<String>();

	public List<String> changedArticleIds = new ArrayList<String>();
	
	@Override
	public void onArticleChanged(EntityKey site, Article article) {
		changedSiteNames.add(site.getName());
		changedArticleIds.add(article.getExternalId());			
	}
	
	public void reset() {
		changedSiteNames = new ArrayList<String>();
		changedArticleIds = new ArrayList<String>();
    }
}