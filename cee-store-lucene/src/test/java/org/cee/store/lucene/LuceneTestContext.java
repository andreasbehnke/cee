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

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.cee.news.store.ArticleStore;
import org.cee.news.store.SiteStore;
import org.cee.news.store.WorkingSetStore;
import org.cee.search.ArticleSearchService;
import org.cee.store.lucene.LuceneAnalyzers;
import org.cee.store.lucene.LuceneArticleStore;
import org.cee.store.lucene.LuceneConstants;
import org.cee.store.lucene.LuceneSiteStore;
import org.cee.store.lucene.LuceneWorkingSetStore;
import org.cee.store.test.suite.TestContext;

public class LuceneTestContext implements TestContext {

	private SiteStore siteStore;

	private LuceneArticleStore articleStore;
	
	private WorkingSetStore workingSetStore;
	
	private IndexWriter createWriter() {
		try {
			StandardAnalyzer analyzer = new StandardAnalyzer(LuceneConstants.VERSION);
			Directory directory = new RAMDirectory();
			IndexWriterConfig config = new IndexWriterConfig(LuceneConstants.VERSION, analyzer);
			return new IndexWriter(directory, config);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void open() {
		LuceneAnalyzers analysers = new LuceneAnalyzers();
		siteStore = new LuceneSiteStore(createWriter(), analysers);
		IndexWriter articleWriter = createWriter();
		articleStore = new LuceneArticleStore(articleWriter, analysers);
		workingSetStore = new LuceneWorkingSetStore(createWriter(), analysers);
	}

	@Override
	public void close() {}

	@Override
	public SiteStore getSiteStore() {
		return siteStore;
	}

	@Override
	public ArticleStore getArticleStore() {
		return articleStore;
	}

	@Override
	public ArticleSearchService getArticleSearchService() {
		return articleStore;
	}

	@Override
	public WorkingSetStore getWorkingSetStore() {
		return workingSetStore;
	}

}
