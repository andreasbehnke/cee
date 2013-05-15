package com.cee.news.store.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;

import com.cee.news.model.EntityKey;
import com.google.gson.Gson;

public abstract class LuceneStoreBase {

	protected static final Gson GSON = new Gson();
	
	protected IndexWriter indexWriter;
	
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

	protected IndexSearcher aquireSearcher(boolean refresh) throws IOException {
		return getSearcherManager(refresh).acquire();
	}

	protected void releaseSearcher(IndexSearcher searcher) throws IOException {
		getSearcherManager(false).release(searcher);
	}

	protected String getStringFieldOrNull(Document document, String fieldName) {
		IndexableField field = document.getField(fieldName);
		if (field == null) {
			return null;
		} else {
			return field.stringValue();
		}
	}

	public IndexWriter getIndexWriter() {
		return indexWriter;
	}

	public void setIndexWriter(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}

	protected Document getSingleDocument(IndexSearcher searcher, Query query) throws IOException {
		TopDocs topDocs = searcher.search(query, 1);
		if (topDocs.totalHits == 0) {
			return null;
		} else {
			return searcher.doc(topDocs.scoreDocs[0].doc);
		}
	}

	protected boolean containsDocument(Query query) throws IOException {
		IndexSearcher searcher = aquireSearcher(true);
		try {
			TopDocs topDocs = searcher.search(query, 1);
			if (topDocs.totalHits == 0) {
				return false;
			} else {
				return true;
			}
		} finally {
			releaseSearcher(searcher);
		}
	}
	
	protected long documentCount(Query query) throws IOException {
		IndexSearcher searcher = aquireSearcher(true);
		try {
			TopDocs topDocs = searcher.search(query, LuceneConstants.MAX_RESULT_SIZE);
			return topDocs.totalHits;
		} finally {
			releaseSearcher(searcher);
		}
	}
	
	protected List<EntityKey> getSortedDocuments(Query query, Sort sort, String keyField, String nameField) throws IOException {
		List<EntityKey> entityKeys = new ArrayList<EntityKey>();
		IndexSearcher searcher = aquireSearcher(true);
		try {
			TopDocs topDocs = searcher.search(query, LuceneConstants.MAX_RESULT_SIZE, sort);
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = searcher.doc(scoreDoc.doc);
				String key = doc.get(keyField);
				String name = doc.get(nameField);
				entityKeys.add(EntityKey.get(key, name));
			}
		} finally {
			releaseSearcher(searcher);
		}
		return entityKeys;
	}
}
