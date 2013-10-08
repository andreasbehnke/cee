package org.cee.store.test.suite;

/*
 * #%L
 * Content Extraction Engine - News Store Test Suite
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

import org.cee.news.store.ArticleStore;
import org.cee.news.store.SiteStore;
import org.cee.news.store.WorkingSetStore;
import org.cee.search.ArticleSearchService;
import org.junit.After;
import org.junit.Before;

public abstract class TestStoreBase {

	protected abstract TestContext getContext();
	
	protected SiteStore getSiteStore() {
		return getContext().getSiteStore();
	}

	protected ArticleStore getArticleStore() {
		return getContext().getArticleStore();
	}

	protected ArticleSearchService getArticleSearchService() {
		return getContext().getArticleSearchService();
	}

	protected WorkingSetStore getWorkingSetStore() {
		return getContext().getWorkingSetStore();
	}
	
	@Before
	public void open() {
		getContext().open();
	}
	
	@After
	public void close() {
		getContext().close();
	}
}