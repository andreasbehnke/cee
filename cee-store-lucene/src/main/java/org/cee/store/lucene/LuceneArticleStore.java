package org.cee.store.lucene;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.cee.news.model.Article;
import org.cee.news.model.ArticleKey;
import org.cee.news.model.EntityKey;
import org.cee.news.model.TextBlock;
import org.cee.news.model.WorkingSet;
import org.cee.news.store.ArticleChangeListener;
import org.cee.news.store.ArticleChangeListenerSupport;
import org.cee.news.store.ArticleStore;
import org.cee.news.store.StoreException;
import org.cee.search.ArticleSearchService;
import org.cee.search.SearchException;

public class LuceneArticleStore extends LuceneStoreBase implements ArticleStore, ArticleSearchService {
	
	private interface ArticleKeyFilter {
		
		boolean accept(String key, ScoreDoc scoreDoc);
		
	}

	private ArticleChangeListenerSupport listenerSupport = new ArticleChangeListenerSupport();
	
	private Query createArticleQuery(ArticleKey articleKey) {
		BooleanQuery query = new BooleanQuery();
		query.add(new BooleanClause(new TermQuery(new Term(LuceneConstants.FIELD_ARTICLE_EXTERNAL_ID, articleKey.getKey())), Occur.MUST));
		query.add(new BooleanClause(new TermQuery(new Term(LuceneConstants.FIELD_ARTICLE_SITE, articleKey.getSiteKey())), Occur.MUST));
		return query;
	}
	
	private Query createLanguageQuery(String language) {
		return new TermQuery(new Term(LuceneConstants.FIELD_ARTICLE_LANGUAGE, language));
	}
	
	private Query createQueryArticlesOfSites(List<EntityKey> sites) {
		if (sites.size() == 1) {
			return new TermQuery(new Term(LuceneConstants.FIELD_ARTICLE_SITE, sites.get(0).getKey()));
		} else {
			BooleanQuery query = new BooleanQuery();
			query.setMinimumNumberShouldMatch(1);
			for (EntityKey site : sites) {
				query.add(new TermQuery(new Term(LuceneConstants.FIELD_ARTICLE_SITE, site.getKey())), BooleanClause.Occur.SHOULD);
			}
			return query;
		}
	}
	
	private Query createFindArticlesQuery(List<EntityKey> sites, String fulltextSearchQuery, String language) throws org.apache.lucene.queryparser.classic.ParseException {
		Analyzer analyzer = getAnalyzer(language);
		
		MultiFieldQueryParser parser = new MultiFieldQueryParser(LuceneConstants.VERSION, LuceneConstants.ARTICLE_FULLTEXT_SEARCH_FIELDS, analyzer, LuceneConstants.ARTICLE_FULLTEXT_SEARCH_BOOSTS);
		Query fulltextQuery = parser.parse(QueryParser.escape(fulltextSearchQuery));
		Query sitesQuery = createQueryArticlesOfSites(sites);
		Query languageQuery = createLanguageQuery(language);
		
		BooleanQuery query = new BooleanQuery();
		query.add(sitesQuery, Occur.MUST);
		query.add(languageQuery, Occur.MUST);
		query.add(fulltextQuery, Occur.MUST);
		return query;
	}
	
	private Query boostRelatedQuery(Query relatedQuery) {
		List<BooleanClause> clauses = ((BooleanQuery)relatedQuery).clauses();
		for (BooleanClause booleanClause : clauses) {
	        TermQuery tq = (TermQuery)booleanClause.getQuery();
	        Float fieldBoost = LuceneConstants.ARTICLE_FULLTEXT_SEARCH_BOOSTS.get(tq.getTerm().field());
	        if (fieldBoost != null) {
	        	tq.setBoost(fieldBoost * tq.getBoost());
	        }
	    }
		return relatedQuery;
	}
	
