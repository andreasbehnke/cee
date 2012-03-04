package com.cee.news.client;

import com.cee.news.client.error.ErrorHandler;

public abstract class DefaultClientFactory implements ClientFactory {

    private StartView startView;
    
    private NewsView newsView;
    
    @Override
    public abstract ErrorHandler getGlobalErrorHandler();
    
    protected abstract StartView createStartView();

    @Override
    public StartView getStartView() {
        if (startView == null) {
            startView = createStartView();
        }
        return startView;
    }
    
    protected abstract NewsView createNewsView();

    @Override
    public NewsView getNewsView() {
        if (newsView == null) {
            newsView = createNewsView();
        }
        return newsView;
    }

    @Override
    public abstract PageSwitchView getPageSwitchView();
}
