package com.cee.news.client.ui;

import com.cee.news.client.ClientFactory;
import com.cee.news.client.NewsPanel;
import com.cee.news.client.NewsView;
import com.cee.news.client.PageSwitchView;
import com.cee.news.client.StartView;
import com.cee.news.client.error.ErrorDialog;
import com.cee.news.client.error.ErrorHandler;
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
        
        startView = new Start();
        newsView = new NewsPanel();
        pageSwitchView = new PageSwitchView() {
            
            @Override
            public Widget asWidget() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public void showStartPage() {
                RootPanel.get().remove(newsView);
                RootPanel.get().add(startView);
            }
            
            @Override
            public void showNewsPage() {
                RootPanel.get().remove(startView);
                RootPanel.get().add(newsView);
            }
        };
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
    public PageSwitchView getPageSwitchView() {
        return pageSwitchView;
    }
}
