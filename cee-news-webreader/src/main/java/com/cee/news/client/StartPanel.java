package com.cee.news.client;

import com.cee.news.client.list.ListPanel;
import com.cee.news.client.workingset.WorkingSetSelectionPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;

public class StartPanel extends Composite {
    
	private final ListPanel listPanelLatestArticles;
    private final WorkingSetSelectionPanel workingSetSelectionPanel;
    private final ListPanel listPanelSites;
    
    public StartPanel() {
    	LayoutPanel layoutPanel = new LayoutPanel();
        initWidget(layoutPanel);
        layoutPanel.setSize("781px", "441px");
        
        workingSetSelectionPanel = new WorkingSetSelectionPanel();
        layoutPanel.add(workingSetSelectionPanel);
        layoutPanel.setWidgetLeftWidth(workingSetSelectionPanel, 0.0, Unit.PX, 626.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(workingSetSelectionPanel, 0.0, Unit.PX, 24.0, Unit.PX);
        
        listPanelSites = new ListPanel();
        layoutPanel.add(listPanelSites);
        layoutPanel.setWidgetLeftWidth(listPanelSites, 0.0, Unit.PX, 360.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(listPanelSites, 52.0, Unit.PX, 0.0, Unit.PX);
        
        InlineLabel nlnlblSites = new InlineLabel("Sites:");
        layoutPanel.add(nlnlblSites);
        layoutPanel.setWidgetLeftWidth(nlnlblSites, 0.0, Unit.PX, 90.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblSites, 30.0, Unit.PX, 16.0, Unit.PX);
        
        listPanelLatestArticles = new ListPanel();
        layoutPanel.add(listPanelLatestArticles);
        layoutPanel.setWidgetLeftRight(listPanelLatestArticles, 366.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(listPanelLatestArticles, 52.0, Unit.PX, 0.0, Unit.PX);
        
        InlineLabel nlnlblLatestNews = new InlineLabel("Latest News:");
        layoutPanel.add(nlnlblLatestNews);
        layoutPanel.setWidgetLeftWidth(nlnlblLatestNews, 366.0, Unit.PX, 90.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblLatestNews, 30.0, Unit.PX, 16.0, Unit.PX);
    }
    public ListPanel getListPanelLatestArticles() {
        return listPanelLatestArticles;
    }
    public WorkingSetSelectionPanel getWorkingSetSelectionPanel() {
        return workingSetSelectionPanel;
    }
    public ListPanel getListPanelSites() {
        return listPanelSites;
    }
}
