package com.cee.news.store.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import com.cee.news.model.EntityKey;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;

public class LuceneSiteStore extends LuceneStoreBase implements SiteStore {
	
	private Query createSiteQuery(EntityKey siteKey) {
		return new TermQuery(new Term(LuceneConstants.FIELD_SITE_NAME, siteKey.getKey()));
	}
	
	private Query createAllSitesQuery() {
		return new MatchAllDocsQuery();
	}
	
	private Document getSiteDocument(IndexSearcher searcher, EntityKey siteKey) throws IOException {
		return getSingleDocument(searcher, createSiteQuery(siteKey));
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
			.addStoredField(LuceneConstants.FIELD_SITE_FEEDS, GSON.toJson(site.getFeeds()))
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
			site.setFeeds(GSON.<List<Feed>>fromJson(jsonFeeds, LuceneConstants.FEED_LIST_TYPE));
		}
		return site;
	}
	
	public LuceneSiteStore() {}
	
	public LuceneSiteStore(IndexWriter indexWriter) {
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
			Query q = createSiteQuery(EntityKey.get(name));
			return containsDocument(q);
		} catch (IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

	@Override
	public Site getSite(EntityKey key) throws StoreException {
		try {
			return createSiteFromDocument(getSiteDocument(key));
		} catch (IOException ioe) {
			throw new StoreException(null, ioe);
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
			Query query = createAllSitesQuery();
			return getSortedDocuments(query, LuceneConstants.SITE_NAME_SORT, LuceneConstants.FIELD_SITE_NAME, LuceneConstants.FIELD_SITE_NAME);
		} catch(IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

}
