package com.cee.news.client;

import com.cee.news.client.content.EntityKeyCell;
import com.cee.news.client.content.SourceSelectionPanel;
import com.cee.news.client.content.SourceSelectionView;
import com.cee.news.client.list.ScrollLoader;
import com.cee.news.client.progress.ProgressView;
import com.cee.news.client.progress.TextProgressView;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.cee.news.client.workingset.ui.WorkingSetSelection;
import com.cee.news.model.EntityKey;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.cee.news.client.search.SearchPanel;
import com.cee.news.client.search.SearchView;
import com.google.gwt.user.client.ui.Button;

public class StartPanel extends Composite implements StartView {
    
	private final WorkingSetSelection workingSetSelection;
    
    private final TextProgressView progressView;
    
    private final SourceSelectionView sourceSelectionView;
    
    private SearchView searchView;
    
    private final CellList<EntityKey> cellListLatestArticles;

    private Button buttonRefresh;

    public StartPanel() {
    	LayoutPanel layoutPanel = new LayoutPanel();
        initWidget(layoutPanel);
        layoutPanel.setSize("818px", "493px");
        
        workingSetSelection = new WorkingSetSelection();
        layoutPanel.add(workingSetSelection);
        layoutPanel.setWidgetLeftWidth(workingSetSelection, 0.0, Unit.PX, 376.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(workingSetSelection, 0.0, Unit.PX, 30.0, Unit.PX);
        
        sourceSelectionView = new SourceSelectionPanel();
        layoutPanel.add(sourceSelectionView);
        layoutPanel.setWidgetLeftWidth(sourceSelectionView, 0.0, Unit.PX, 360.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(sourceSelectionView, 52.0, Unit.PX, 0.0, Unit.PX);
        
        InlineLabel nlnlblSites = new InlineLabel("Sites:");
        layoutPanel.add(nlnlblSites);
        layoutPanel.setWidgetLeftWidth(nlnlblSites, 0.0, Unit.PX, 90.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblSites, 30.0, Unit.PX, 16.0, Unit.PX);
        
        InlineLabel nlnlblLatestNews = new InlineLabel("Latest News:");
        layoutPanel.add(nlnlblLatestNews);
        layoutPanel.setWidgetLeftWidth(nlnlblLatestNews, 366.0, Unit.PX, 90.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblLatestNews, 30.0, Unit.PX, 16.0, Unit.PX);
        
        progressView = new TextProgressView();
        progressView.setMessageFormat("(update %s)");
        layoutPanel.add(progressView);
        layoutPanel.setWidgetLeftRight(progressView, 447.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(progressView, 30.0, Unit.PX, 16.0, Unit.PX);
        
        cellListLatestArticles = new CellList<EntityKey>(new EntityKeyCell());
        ScrollLoader articlesPager = new ScrollLoader();
        articlesPager.setDisplay(cellListLatestArticles);
        layoutPanel.add(articlesPager);
        layoutPanel.setWidgetLeftRight(articlesPager, 366.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(articlesPager, 52.0, Unit.PX, 0.0, Unit.PX);
        
        buttonRefresh = new Button("Refresh");
        buttonRefresh.setText("Refresh");
        layoutPanel.add(buttonRefresh);
        layoutPanel.setWidgetRightWidth(buttonRefresh, 0.0, Unit.PX, 78.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(buttonRefresh, 6.0, Unit.PX, 24.0, Unit.PX);
        
        searchView = new SearchPanel();
        layoutPanel.add(searchView);
        layoutPanel.setWidgetLeftRight(searchView, 303.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(searchView, 0.0, Unit.PX, 24.0, Unit.PX);
    }

    @Override
    public CellList<EntityKey> getCellListLatestArticles() {
		return cellListLatestArticles;
	}

    @Override
	public WorkingSetSelectionView getWorkingSetSelectionView() {
        return workingSetSelection;
    }
    
    @Override
    public SearchView getSearchView() {
        return searchView;
    }
    
    @Override
    public ProgressView getProgressView() {
    	return progressView;
    }
    
    @Override
    public SourceSelectionView getSourceSelectionView() {
        return sourceSelectionView;
    }
    
    @Override
    public HasClickHandlers getButtonRefresh() {
        return buttonRefresh;
    }
}
