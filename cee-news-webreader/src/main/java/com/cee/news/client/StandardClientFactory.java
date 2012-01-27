package com.cee.news.client;

import com.cee.news.client.error.ErrorDialog;
import com.cee.news.client.error.ErrorHandler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class StandardClientFactory extends DefaultClientFactory {

    private final ErrorHandler globalErrorHandler;
    
    private final PageSwitchView pageSwitchView;
    
    private final StartView startView;
    
    private final NewsView newsView;
    
    public StandardClientFactory() {
        
        globalErrorHandler = new ErrorDialog();
        
        LayoutPanel layoutPanel = new LayoutPanel();
        layoutPanel.setSize("100%", "100%");
        
        startView = new StartPanel();
        newsView = new NewsPanel();
        pageSwitchView = new PageSwitchPanel(startView, newsView);
        
        layoutPanel.add(pageSwitchView);
        Widget pageSwitchWidget = pageSwitchView.asWidget();
        pageSwitchWidget.setSize("100%", "100%");
        layoutPanel.setWidgetLeftRight(pageSwitchWidget, 0.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(pageSwitchWidget, 0.0, Unit.PX, 0.0, Unit.PX);
        
        RootPanel.get().add(layoutPanel, 0, 0);
    }

    @Override
    public ErrorHandler getGlobalErrorHandler() {
        return globalErrorHandler;
    }

    @Override
    protected StartView createStartView() {
        return startView;
    }

    @Override
    protected NewsView createNewsView() {
        return newsView;
    }
    
    @Override
    public PageSwitchView getPageSwitchView() {
        return pageSwitchView;
    }
}
