package com.cee.news.client;

import com.cee.news.client.list.EntityContent;
import com.cee.news.client.list.EntityContentCell;
import com.cee.news.client.list.EntityContentKeyProvider;
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
        layoutPanel.add(cellListSites);
        layoutPanel.setWidgetLeftWidth(cellListSites, 0.0, Unit.PX, 360.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(cellListSites, 52.0, Unit.PX, 0.0, Unit.PX);
        
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
        layoutPanel.add(cellListLatestArticles);
        layoutPanel.setWidgetLeftRight(cellListLatestArticles, 366.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(cellListLatestArticles, 52.0, Unit.PX, 0.0, Unit.PX);
    }
    /* (non-Javadoc)
	 * @see com.cee.news.client.StartView#getLatestArticlesListView()
	 */
    @Override
    public CellList<EntityContent> getCellListLatestArticles() {
		return cellListLatestArticles;
	}
    /* (non-Javadoc)
	 * @see com.cee.news.client.StartView#getWorkingSetSelectionView()
	 */
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
    	// TODO Auto-generated method stub
    	return null;
    }
}
