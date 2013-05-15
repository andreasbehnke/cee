package com.cee.news.store.lucene;

import java.util.List;

import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.cee.news.search.ArticleSearchService;
import com.cee.news.search.SearchException;

public class LuceneArticleSearchService extends LuceneStoreBase implements ArticleSearchService {

	@Override
	public List<String> getSupportedLanguages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArticleKey> findArticles(List<EntityKey> sites,
			String fulltextSearchQuery) throws SearchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArticleKey> findRelatedArticles(List<EntityKey> sites,
			ArticleKey articleKey) throws SearchException {
		// TODO Auto-generated method stub
		return null;
	}

}
