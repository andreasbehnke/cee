package com.cee.news.client;

import com.cee.news.client.error.ErrorHandler;

public interface ClientFactory extends StartView, NewsView {

    ErrorHandler getGlobalErrorHandler();
    
    PageSwitchView getPageSwitchView();
}
