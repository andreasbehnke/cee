package com.cee.news.client.ui;

import com.cee.news.client.NewsView;
import com.cee.news.client.content.EntityContent;
import com.cee.news.client.paging.PagingView;
import com.cee.news.model.ArticleKey;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class News extends Composite implements NewsView {

    private static NewsUiBinder uiBinder = GWT.create(NewsUiBinder.class);

    interface NewsUiBinder extends UiBinder<Widget, News> {
    }

    public News() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public HasText getSiteNameLabel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasClickHandlers getButtonGoToStart() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PagingView getNewsPagingView() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CellList<EntityContent<ArticleKey>> getWhatOthersSayCellList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasClickHandlers getButtonRefresh() {
        // TODO Auto-generated method stub
        return null;
    }

}
