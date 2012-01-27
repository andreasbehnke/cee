package com.cee.news.client;

import com.google.gwt.user.client.ui.DeckPanel;

public class PageSwitchPanel extends DeckPanel implements PageSwitchView {
    
    private static final int START_PANEL_INDEX = 0;
    
    private static final int NEWS_PANEL_INDEX = 1;

    public PageSwitchPanel(StartView startView, NewsView newsView) {
        add(startView);
        startView.asWidget().setSize("100%", "100%");
        add(newsView);
        newsView.asWidget().setSize("100%", "100%");
        showWidget(START_PANEL_INDEX);
    }
    
    @Override
    public void showStartPage() {
        showWidget(START_PANEL_INDEX);
    }

    @Override
    public void showNewsPage() {
        showWidget(NEWS_PANEL_INDEX);
    }
}
