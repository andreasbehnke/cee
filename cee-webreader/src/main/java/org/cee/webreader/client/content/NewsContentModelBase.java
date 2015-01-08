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


import java.util.ArrayList;
import java.util.List;

import org.cee.client.EntityContent;
import org.cee.store.article.ArticleKey;
import org.cee.webreader.client.list.CellListContentModel;
import org.cee.webreader.client.list.ContentModel;
import org.cee.webreader.client.list.DefaultListModel;
import org.cee.webreader.client.util.SafeHtmlUtil;

import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class NewsContentModelBase extends DefaultListModel<ArticleKey> implements ContentModel<ArticleKey>, CellListContentModel<ArticleKey> {

    protected final GwtNewsServiceAsync newsService = GwtNewsServiceAsync.Util.getInstance();

    @Override
    public void getContentTitle(final HasSafeHtml target, ArticleKey key) {
        if (keys == null) {
            throw new IllegalStateException("Headlines have not been set yet!");
        }
        target.setHTML(SafeHtmlUtil.sanitize(key.getName()));
    }

    @Override
    public void getContentDescription(final HasSafeHtml target, ArticleKey key) {
    	newsService.getHtmlDescription(key, new AsyncCallback<EntityContent<ArticleKey>>() {
            public void onSuccess(EntityContent<ArticleKey> result) {
                target.setHTML(SafeHtmlUtil.sanitize(result.getContent()) );
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load description!");
            }
        });
    }

    @Override
    public void getContent(final HasSafeHtml target, ArticleKey key) {
        newsService.getHtmlContent(key, new AsyncCallback<EntityContent<ArticleKey>>() {
            public void onSuccess(EntityContent<ArticleKey> result) {
            	target.setHTML(SafeHtmlUtil.sanitize(result.getContent()) );
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load content!");
            }
        });
    }
    
    @Override
    public void getContent(ArrayList<ArticleKey> keys, AsyncCallback<List<EntityContent<ArticleKey>>> callback) {
        newsService.getHtmlDescriptions(keys, callback);
    }
}
