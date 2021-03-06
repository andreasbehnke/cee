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

import org.cee.search.ArticleSearchService;
import org.cee.store.article.ArticleStore;
import org.cee.store.site.SiteStore;
import org.cee.store.workingset.WorkingSetStore;

public interface TestContext {

	public abstract void open();
	
	public abstract void close();
	
	public abstract SiteStore getSiteStore();

	public abstract ArticleStore getArticleStore();

	public abstract ArticleSearchService getArticleSearchService();

	public abstract WorkingSetStore getWorkingSetStore();

}