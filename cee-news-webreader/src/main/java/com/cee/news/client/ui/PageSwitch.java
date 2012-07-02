package com.cee.news.client.ui;

import com.cee.news.client.NewsView;
import com.cee.news.client.PageSwitchView;
import com.cee.news.client.StartView;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

final class PageSwitch implements PageSwitchView {
    
    private final StartView startView;
    
    private final NewsView newsView;
    
    public PageSwitch(StartView startView, NewsView newsView) {
        super();
        this.startView = startView;
        this.newsView = newsView;
    }

    @Override
    public Widget asWidget() {
        return RootPanel.get();
    }

    @Override
    public void showStartPage() {
        newsView.removeScrollHandler();
        RootPanel.get().remove(newsView);
        RootPanel.get().add(startView);
        startView.registerScrollHandler();
    }

    @Override
    public void showNewsPage() {
        startView.removeScrollHandler();
        RootPanel.get().remove(startView);
        RootPanel.get().add(newsView);
        newsView.registerScrollHandler();
    }
}