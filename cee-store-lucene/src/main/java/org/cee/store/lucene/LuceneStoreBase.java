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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.cee.store.EntityKey;

import com.google.gson.Gson;

public abstract class LuceneStoreBase {

	protected static final Gson GSON = new Gson();
	
	private IndexWriter indexWriter;
	
	private SearcherManager searcherManager;
	
	private Object searcherManagerLock = new Object();

	private LuceneAnalyzers analyzers;

	private SearcherManager getSearcherManager() throws IOException {
		if (searcherManager == null) {
			synchronized (searcherManagerLock) {
				if (searcherManager == null) {
					searcherManager = new SearcherManager(indexWriter, false, null);		
				}
			}
		}
		return searcherManager;
	}

	protected IndexSearcher aquireSearcher() throws IOException {
		return getSearcherManager().acquire();
	}

	protected void releaseSearcher(IndexSearcher searcher) throws IOException {
		getSearcherManager().release(searcher);
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
	
	public LuceneAnalyzers getAnalyzers() {
		return analyzers;
	}

	public void setAnalyzers(LuceneAnalyzers analysers) {
		this.analyzers = analysers;
	}
	
	protected Document getSingleDocument(IndexSearcher searcher, Query query) throws IOException {
		TopDocs topDocs = searcher.search(query, 1);
		if (topDocs.totalHits == 0) {
			return null;
		} else {
			return searcher.doc(topDocs.scoreDocs[0].doc);
		}
	}
	
	protected boolean containsDocument(Query query, IndexSearcher searcher) throws IOException {
		TopDocs topDocs = searcher.search(query, 1);
		if (topDocs.totalHits == 0) {
			return false;
		} else {
			return true;
		}
	}

	protected boolean containsDocument(Query query) throws IOException {
		IndexSearcher searcher = aquireSearcher();
		try {
			return containsDocument(query, searcher);
		} finally {
			releaseSearcher(searcher);
		}
	}
	
	protected long documentCount(Query query) throws IOException {
		IndexSearcher searcher = aquireSearcher();
		try {
			TopDocs topDocs = searcher.search(query, LuceneConstants.MAX_RESULT_SIZE);
			return topDocs.totalHits;
		} finally {
			releaseSearcher(searcher);
		}
	}
	
	protected List<EntityKey> getSortedDocuments(Query query, Sort sort, String keyField, String nameField) throws IOException {
		List<EntityKey> entityKeys = new ArrayList<EntityKey>();
		IndexSearcher searcher = aquireSearcher();
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
	
	protected Analyzer getAnalyzer(String language) {
		return analyzers.getAnalayserForLanguage(language);
	}
	
	protected void deleteDocuments(Query query) throws IOException {
		indexWriter.deleteDocuments(query);
	}

	protected void addDocument(Document document, String language) throws IOException {
		Analyzer analyzer = analyzers.getAnalayserForLanguage(language);
		indexWriter.addDocument(document, analyzer);
	}
	
	protected void commit() throws IOException {
		indexWriter.commit();
		getSearcherManager().maybeRefreshBlocking();
	}
}
