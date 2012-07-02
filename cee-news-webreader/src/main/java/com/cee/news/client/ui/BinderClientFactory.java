package com.cee.news.client.ui;

import com.cee.news.client.ClientFactory;
import com.cee.news.client.NewsView;
import com.cee.news.client.StartView;
import com.cee.news.client.error.ErrorDialog;
import com.cee.news.client.error.ErrorHandler;

public class BinderClientFactory implements ClientFactory {

    private final ErrorHandler globalErrorHandler;
    
    private final PageSwitch pageSwitchView;
    
    private final StartView startView;
    
    private final NewsView newsView;
    
    public BinderClientFactory() {
        Resources.INSTANCE.styles().ensureInjected();
        
        globalErrorHandler = new ErrorDialog();
        
        startView = new Start();
        newsView = new News();
        pageSwitchView = new PageSwitch(startView, newsView);
        pageSwitchView.showStartPage();
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
    public PageSwitch getPageSwitchView() {
        return pageSwitchView;
    }
}
