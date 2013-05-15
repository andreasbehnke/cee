package com.cee.news.store.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.cee.news.search.ArticleSearchService;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.WorkingSetStore;
import com.cee.news.store.test.suite.TestContext;

public class LuceneTestContext implements TestContext {

	private SiteStore siteStore;

	private ArticleStore articleStore;
	
	private ArticleSearchService articleSearchService;

	private WorkingSetStore workingSetStore;
	
	private IndexWriter createWriter() {
		try {
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
			Directory directory = new RAMDirectory();
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
			return new IndexWriter(directory, config);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void open() {
		siteStore = new LuceneSiteStore(createWriter());
		articleStore = new LuceneArticleStore();
		articleSearchService = new LuceneArticleSearchService();
		workingSetStore = new LuceneWorkingSetStore(createWriter());
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
		return articleSearchService;
	}

	@Override
	public WorkingSetStore getWorkingSetStore() {
		return workingSetStore;
	}

}
