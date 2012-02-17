package com.cee.news.client.search;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;

public class SearchPanel extends Composite implements SearchView {
    private TextBox textBoxSearch;
    private Button buttonSearch;
    private Button buttonClear;

    public SearchPanel() {
        
        LayoutPanel layoutPanel = new LayoutPanel();
        initWidget(layoutPanel);
        layoutPanel.setSize("506px", "30px");
        
        textBoxSearch = new TextBox();
        layoutPanel.add(textBoxSearch);
        layoutPanel.setWidgetLeftRight(textBoxSearch, 0.0, Unit.PX, 168.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(textBoxSearch, 0.0, Unit.PX, 28.0, Unit.PX);
        
        buttonSearch = new Button("Search");
        layoutPanel.add(buttonSearch);
        layoutPanel.setWidgetLeftWidth(buttonSearch, 344.0, Unit.PX, 78.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(buttonSearch, 0.0, Unit.PX, 24.0, Unit.PX);
        
        buttonClear = new Button("Clear");
        layoutPanel.add(buttonClear);
        layoutPanel.setWidgetLeftWidth(buttonClear, 428.0, Unit.PX, 78.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(buttonClear, 0.0, Unit.PX, 24.0, Unit.PX);
    }

    @Override
    public HasClickHandlers getSearchButton() {
        return buttonSearch;
    }

    @Override
    public HasClickHandlers getClearButton() {
        return buttonClear;
    }

    @Override
    public HasText getSearchText() {
        return textBoxSearch;
    }

    @Override
    public void setSearchButtonEnabled(boolean enabled) {
        buttonSearch.setEnabled(enabled);
    }

    @Override
    public void setClearButtonEnabled(boolean enabled) {
        buttonClear.setEnabled(enabled);
    }
}
