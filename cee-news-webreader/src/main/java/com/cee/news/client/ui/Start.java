package com.cee.news.client.ui;

import com.cee.news.client.StartView;
import com.cee.news.client.content.EntityContent;
import com.cee.news.client.content.EntityContentCell;
import com.cee.news.client.content.EntityKeyProvider;
import com.cee.news.client.content.SourceSelectionView;
import com.cee.news.client.search.SearchView;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.cee.news.model.ArticleKey;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class Start extends Composite implements StartView {
    
    //TODO: apply these styles to cells of scroll panel
    //<div class="clickable teaser column3"><p class="meta">01.02.2011 09:53 - spiegel</p><h1>1 Dieses ist ein sehr spannender Artikel</h1><p>This are the first sentences of the article...</p></div>

    private static StartUiBinder uiBinder = GWT.create(StartUiBinder.class);

    interface StartUiBinder extends UiBinder<Widget, Start> {
    }
    
    @UiField
    WorkingSetSelection workingSetSelection;
    
    @UiField
    Search search;
    
    @UiField
    SourceSelection sourceSelection;
    
    @UiField
    Button buttonRefresh;

    @UiField
    SimplePanel panelArticles;
    
    private CellList<EntityContent<ArticleKey>> cellListLatestArticles;
    
    public Start() {
        initWidget(uiBinder.createAndBindUi(this));
        EntityContentCell<ArticleKey> cell = new EntityContentCell<ArticleKey>();
        cellListLatestArticles = new CellList<EntityContent<ArticleKey>>(cell, new EntityKeyProvider<ArticleKey>());
        panelArticles.setWidget(cellListLatestArticles);
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
}
