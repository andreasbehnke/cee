package com.cee.news.client;

import com.cee.news.client.error.ErrorHandler;

public interface ClientFactory {

    ErrorHandler getGlobalErrorHandler();
    
    PageSwitchView getPageSwitchView();
    
    StartView getStartView();
    
    NewsView getNewsView();
}
