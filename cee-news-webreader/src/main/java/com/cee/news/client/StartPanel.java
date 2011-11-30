package com.cee.news.client;

import com.cee.news.client.list.EntityContent;
import com.cee.news.client.list.EntityContentCell;
import com.cee.news.client.list.EntityContentKeyProvider;
import com.cee.news.client.list.ScrollLoader;
import com.cee.news.client.progress.ProgressView;
import com.cee.news.client.progress.TextProgressView;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.cee.news.client.workingset.ui.WorkingSetSelection;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;

public class StartPanel extends Composite implements StartView {
    
	private final WorkingSetSelection workingSetSelection;
    
    private final TextProgressView progressView;
    
    private final CellList<EntityContent> cellListSites;
    
    private final CellList<EntityContent> cellListLatestArticles;
    
    public StartPanel() {
    	LayoutPanel layoutPanel = new LayoutPanel();
        initWidget(layoutPanel);
        layoutPanel.setSize("781px", "441px");
        
        workingSetSelection = new WorkingSetSelection();
        layoutPanel.add(workingSetSelection);
        layoutPanel.setWidgetLeftWidth(workingSetSelection, 0.0, Unit.PX, 626.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(workingSetSelection, 0.0, Unit.PX, 24.0, Unit.PX);
        
        cellListSites = new CellList<EntityContent>(new EntityContentCell(), new EntityContentKeyProvider());
        ScrollLoader sitesPager = new ScrollLoader();
        sitesPager.setDisplay(cellListSites);
        layoutPanel.add(sitesPager);
        layoutPanel.setWidgetLeftWidth(sitesPager, 0.0, Unit.PX, 360.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(sitesPager, 52.0, Unit.PX, 0.0, Unit.PX);
        
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
        
        cellListLatestArticles = new CellList<EntityContent>(new EntityContentCell());
        ScrollLoader articlesPager = new ScrollLoader();
        articlesPager.setDisplay(cellListLatestArticles);
        layoutPanel.add(articlesPager);
        layoutPanel.setWidgetLeftRight(articlesPager, 366.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(articlesPager, 52.0, Unit.PX, 0.0, Unit.PX);
    }

    @Override
    public CellList<EntityContent> getCellListLatestArticles() {
		return cellListLatestArticles;
	}

    @Override
	public WorkingSetSelectionView getWorkingSetSelectionView() {
        return workingSetSelection;
    }
    
    @Override
    public ProgressView getProgressView() {
    	return progressView;
    }
    
    @Override
    public CellList<EntityContent> getCellListSites() {
    	return cellListSites;
    }
}
