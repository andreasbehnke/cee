package org.cee.webreader.client.ui;

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


import org.cee.client.EntityContent;
import org.cee.store.article.ArticleKey;
import org.cee.webreader.client.StartView;
import org.cee.webreader.client.content.EntityContentCell;
import org.cee.webreader.client.content.EntityContentKeyProvider;
import org.cee.webreader.client.content.SourceSelectionView;
import org.cee.webreader.client.list.IncreaseVisibleRangeScrollHandler;
import org.cee.webreader.client.search.SearchView;
import org.cee.webreader.client.workingset.WorkingSetSelectionView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Start extends Composite implements StartView {
    
    private static StartUiBinder uiBinder = GWT.create(StartUiBinder.class);

    interface StartUiBinder extends UiBinder<Widget, Start> {
    }
    
    interface ArticleCellListStyle extends CellList.Style {
        
        @Override
        public String cellListEvenItem();
        
        @Override
        public String cellListOddItem();
        
        @Override
        public String cellListWidget();
        
        @Override
        public String cellListKeyboardSelectedItem();
        
        @Override
        public String cellListSelectedItem();
    }
    
    interface ArticleCellListResources extends CellList.Resources {
        
        public static final ArticleCellListResources INSTANCE = GWT.create(ArticleCellListResources.class);
        
        @Override
        @Source("ArticleCellList.css")
        public ArticleCellListStyle cellListStyle();
    }
    
    private HandlerRegistration scrollRegistration;
    
    private int scrollPosition = -1;
    
    @UiField
    WorkingSetSelection workingSetSelection;
    
    @UiField
    Search search;
    
    @UiField
    SourceSelection sourceSelection;
    
    @UiField
    Button buttonRefresh;
    
    @UiField(provided=true)
    CellList<EntityContent<ArticleKey>> cellListLatestArticles = new CellList<EntityContent<ArticleKey>>(
            new EntityContentCell<ArticleKey>(), 
            ArticleCellListResources.INSTANCE, 
            new EntityContentKeyProvider<ArticleKey>());
    
    public Start() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public CellList<EntityContent<ArticleKey>> getCellListLatestArticles() {
        return cellListLatestArticles;
    }

    @Override
    public WorkingSetSelectionView getWorkingSetSelectionView() {
        return workingSetSelection;
    }

    @Override
    public SourceSelectionView getSourceSelectionView() {
        return sourceSelection;
    }

    @Override
    public SearchView getSearchView() {
        return search;
    }

    @Override
    public HasClickHandlers getButtonRefresh() {
        return buttonRefresh;
    }
    
    @Override
    public int getNumberOfVisibleArticleTeasers() {
        final Styles styles = Resources.INSTANCE.styles();
        return (((Window.getClientHeight() - styles.articleTeaserTop()) / styles.articleTeaserHeight()) + 1) * styles.articleTeaserColumns();
    }
    
    @Override
    public void registerScrollHandler() {
    	if (scrollPosition > -1) {
        	Window.scrollTo(0, scrollPosition);
        }
        final Styles styles = Resources.INSTANCE.styles();
        final WindowVerticalScroll verticalScroll = new WindowVerticalScroll(cellListLatestArticles.getElement(), styles.articleTeaserTop());
        scrollRegistration = Window.addWindowScrollHandler(
                new IncreaseVisibleRangeScrollHandler(
                        cellListLatestArticles,
                        verticalScroll,
                        styles.articleTeaserColumns(), 
                        styles.articleTeaserHeight()));
    }
    
    @Override
    public void removeScrollHandler() {
        if (scrollRegistration != null) {
            scrollRegistration.removeHandler();
            scrollRegistration = null;
        }
        scrollPosition = Window.getScrollTop();
    }
}