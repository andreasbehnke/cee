package org.cee.webreader.client.content;

/*
 * #%L
 * News Reader
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


import java.util.List;

import org.cee.news.model.ArticleKey;
import org.cee.news.model.EntityKey;
import org.cee.webreader.client.async.NotificationCallback;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class RelatedArticlesContentModel extends NewsContentModelBase {
    
    private ArticleKey articleKey;
    
    private EntityKey workingSet;
    
    private void performRelatedSearch(final NotificationCallback callback) {
        newsService.getRelatedArticles(articleKey, workingSet, new AsyncCallback<List<ArticleKey>>() {    
            @Override
            public void onSuccess(List<ArticleKey> result) {
                setValues(result);
                if(callback != null) {
                    callback.finished();
                }
            }
            @Override
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load related articles!");//TODO: i18n
            }
        });
    }
    
    public void getRelatedArticles(final ArticleKey articleKey, final EntityKey workingSet) {
        getRelatedArticles(articleKey, workingSet, null);
    }
    
    public void getRelatedArticles(final ArticleKey articleKey, final EntityKey workingSet, final NotificationCallback callback) {
        this.articleKey = articleKey;
        this.workingSet = workingSet;
        performRelatedSearch(callback);
    }
    
    public void refresh() {
        refresh(null);
    }
    
    public void refresh(NotificationCallback callback) {
        performRelatedSearch(callback);
    }
}
