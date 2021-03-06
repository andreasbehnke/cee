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

import java.util.ArrayList;
import java.util.List;

import org.cee.store.EntityKey;
import org.cee.store.article.Article;
import org.cee.store.article.ArticleChangeListener;

public class DummyArticleChangeListener implements ArticleChangeListener {
	
	public List<String> changedSiteNames = new ArrayList<String>();

	public List<String> changedArticleIds = new ArrayList<String>();
	
	@Override
	public void onArticleChanged(EntityKey site, Article article) {
		changedSiteNames.add(site.getName());
		changedArticleIds.add(article.getExternalId());			
	}
	
	public void reset() {
		changedSiteNames = new ArrayList<String>();
		changedArticleIds = new ArrayList<String>();
    }
}