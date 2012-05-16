package com.cee.news.client.ui;

import com.cee.news.client.ClientFactory;
import com.cee.news.client.NewsPanel;
import com.cee.news.client.NewsView;
import com.cee.news.client.PageSwitchPanel;
import com.cee.news.client.PageSwitchView;
import com.cee.news.client.StartView;
import com.cee.news.client.error.ErrorDialog;
import com.cee.news.client.error.ErrorHandler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class BinderClientFactory implements ClientFactory {

    private final ErrorHandler globalErrorHandler;
    
    private final PageSwitchView pageSwitchView;
    
    private final StartView startView;
    
    private final NewsView newsView;
    
    public BinderClientFactory() {
        Resources.INSTANCE.styles().ensureInjected();
        
        globalErrorHandler = new ErrorDialog();
        
        LayoutPanel layoutPanel = new LayoutPanel();
        layoutPanel.setSize("100%", "100%");
        
        startView = new Start();
        //TODO migrate news panel to UI binder
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
    public StartView getStartView() {
        return startView;
    }

    @Override
    public NewsView getNewsView() {
        return newsView;
    }
    
    @Override
    public PageSwitchView getPageSwitchView() {
        return pageSwitchView;
    }
}