	private Query createRelatedArticlesQuery(List<EntityKey> sites, ArticleKey reference, IndexSearcher searcher, String language) throws IOException {
		Query articleQuery = createArticleQuery(reference);
		TopDocs topDocs = searcher.search(articleQuery, 1);
		if (topDocs.totalHits == 0) {
			return new BooleanQuery(true);
		}
		MoreLikeThis mlt = new MoreLikeThis(searcher.getIndexReader());
		mlt.setFieldNames(LuceneConstants.ARTICLE_RELATED_SEARCH_FIELDS);
		mlt.setMaxQueryTerms(20);
		mlt.setBoost(true);
		mlt.setMinTermFreq(0);
		mlt.setMinDocFreq(0);
		Query relatedQuery = boostRelatedQuery(mlt.like(topDocs.scoreDocs[0].doc));
		
		BooleanQuery query = new BooleanQuery();
		query.add(new BooleanClause(relatedQuery, Occur.MUST));
		query.add(new BooleanClause(createQueryArticlesOfSites(sites), Occur.MUST));
		return query;
	}
	
	private List<ArticleKey> getKeys(Query query, IndexSearcher searcher, Sort sort, ArticleKeyFilter filter) throws IOException {
		List<ArticleKey> entityKeys = new ArrayList<ArticleKey>();
		TopDocs topDocs = null;
		if (sort != null) {
			topDocs = searcher.search(query, LuceneConstants.MAX_RESULT_SIZE, sort);
		} else {
			topDocs = searcher.search(query, LuceneConstants.MAX_RESULT_SIZE);
		}
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			String key = doc.get(LuceneConstants.FIELD_ARTICLE_EXTERNAL_ID);
			if (filter == null || filter.accept(key, scoreDoc)) {
				String name = doc.get(LuceneConstants.FIELD_ARTICLE_TITLE);
				String siteName = doc.get(LuceneConstants.FIELD_ARTICLE_SITE);
				entityKeys.add(ArticleKey.get(name, key, siteName, scoreDoc.score));
			}
		}
		return entityKeys;
	}
	
	private List<ArticleKey> getKeys(Query query, Sort sort) throws IOException {
		IndexSearcher searcher = aquireSearcher();
		try {
			return getKeys(query, searcher, sort, null);
		} finally {
			releaseSearcher(searcher);
		}
	}
	
	private List<ArticleKey> getKeys(Query query, IndexSearcher searcher, ArticleKeyFilter filter) throws IOException {
		return getKeys(query, searcher, null, filter);
	}
	
	private List<ArticleKey> getKeys(Query query) throws IOException {
		return getKeys(query, (Sort)null);
	}
	
	private Document getArticleDocument(ArticleKey articleKey, IndexSearcher searcher) throws IOException {
		return getSingleDocument(searcher, createArticleQuery(articleKey));
	}
	
	private Document getArticleDocument(ArticleKey articleKey) throws IOException {
		IndexSearcher searcher = aquireSearcher();
		try {
			return getArticleDocument(articleKey, searcher);
		} finally {
			releaseSearcher(searcher);
		}
	}
	
	private Document createArticleDocument(EntityKey site, Article article) {
		DocumentBuilder builder = new DocumentBuilder()
			.addStringField(LuceneConstants.FIELD_ARTICLE_EXTERNAL_ID, article.getExternalId(), Field.Store.YES)
			.addStringField(LuceneConstants.FIELD_ARTICLE_SITE, site.getKey(), Field.Store.YES)
			.addStringField(LuceneConstants.FIELD_ARTICLE_LANGUAGE, article.getLanguage(), Field.Store.YES)
			.addStringField(LuceneConstants.FIELD_ARTICLE_LOCATION, article.getLocation(), Field.Store.YES)
			.addTextFieldWithTermVectors(LuceneConstants.FIELD_ARTICLE_TITLE, article.getTitle(), Field.Store.YES)
			.addTextFieldWithTermVectors(LuceneConstants.FIELD_ARTICLE_SHORT_TEXT, article.getShortText(), Field.Store.YES)
			.addDateField(LuceneConstants.FIELD_ARTICLE_PUBLISHED_DATE, article.getPublishedDate(), Field.Store.YES);
		for (TextBlock block : article.getContent()) {
	        builder.addTextFieldWithTermVectors(LuceneConstants.FIELD_ARTICLE_CONTENT, block.getContent(), Field.Store.YES);
        }
		return builder.getDocument();
	}
	
	private Article createArticleFromDocument(Document articleDocument, boolean withContext) throws ParseException {
		if (articleDocument == null) {
			return null;
		}
		Article article = new Article();
		article.setExternalId(getStringFieldOrNull(articleDocument, LuceneConstants.FIELD_ARTICLE_EXTERNAL_ID));
		article.setLanguage(getStringFieldOrNull(articleDocument, LuceneConstants.FIELD_ARTICLE_LANGUAGE));
		article.setLocation(getStringFieldOrNull(articleDocument, LuceneConstants.FIELD_ARTICLE_LOCATION));
		article.setTitle(getStringFieldOrNull(articleDocument, LuceneConstants.FIELD_ARTICLE_TITLE));
		article.setShortText(getStringFieldOrNull(articleDocument, LuceneConstants.FIELD_ARTICLE_SHORT_TEXT));
		Calendar published = Calendar.getInstance();
		published.setTime(DateTools.stringToDate(getStringFieldOrNull(articleDocument, LuceneConstants.FIELD_ARTICLE_PUBLISHED_DATE)));
		article.setPublishedDate(published);
		if (withContext) {
			IndexableField[] fields = articleDocument.getFields(LuceneConstants.FIELD_ARTICLE_CONTENT);
			List<TextBlock> blocks = new ArrayList<TextBlock>();
			for (IndexableField field : fields) {
	            blocks.add(new TextBlock(field.stringValue()));
            }
			article.setContent(blocks);
		}
		return article;
	}
	
	public LuceneArticleStore() {}

	public LuceneArticleStore(IndexWriter indexWriter, LuceneAnalyzers analyzers) {
		setIndexWriter(indexWriter);
		setAnalyzers(analyzers);
	}

	@Override
	public ArticleKey update(EntityKey site, Article article) throws StoreException {
		try {
			String siteKey = site.getName();
			ArticleKey articleKey = ArticleKey.get(article.getTitle(), article.getExternalId(), siteKey);
			deleteDocuments(createArticleQuery(articleKey));
			addDocument(createArticleDocument(site, article), article.getLanguage());
			commit();
			// we can not determine if we have updated or created a new article. Always fire article created event.
			listenerSupport.fireArticleChanged(site, article);
			return articleKey;
		} catch(IOException ioe) {
			throw new StoreException(site, ioe);
		}
	}
	
	@Override
	public boolean contains(EntityKey site, String externalId) throws StoreException {
		ArticleKey articleKey = ArticleKey.get(null, externalId, site.getKey());
	    try {
	        return containsDocument(createArticleQuery(articleKey));
        } catch (IOException e) {
        	throw new StoreException(null, e);
        }
	}
	
	@Override
	public List<ArticleKey> addNewArticles(EntityKey siteKey, List<Article> articles) throws StoreException {
		try {
			IndexSearcher searcher = aquireSearcher();
			List<ArticleKey> articleKeys = new ArrayList<ArticleKey>();
			try {
			    for (Article article : articles) {
			    	ArticleKey articleKey = ArticleKey.get(article.getTitle(), article.getExternalId(), siteKey.getKey());
			    	if (!containsDocument(createArticleQuery(articleKey), searcher)) {
			    		addDocument(createArticleDocument(siteKey, article), article.getLanguage());
			    		articleKeys.add(articleKey);
			    		listenerSupport.fireArticleChanged(siteKey, article);
			    	}
		        }
			    return articleKeys;
			} finally {
				releaseSearcher(searcher);
				commit();
			}
		} catch (IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

	@Override
	public void addArticleChangeListener(ArticleChangeListener listener) {
		listenerSupport.addArticleChangeListener(listener);
	}

	@Override
	public Article getArticle(ArticleKey key, boolean withContent) throws StoreException {
		try {
			return createArticleFromDocument(getArticleDocument(key), withContent);
		} catch (IOException ioe) {
			throw new StoreException(null, ioe);
		} catch (ParseException pe) {
			throw new StoreException(null, pe);
        }
	}

	@Override
	public List<Article> getArticles(List<ArticleKey> keys, boolean withContent) throws StoreException {
		try {
			List<Article> articles = new ArrayList<Article>();
			IndexSearcher searcher = aquireSearcher();
			try {
				for (ArticleKey articleKey : keys) {
					Document articleDocument = getArticleDocument(articleKey);
		            articles.add(createArticleFromDocument(articleDocument, withContent));
	            }
				if (articles.size() != keys.size()) {
					throw new StoreException("EntityKey list and result list have different size");
				}
				return articles;
			} finally {
				releaseSearcher(searcher);
			}
		} catch(IOException ioe) {
			throw new StoreException(null, ioe);
		} catch(ParseException pe) {
			throw new StoreException(null, pe);
		}
	}

	@Override
	public List<ArticleKey> getArticlesOrderedByDate(EntityKey siteKey) throws StoreException {
		try {
			ArrayList<EntityKey> sites = new ArrayList<EntityKey>();
			sites.add(siteKey);
			return getKeys(createQueryArticlesOfSites(sites), LuceneConstants.ARTICLE_PUBLISHED_SORT);
		} catch(IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

	@Override
	public List<ArticleKey> getArticlesOrderedByDate(List<EntityKey> siteKeys) throws StoreException {
		try {
			return getKeys(createQueryArticlesOfSites(siteKeys), LuceneConstants.ARTICLE_PUBLISHED_SORT);
		} catch(IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

	@Override
	public List<ArticleKey> getArticlesOrderedByDate(WorkingSet workingSet) throws StoreException {
		try {
			return getKeys(createQueryArticlesOfSites(workingSet.getSites()), LuceneConstants.ARTICLE_PUBLISHED_SORT);
		} catch(IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

	@Override
	public List<String> getSupportedLanguages() {
		return getAnalyzers().getSupportedLanguages();
	}

	@Override
	public List<ArticleKey> findArticles(List<EntityKey> sites, String fulltextSearchQuery, String language) throws SearchException {
		try {
			if (fulltextSearchQuery.trim().length() == 0) {
				return new ArrayList<ArticleKey>();
			}
			return getKeys(createFindArticlesQuery(sites, fulltextSearchQuery, language));
		} catch(IOException ioe) {
			throw new SearchException("Could not search for \"" + fulltextSearchQuery + "\"", ioe);
		} catch (org.apache.lucene.queryparser.classic.ParseException pe) {
			throw new SearchException("Could not parse query \"" + fulltextSearchQuery + "\"", pe);
        }
	}

	@Override
	public List<ArticleKey> findRelatedArticles(List<EntityKey> sites, ArticleKey articleKey, String language) throws SearchException {
		try {
			IndexSearcher searcher = aquireSearcher();
			try {
				final String relatedKey = articleKey.getKey();
				BooleanQuery query = (BooleanQuery)createRelatedArticlesQuery(sites, articleKey, searcher, language);
				
				return getKeys(
						query, 
						searcher, 
						new ArticleKeyFilter() {
							
							boolean isFirst = true;
							
							boolean rejectAll = false;
							
							float minScore;
							
							@Override
							public boolean accept(String key, ScoreDoc scoreDoc) {
								if (rejectAll) {
									return false;
								}
								if (relatedKey.equals(key)) {
									return false;
								}
								if (isFirst) {
									isFirst = false;
									if (scoreDoc.score < 0.36f) {
										rejectAll = true;
										return false;
									}
									//calculate min score
									minScore = scoreDoc.score / 3.3f;
									return true;
								}
								if (scoreDoc.score < minScore) {
									return false;
								}
								return true;
							}
						});
			} finally {
				releaseSearcher(searcher);
			}
		} catch (IOException ioe) {
			throw new SearchException("Could not find related article", ioe);
		}
	}
}
