package org.cee.news.store;

import org.cee.news.model.Article;
import org.cee.news.model.EntityKey;

public interface ArticleChangeListener {

	void onArticleChanged(EntityKey site, Article article);
}
