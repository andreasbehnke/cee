package com.cee.news.store.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import com.cee.news.search.ArticleSearchService;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.WorkingSetStore;
import com.cee.news.store.test.suite.TestContext;

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
	public void close() {
		// TODO Auto-generated method stub

	}

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
