package com.cee.news.client.ui;

import java.util.List;

import com.cee.news.client.content.FeedData;
import com.cee.news.client.content.NewSiteWizardView;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class NewSiteWizard extends PopupPanel implements NewSiteWizardView {
    
    public interface NewSiteWizardUiBinder extends UiBinder<Widget, NewSiteWizard> {}
    
    private static NewSiteWizardUiBinder uiBinder = GWT.create(NewSiteWizardUiBinder.class);
    
    private static Resources resources = GWT.create(Resources.class);
    
    @UiField
    HTMLPanel pageLocationInput;
    
    @UiField
    HTMLPanel pageFeedSelection;
    
    @UiField
    TextBox textBoxSiteUrl;
    
    @UiField
    TextBox textBoxSiteName;

    @UiField(provided=true)
    CellTable<FeedData> cellTableFeeds;
    
    @UiField
    InlineLabel labelErrorMessage;
    
    @UiField
    Button buttonLocationInput;
    
    @UiField
    Button buttonStoreSite;
    
    @UiField
    Button buttonCancel;
    
    public NewSiteWizard() {
        cellTableFeeds = new CellTable<FeedData>();
        
        Column<FeedData, Boolean> columnActive = new Column<FeedData, Boolean>(new CheckboxCell()) {
            @Override
            public Boolean getValue(FeedData feed) {
                return feed.getIsActive();
            }
        };
        columnActive.setFieldUpdater(new FieldUpdater<FeedData, Boolean>() {
            
            @Override
            public void update(int index, FeedData feed, Boolean active) {
                feed.setIsActive(active);
                cellTableFeeds.redraw();
            }
        });
        cellTableFeeds.addColumn(columnActive, "Active");
        
        TextColumn<FeedData> columnTitle = new TextColumn<FeedData>() {
            @Override
            public String getValue(FeedData feed) {
                return feed.getTitle();
            }
        };
        cellTableFeeds.addColumn(columnTitle, "Title");
    
        setWidget(uiBinder.createAndBindUi(this));
        setGlassEnabled(true);
        setStyleName(resources.styles().popupPanel());
    }
    
    @Override
    public void showPageLocationInput() {
        pageLocationInput.setVisible(true);
        pageFeedSelection.setVisible(false);
        buttonLocationInput.setVisible(true);
        buttonStoreSite.setVisible(false);
    }

    @Override
    public void showPageFeedSelection() {
        pageLocationInput.setVisible(false);
        pageFeedSelection.setVisible(true);
        buttonLocationInput.setVisible(false);
        buttonStoreSite.setVisible(true);
    }

    @Override
    public HasClickHandlers getButtonLocationInput() {
        return buttonLocationInput;
    }

    @Override
    public HasClickHandlers getButtonStoreSite() {
        return buttonStoreSite;
    }

    @Override
    public HasClickHandlers getButtonCancel() {
        return buttonCancel;
    }

    @Override
    public HasValue<String> getLocationInput() {
        return textBoxSiteUrl;
    }

    @Override
    public HasValue<String> getSiteNameInput() {
        return textBoxSiteName;
    }

    @Override
    public HasText getErrorText() {
        return labelErrorMessage;
    }

    @Override
    public void setFeeds(List<FeedData> feeds) {
        cellTableFeeds.setRowData(feeds);
    }

    @Override
    public void setButtonsEnabled(boolean enabled) {
        buttonCancel.setEnabled(enabled);
        buttonStoreSite.setEnabled(enabled);
        buttonLocationInput.setEnabled(enabled);
    }

}
