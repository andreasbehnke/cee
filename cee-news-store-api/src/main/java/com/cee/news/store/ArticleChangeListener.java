package com.cee.news.store;

import com.cee.news.model.Article;
import com.cee.news.model.Site;

public interface ArticleChangeListener {

	void onArticleCreated(Site site, Article article);
	
	void onArticleChanged(Site site, Article article);
}
