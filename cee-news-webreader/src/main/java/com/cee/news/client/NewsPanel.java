package com.cee.news.client;

import com.cee.news.client.list.ListPanel;
import com.cee.news.client.paging.PagingPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;

public class NewsPanel extends Composite {

    public NewsPanel() {
        
        LayoutPanel layoutPanel = new LayoutPanel();
        initWidget(layoutPanel);
        layoutPanel.setSize("963px", "470px");
        
        Button btnGoToStart = new Button("Go to Start");
        layoutPanel.add(btnGoToStart);
        layoutPanel.setWidgetLeftWidth(btnGoToStart, 0.0, Unit.PX, 124.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(btnGoToStart, 0.0, Unit.PX, 24.0, Unit.PX);
        
        PagingPanel pagingPanel = new PagingPanel();
        layoutPanel.add(pagingPanel);
        layoutPanel.setWidgetLeftRight(pagingPanel, 0.0, Unit.PX, 333.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(pagingPanel, 52.0, Unit.PX, 449.0, Unit.PX);
        
        ListPanel listPanel = new ListPanel();
        layoutPanel.add(listPanel);
        layoutPanel.setWidgetRightWidth(listPanel, 0.0, Unit.PX, 327.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(listPanel, 52.0, Unit.PX, 449.0, Unit.PX);
        
        InlineLabel nlnlblCurrentSite = new InlineLabel("Current Site:");
        layoutPanel.add(nlnlblCurrentSite);
        layoutPanel.setWidgetLeftWidth(nlnlblCurrentSite, 0.0, Unit.PX, 90.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblCurrentSite, 30.0, Unit.PX, 16.0, Unit.PX);
        
        InlineLabel nlnlblNewInlinelabel = new InlineLabel("What other say:");
        layoutPanel.add(nlnlblNewInlinelabel);
        layoutPanel.setWidgetRightWidth(nlnlblNewInlinelabel, 178.0, Unit.PX, 149.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblNewInlinelabel, 30.0, Unit.PX, 16.0, Unit.PX);
    }
}
