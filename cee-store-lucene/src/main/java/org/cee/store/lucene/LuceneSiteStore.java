package org.cee.store.lucene;

/*
 * #%L
 * Content Extraction Engine - News Store Lucene
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.cee.news.model.EntityKey;
import org.cee.news.model.Feed;
import org.cee.news.model.Site;
import org.cee.news.store.SiteStore;
import org.cee.news.store.StoreException;

public class LuceneSiteStore extends LuceneStoreBase implements SiteStore {
	
	private Query createSiteQuery(EntityKey siteKey) {
		return new TermQuery(new Term(LuceneConstants.FIELD_SITE_NAME, siteKey.getKey()));
	}
	
	private Query createSitesQuery(List<EntityKey> siteKeys) {
		if (siteKeys.size() == 1) {
			return createSiteQuery(siteKeys.get(0));
		} else {
			BooleanQuery query = new BooleanQuery();
			query.setMinimumNumberShouldMatch(1);
			float boost = siteKeys.size() * 10;
			for (EntityKey siteKey: siteKeys) {
				Query siteQuery = createSiteQuery(siteKey);
				siteQuery.setBoost(boost);
				query.add(siteQuery, BooleanClause.Occur.SHOULD);
				boost -= 10;
			}
			return query;
		}
	}
	
	private Query createAllSitesQuery() {
		return new MatchAllDocsQuery();
	}
	
	private Document getSiteDocument(IndexSearcher searcher, EntityKey siteKey) throws IOException {
		return getSingleDocument(searcher, createSiteQuery(siteKey));
	}
	
	private Document getSiteDocument(EntityKey siteKey) throws IOException {
		IndexSearcher searcher = aquireSearcher();
		try {
			return getSiteDocument(searcher, siteKey);
		} finally {
			releaseSearcher(searcher);
		}
	}
	
	private Document createSiteDocument(Site site) {
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
	
	public LuceneSiteStore(IndexWriter indexWriter, LuceneAnalyzers analysers) {
		setIndexWriter(indexWriter);
		setAnalyzers(analysers);
	}

	@Override
	public EntityKey update(Site site) throws StoreException {
		try {
			String siteName = site.getName();
			EntityKey siteKey = EntityKey.get(siteName, siteName);
			deleteDocuments(createSiteQuery(siteKey));
			addDocument(createSiteDocument(site), site.getLanguage());
			commit();
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
			IndexSearcher searcher = aquireSearcher();
			Query query = createSitesQuery(keys);
			try {
				TopDocs topDocs = searcher.search(query, LuceneConstants.MAX_RESULT_SIZE);
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
	                sites.add(createSiteFromDocument(searcher.doc(scoreDoc.doc)));
                }
			} finally {
				releaseSearcher(searcher);
			}
			if (sites.size() != keys.size()) {
				throw new StoreException("EntityKey list and result list have different size");
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
