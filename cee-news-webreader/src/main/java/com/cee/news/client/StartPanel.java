package com.cee.news.client;

import com.cee.news.client.content.EntityContent;
import com.cee.news.client.content.EntityContentCell;
import com.cee.news.client.content.SourceSelectionPanel;
import com.cee.news.client.content.SourceSelectionView;
import com.cee.news.client.list.IncreaseVisibleRangeScrollHandler;
import com.cee.news.client.search.SearchPanel;
import com.cee.news.client.search.SearchView;
import com.cee.news.client.ui.WorkingSetSelection;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.cee.news.model.ArticleKey;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class StartPanel extends Composite implements StartView {
    
	private final WorkingSetSelection workingSetSelection;
    
    private final SourceSelectionView sourceSelectionView;
    
    private final SearchView searchView;

    private final CellList<EntityContent<ArticleKey>> cellListLatestArticles;

    private final Button buttonRefresh;

    public StartPanel() {
    	LayoutPanel layoutPanel = new LayoutPanel();
        initWidget(layoutPanel);
        layoutPanel.setSize("971px", "493px");
        
        workingSetSelection = new WorkingSetSelection();
        layoutPanel.add(workingSetSelection);
        layoutPanel.setWidgetLeftWidth(workingSetSelection, 0.0, Unit.PX, 365.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(workingSetSelection, 0.0, Unit.PX, 30.0, Unit.PX);
        
        sourceSelectionView = new SourceSelectionPanel();
        layoutPanel.add(sourceSelectionView);
        layoutPanel.setWidgetLeftWidth(sourceSelectionView, 0.0, Unit.PX, 353.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(sourceSelectionView, 52.0, Unit.PX, 0.0, Unit.PX);
    
    	InlineLabel nlnlblSites = new InlineLabel("Sites:");
        layoutPanel.add(nlnlblSites);
        layoutPanel.setWidgetLeftWidth(nlnlblSites, 0.0, Unit.PX, 90.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblSites, 30.0, Unit.PX, 16.0, Unit.PX);
        
        InlineLabel nlnlblLatestNews = new InlineLabel("Latest News:");
        layoutPanel.add(nlnlblLatestNews);
        layoutPanel.setWidgetLeftWidth(nlnlblLatestNews, 366.0, Unit.PX, 90.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblLatestNews, 30.0, Unit.PX, 16.0, Unit.PX);
        
        cellListLatestArticles = new CellList<EntityContent<ArticleKey>>(new EntityContentCell<ArticleKey>());
        ScrollPanel articlesPager = new ScrollPanel();
        articlesPager.setWidget(cellListLatestArticles);
        articlesPager.addScrollHandler(new IncreaseVisibleRangeScrollHandler(cellListLatestArticles, articlesPager));
        layoutPanel.add(articlesPager);
        layoutPanel.setWidgetLeftRight(articlesPager, 366.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(articlesPager, 52.0, Unit.PX, 0.0, Unit.PX);
        
        buttonRefresh = new Button("Refresh");
        buttonRefresh.setText("Refresh");
        layoutPanel.add(buttonRefresh);
        layoutPanel.setWidgetRightWidth(buttonRefresh, 0.0, Unit.PX, 78.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(buttonRefresh, 0.0, Unit.PX, 24.0, Unit.PX);
        
        searchView = new SearchPanel();
        layoutPanel.add(searchView);
        layoutPanel.setWidgetLeftRight(searchView, 371.0, Unit.PX, 84.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(searchView, 0.0, Unit.PX, 30.0, Unit.PX);
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
    public SearchView getSearchView() {
        return searchView;
    }
    
    @Override
    public SourceSelectionView getSourceSelectionView() {
        return sourceSelectionView;
    }
    
    @Override
    public HasClickHandlers getButtonRefresh() {
        return buttonRefresh;
    }
    
    @Override
    public int getNumberOfVisibleArticleTeasers() {
        return 10;
    }

    @Override
    public void registerScrollHandler() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeScrollHandler() {
        // TODO Auto-generated method stub
        
    }
}
