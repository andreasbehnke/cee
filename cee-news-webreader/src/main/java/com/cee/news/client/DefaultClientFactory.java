package com.cee.news.client;

import com.cee.news.client.content.SourceSelectionView;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.paging.PagingView;
import com.cee.news.client.progress.ProgressView;
import com.cee.news.client.search.SearchView;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.cee.news.model.EntityKey;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public abstract class DefaultClientFactory implements ClientFactory {

    private StartView startView;
    
    private NewsView newsView;
    
    @Override
    public CellList<EntityKey> getCellListLatestArticles() {
        return getStartView().getCellListLatestArticles();
    }

    @Override
    public WorkingSetSelectionView getWorkingSetSelectionView() {
        return getStartView().getWorkingSetSelectionView();
    }
    
    @Override
    public SearchView getSearchView() {
        return getStartView().getSearchView();
    }

    @Override
    public SourceSelectionView getSourceSelectionView() {
        return getStartView().getSourceSelectionView();
    }

    @Override
    public ProgressView getProgressView() {
        return getStartView().getProgressView();
    }

    @Override
    public HasText getSiteNameLabel() {
        return getNewsView().getSiteNameLabel();
    }

    @Override
    public HasClickHandlers getButtonGoToStart() {
        return getNewsView().getButtonGoToStart();
    }

    @Override
    public PagingView getNewsPagingView() {
        return getNewsView().getNewsPagingView();
    }

    @Override
    public CellList<EntityKey> getWhatOthersSayCellList() {
        return getNewsView().getWhatOthersSayCellList();
    }

    @Override
    public abstract ErrorHandler getGlobalErrorHandler();
    
    protected abstract StartView createStartView();

    private StartView getStartView() {
        if (startView == null) {
            startView = createStartView();
        }
        return startView;
    }
    
    protected abstract NewsView createNewsView();

    private NewsView getNewsView() {
        if (newsView == null) {
            newsView = createNewsView();
        }
        return newsView;
    }

    @Override
    public abstract PageSwitchView getPageSwitchView();
    
    @Override
    public Widget asWidget() {
        return getPageSwitchView().asWidget();
    }
}
