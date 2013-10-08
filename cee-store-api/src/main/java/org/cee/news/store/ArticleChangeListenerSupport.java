package org.cee.news.store;

/*
 * #%L
 * Content Extraction Engine - News Store API
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

import org.cee.news.model.Article;
import org.cee.news.model.EntityKey;

public class ArticleChangeListenerSupport {

	private List<ArticleChangeListener> changeListeners;
    
	public void fireArticleChanged(EntityKey site, Article article) {
    	if (changeListeners == null) return;
    	for (ArticleChangeListener changeListener : changeListeners) {
			changeListener.onArticleChanged(site, article);
		}
    }
    
    public void addArticleChangeListener(ArticleChangeListener listener) {
    	if (changeListeners == null) {
    		changeListeners = new ArrayList<ArticleChangeListener>();
    	}
    	changeListeners.add(listener);
    }
}
