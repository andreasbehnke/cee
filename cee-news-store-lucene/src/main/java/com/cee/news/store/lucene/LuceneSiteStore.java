package com.cee.news.store.lucene;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import com.cee.news.model.EntityKey;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LuceneSiteStore implements SiteStore {
	
	private final static Type FEED_TYPE = new TypeToken<List<Feed>>(){}.getType();
	
	private final static Sort SITE_NAME_SORT = new Sort(new SortField(LuceneConstants.FIELD_SITE_NAME, SortField.Type.STRING, false));
	
	private final static int MAX_RESULT_SIZE = 500;
	
	private Gson gson = new Gson();
	
	private IndexWriter indexWriter;
	
	private SearcherManager searcherManager;
	
	private Object searcherManagerLock = new Object();
	
	private SearcherManager getSearcherManager(boolean refresh) throws IOException {
		if (searcherManager == null) {
			synchronized (searcherManagerLock) {
				if (searcherManager == null) {
					searcherManager = new SearcherManager(indexWriter, false, null);		
				}
			}
		}
		if (refresh) {
			searcherManager.maybeRefreshBlocking();
		}
		return searcherManager;
	}
	
	private IndexSearcher aquireSearcher(boolean refresh) throws IOException {
		return getSearcherManager(refresh).acquire();
	}
	
	private void releaseSearcher(IndexSearcher searcher) throws IOException {
		getSearcherManager(false).release(searcher);
	}
	
	private Query createSiteQuery(EntityKey siteKey) {
		return new TermQuery(new Term(LuceneConstants.FIELD_SITE_NAME, siteKey.getKey()));
	}
	
	private Query createAllSitesQuery() {
		return new MatchAllDocsQuery();
	}
	
	private Document getSiteDocument(IndexSearcher searcher, EntityKey siteKey) throws IOException {
		Query q = createSiteQuery(siteKey);
		TopDocs topDocs = searcher.search(q, 1);
		if (topDocs.totalHits == 0) {
			return null;
		} else {
			return searcher.doc(topDocs.scoreDocs[0].doc);
		}
	}
	
	private Document getSiteDocument(EntityKey siteKey) throws IOException {
		IndexSearcher searcher = aquireSearcher(true);
		try {
			return getSiteDocument(searcher, siteKey);
		} finally {
			releaseSearcher(searcher);
		}
	}
	
	private Document createSiteDocument(EntityKey siteKey, Site site) {
		return new DocumentBuilder()
			.addStringField(LuceneConstants.FIELD_SITE_NAME, site.getName(), Field.Store.YES)
			.addTextField(LuceneConstants.FIELD_SITE_TITLE, site.getTitle(), Field.Store.YES)
			.addStringField(LuceneConstants.FIELD_SITE_LANGUAGE, site.getLanguage(), Field.Store.YES)
			.addStringField(LuceneConstants.FIELD_SITE_LOCATION, site.getLocation(), Field.Store.YES)
			.addTextField(LuceneConstants.FIELD_SITE_DESCRIPTION, site.getDescription(), Field.Store.YES)
			.addStoredField(LuceneConstants.FIELD_SITE_FEEDS, gson.toJson(site.getFeeds()))
			.getDocument();
	}
	
	private Site createSiteFromDocument(Document siteDocument) {
		if (siteDocument == null) {
			return null;
		}
		Site site = new Site();
		site.setName(getStringFieldOrNull(siteDocument, LuceneConstants.FIELD_SITE_NAME));
		site.setTitle(getStringFieldOrNull(siteDocument, LuceneConstants.FIELD_SITE_TITLE));
		site.setLanguage(getStringFieldOrNull(siteDocument, LuceneConstants.FIELD_SITE_LANGUAGE));
		site.setLocation(getStringFieldOrNull(siteDocument, LuceneConstants.FIELD_SITE_LOCATION));
		site.setDescription(getStringFieldOrNull(siteDocument, LuceneConstants.FIELD_SITE_DESCRIPTION));
		String jsonFeeds = getStringFieldOrNull(siteDocument, LuceneConstants.FIELD_SITE_FEEDS);
		if (jsonFeeds != null) {
			site.setFeeds(gson.<List<Feed>>fromJson(jsonFeeds, FEED_TYPE));
		}
		return site;
	}
	
	private String getStringFieldOrNull(Document document, String fieldName) {
		IndexableField field = document.getField(fieldName);
		if (field == null) {
			return null;
		} else {
			return field.stringValue();
		}
	}
	
	public LuceneSiteStore() {}
	
	public LuceneSiteStore(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}

	public IndexWriter getIndexWriter() {
		return indexWriter;
	}

	public void setIndexWriter(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}

	@Override
	public EntityKey update(Site site) throws StoreException {
		try {
			String siteName = site.getName();
			EntityKey siteKey = EntityKey.get(siteName, siteName);
			indexWriter.deleteDocuments(createSiteQuery(siteKey));
			indexWriter.addDocument(createSiteDocument(siteKey, site));
			indexWriter.commit();
			return siteKey;
		} catch(IOException ioe) {
			throw new StoreException(site, ioe);
		}
	}

	@Override
	public boolean contains(String name) throws StoreException {
		try {
			IndexSearcher searcher = aquireSearcher(true);
			EntityKey siteKey = EntityKey.get(name);
			try {
				Query q = createSiteQuery(siteKey);
				TopDocs topDocs = searcher.search(q, 1);
				if (topDocs.totalHits == 0) {
					return false;
				} else {
					return true;
				}
			} finally {
				releaseSearcher(searcher);
			}
		} catch (IOException ioe) {
			throw new StoreException(name, ioe);
		}
	}

	@Override
	public Site getSite(EntityKey key) throws StoreException {
		try {
			return createSiteFromDocument(getSiteDocument(key));
		} catch (IOException ioe) {
			throw new StoreException(key, ioe);
		}
	}

	@Override
	public List<Site> getSites(List<EntityKey> keys) throws StoreException {
		try {
			List<Site> sites = new ArrayList<Site>();
			IndexSearcher searcher = aquireSearcher(true);
			try {
				for (EntityKey siteKey : keys) {
					sites.add(createSiteFromDocument(getSiteDocument(searcher, siteKey)));
				}
			} finally {
				releaseSearcher(searcher);
			}
			return sites;
		} catch(IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

	@Override
	public List<EntityKey> getSitesOrderedByName() throws StoreException {
		try {
			List<EntityKey> siteKeys = new ArrayList<EntityKey>();
			IndexSearcher searcher = aquireSearcher(true);
			try {
				Query q = createAllSitesQuery();
				TopDocs topDocs = searcher.search(q, MAX_RESULT_SIZE, SITE_NAME_SORT);
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					String siteName = searcher.doc(scoreDoc.doc).get(LuceneConstants.FIELD_SITE_NAME);
					siteKeys.add(EntityKey.get(siteName, siteName));
				}
			} finally {
				releaseSearcher(searcher);
			}
			return siteKeys;
		} catch(IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

}
