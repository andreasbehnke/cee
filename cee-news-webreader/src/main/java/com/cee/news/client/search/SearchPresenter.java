package com.cee.news.client.search;

import java.util.List;

import com.cee.news.client.content.EntityKeyUtil;
import com.cee.news.client.content.NewsListContentModel;
import com.cee.news.client.list.MultiSelectListModel;
import com.cee.news.model.EntityKey;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class SearchPresenter {

    public SearchPresenter(final NewsListContentModel contentModel, final MultiSelectListModel<EntityKey> siteSelectionModel, final SearchView searchView) {
        searchView.setClearButtonEnabled(false);
        searchView.getSearchButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                searchView.setClearButtonEnabled(true);
                String searchQuery = searchView.getSearchText().getText();
                List<String> keys = EntityKeyUtil.extractKeys(siteSelectionModel.getSelections());
                contentModel.findArticles(keys, searchQuery);
            }
        });
        searchView.getClearButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                searchView.getSearchText().setText("");
                searchView.setClearButtonEnabled(false);
                List<String> keys = EntityKeyUtil.extractKeys(siteSelectionModel.getSelections());
                contentModel.getNewsOfSites(keys);
            }
        });
    }
}
