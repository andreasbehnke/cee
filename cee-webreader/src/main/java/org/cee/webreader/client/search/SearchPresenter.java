package org.cee.webreader.client.search;

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


import org.cee.webreader.client.content.NewsListContentModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class SearchPresenter {

    public SearchPresenter(final NewsListContentModel contentModel, final SearchView searchView) {
        searchView.setClearButtonEnabled(false);
        searchView.getSearchButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                searchView.setClearButtonEnabled(true);
                String searchQuery = searchView.getSearchText().getText();
                contentModel.findArticles(searchQuery);
            }
        });
        searchView.getClearButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                searchView.getSearchText().setText("");
                searchView.setClearButtonEnabled(false);
                contentModel.clearSearch();
            }
        });
    }
}
