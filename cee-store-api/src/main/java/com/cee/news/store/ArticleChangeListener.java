package com.cee.news.store;

import com.cee.news.model.Article;
import com.cee.news.model.EntityKey;

public interface ArticleChangeListener {

	void onArticleChanged(EntityKey site, Article article);
}
