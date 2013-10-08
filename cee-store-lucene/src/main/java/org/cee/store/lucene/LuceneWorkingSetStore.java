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
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.cee.news.model.EntityKey;
import org.cee.news.model.WorkingSet;
import org.cee.news.store.StoreException;
import org.cee.news.store.WorkingSetStore;

public class LuceneWorkingSetStore extends LuceneStoreBase implements WorkingSetStore {

	private Query createWorkingSetQuery(EntityKey wsKey) {
		return new TermQuery(new Term(LuceneConstants.FIELD_WORKING_SET_NAME, wsKey.getKey()));
	}
	
	private Query createAllWorkingSetsQuery() {
		return new MatchAllDocsQuery();
	}
	
	private Document getWorkingSetDocument(IndexSearcher searcher, EntityKey wsKey) throws IOException {
		return getSingleDocument(searcher, createWorkingSetQuery(wsKey));
	}
	
	private Document getWorkingSetDocument(EntityKey wsKey) throws IOException {
		IndexSearcher searcher = aquireSearcher();
		try {
			return getWorkingSetDocument(searcher, wsKey);
		} finally {
			releaseSearcher(searcher);
		}
	}
	
	private Document createWorkingSetDocument(WorkingSet workingSet) {
		return new DocumentBuilder()
			.addStringField(LuceneConstants.FIELD_WORKING_SET_NAME, workingSet.getName(), Field.Store.YES)
			.addStringField(LuceneConstants.FIELD_WORKING_SET_LANGUAGE, workingSet.getLanguage(), Field.Store.YES)
			.addStoredField(LuceneConstants.FIELD_WORKING_SET_SITES, GSON.toJson(workingSet.getSites()))
			.getDocument();
	}
	
	private WorkingSet createWorkingSetFromDocument(Document document) {
		if (document == null) {
			return null;
		}
		WorkingSet workingSet = new WorkingSet();
		workingSet.setName(getStringFieldOrNull(document, LuceneConstants.FIELD_WORKING_SET_NAME));
		workingSet.setLanguage(getStringFieldOrNull(document, LuceneConstants.FIELD_WORKING_SET_LANGUAGE));
		String jsonSites = getStringFieldOrNull(document, LuceneConstants.FIELD_WORKING_SET_SITES);
		if (jsonSites != null) {
			workingSet.setSites(GSON.<List<EntityKey>>fromJson(jsonSites, LuceneConstants.ENTITY_KEY_LIST_TYPE));
		}
		return workingSet;
	}
	
	public LuceneWorkingSetStore() {}
	
	public LuceneWorkingSetStore(IndexWriter indexWriter, LuceneAnalyzers analysers) {
		setIndexWriter(indexWriter);
		setAnalyzers(analysers);
	}
	
	@Override
	public EntityKey update(WorkingSet workingSet) throws StoreException {
		try {
			String wsName = workingSet.getName();
			EntityKey wsKey = EntityKey.get(wsName, wsName);
			deleteDocuments(createWorkingSetQuery(wsKey));
			addDocument(createWorkingSetDocument(workingSet), workingSet.getLanguage());
			commit();
			return wsKey;
		} catch(IOException ioe) {
			throw new StoreException(workingSet, ioe);
		}
	}

	@Override
	public void rename(String oldName, String newName) throws StoreException {
		try {
			EntityKey wsKey = EntityKey.get(oldName, oldName);
			Document document = new DocumentBuilder(getWorkingSetDocument(wsKey))
				.removeField(LuceneConstants.FIELD_WORKING_SET_NAME)
				.addStringField(LuceneConstants.FIELD_WORKING_SET_NAME, newName, Field.Store.YES)
				.getDocument();
			deleteDocuments(createWorkingSetQuery(wsKey));
			addDocument(document, getStringFieldOrNull(document, LuceneConstants.FIELD_WORKING_SET_LANGUAGE));
			commit();
		} catch(IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

	@Override
	public void delete(EntityKey key) throws StoreException {
		try {
			deleteDocuments(createWorkingSetQuery(key));
			commit();
		} catch(IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

	@Override
	public boolean contains(String name) throws StoreException {
		try {
			Query q = createWorkingSetQuery(EntityKey.get(name));
			return containsDocument(q);
		} catch (IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

	@Override
	public WorkingSet getWorkingSet(EntityKey key) throws StoreException {
		try {
			return createWorkingSetFromDocument(getWorkingSetDocument(key));
		} catch (IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

	@Override
	public long getWorkingSetCount() throws StoreException {
		try {
			return documentCount(createAllWorkingSetsQuery());
		} catch (IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

	@Override
	public List<EntityKey> getWorkingSetsOrderedByName() throws StoreException {
		try {
			Query query = createAllWorkingSetsQuery();
			return getSortedDocuments(query, LuceneConstants.WORKING_SET_NAME_SORT, LuceneConstants.FIELD_WORKING_SET_NAME, LuceneConstants.FIELD_WORKING_SET_NAME);
		} catch(IOException ioe) {
			throw new StoreException(null, ioe);
		}
	}

}
