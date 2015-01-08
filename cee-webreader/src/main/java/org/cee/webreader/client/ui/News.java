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


import java.util.List;

import org.cee.client.EntityContent;
import org.cee.store.article.ArticleKey;
import org.cee.webreader.client.NewsView;
import org.cee.webreader.client.content.EntityContentCell;
import org.cee.webreader.client.content.EntityContentKeyProvider;
import org.cee.webreader.client.list.IncreaseVisibleRangeScrollHandler;
import org.cee.webreader.client.list.KeyLink;
import org.cee.webreader.client.paging.PagingView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class News extends Composite implements NewsView, PagingView {

    private static NewsUiBinder uiBinder = GWT.create(NewsUiBinder.class);

    interface NewsUiBinder extends UiBinder<Widget, News> {
    }
    
    interface RelatedCellListStyle extends CellList.Style {
        
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
    
    interface RelatedCellListResources extends CellList.Resources {
        
        public static final RelatedCellListResources INSTANCE = GWT.create(RelatedCellListResources.class);
        
        @Override
        @Source("RelatedCellList.css")
        public RelatedCellListStyle cellListStyle();
    }
    
    private HandlerRegistration scrollRegistration;
    
    @UiField
    Label labelStart;
    
    @UiField
    ListBox selectArticle;
    
    @UiField
    Button buttonNavLeft;
    
    @UiField
    Button buttonNavRight;
    
    @UiField
    Button buttonRefresh;
    
    @UiField(provided = true)
    CellList<EntityContent<ArticleKey>> cellListRelated = new CellList<EntityContent<ArticleKey>>(
            new EntityContentCell<ArticleKey>(), 
            RelatedCellListResources.INSTANCE, 
            new EntityContentKeyProvider<ArticleKey>());

    @UiField
    HTML mainContent;

    private final ButtonTitle nextContent;
    
    private final ButtonTitle previousContent;
    
    public News() {
        initWidget(uiBinder.createAndBindUi(this));
        nextContent = new ButtonTitle(buttonNavRight);
        previousContent = new ButtonTitle(buttonNavLeft);
    }

    //TODO: Remove this from API! Source (site) name is part of article content and needs no additional label
    @Override
    public HasText getSiteNameLabel() {
        return new HasText() {
            
            @Override
            public void setText(String text) {}
            
            @Override
            public String getText() { return null; }
        };
    }

    @Override
    public HasClickHandlers getButtonGoToStart() {
        return labelStart;
    }

    @Override
    public PagingView getNewsPagingView() {
        return this;
    }

    @Override
    public CellList<EntityContent<ArticleKey>> getWhatOthersSayCellList() {
        return cellListRelated;
    }

    @Override
    public int getNumberOfVisibleRelatedArticles() {
        final Styles styles = Resources.INSTANCE.styles();
        return (((Window.getClientHeight() - styles.articleTeaserTop()) / styles.articleTeaserHeight()) + 1) * styles.relatedArticleColumns();
    }

    @Override
    public HasClickHandlers getButtonRefresh() {
        return buttonRefresh;
    }

    @Override
    public void setPreviousEnabled(boolean enabled) {
        buttonNavLeft.setEnabled(enabled);
    }

    @Override
    public HasSafeHtml getPreviousContent() {
        return previousContent;
    }

    @Override
    public void addPreviousClickHandler(ClickHandler handler) {
        buttonNavLeft.addClickHandler(handler);
    }

    @Override
    public void setNextEnabled(boolean enabled) {
        buttonNavRight.setEnabled(enabled);
    }

    @Override
    public HasSafeHtml getNextContent() {
        return nextContent;
    }

    @Override
    public void addNextClickHandler(ClickHandler handler) {
        buttonNavRight.addClickHandler(handler);
    }

    @Override
    public HasSafeHtml getMainContent() {
        return mainContent;
    }

    @Override
    public void resetMainContentScrollPosition() {
        // TODO Do we need to implement this?
    }

    @Override
    public void setJumpToLinks(List<KeyLink> links) {
        selectArticle.clear();
        for (KeyLink link : links) {
            selectArticle.addItem(link.getText(), link.getKeyValue());
        }
    }
    
    @Override
    public void setJumpToSelectedIndex(int index) {
        selectArticle.setSelectedIndex(index);
    }
    
    @Override
    public int getJumpToSelectedIndex() {
        return selectArticle.getSelectedIndex();
    }

    @Override
    public void addJumpToChangedHandler(ChangeHandler handler) {
        selectArticle.addChangeHandler(handler);
    }
    
    @Override
    public void registerScrollHandler() {
    	Window.scrollTo(0, 0);
        final Styles styles = Resources.INSTANCE.styles();
        final WindowVerticalScroll verticalScroll = new WindowVerticalScroll(cellListRelated.getElement(), styles.articleTeaserTop());
        scrollRegistration = Window.addWindowScrollHandler(
                new IncreaseVisibleRangeScrollHandler(
                        cellListRelated,
                        verticalScroll,
                        styles.relatedArticleColumns(), 
                        styles.articleTeaserHeight()));
    }
    
    @Override
    public void removeScrollHandler() {
        if (scrollRegistration != null) {
            scrollRegistration.removeHandler();
            scrollRegistration = null;
        }
    }
}
