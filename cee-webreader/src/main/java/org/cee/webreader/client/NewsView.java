package org.cee.webreader.client;

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

import org.cee.store.article.ArticleKey;
import org.cee.webreader.client.content.EntityContent;
import org.cee.webreader.client.paging.PagingView;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface NewsView extends IsWidget {

	HasText getSiteNameLabel();

	HasClickHandlers getButtonGoToStart();

	PagingView getNewsPagingView();

	CellList<EntityContent<ArticleKey>> getWhatOthersSayCellList();
	
	int getNumberOfVisibleRelatedArticles();

	HasClickHandlers getButtonRefresh();
	
    void registerScrollHandler();
    
    void removeScrollHandler();

}