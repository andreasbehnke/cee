package org.cee.webreader.client.search;

import org.cee.webreader.client.content.NewsListContentModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class SearchPresenter {

    public SearchPresenter(final NewsListContentModel contentModel, final SearchView searchView) {
        searchView.setClearButtonEnabled(false);
        searchView.getSearchButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                searchView.setClearButtonEnabled(true);
                String searchQuery = searchView.getSearchText().getText();
                contentModel.findArticles(searchQuery);
            }
        });
        searchView.getClearButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                searchView.getSearchText().setText("");
                searchView.setClearButtonEnabled(false);
                contentModel.clearSearch();
            }
        });
    }
}
