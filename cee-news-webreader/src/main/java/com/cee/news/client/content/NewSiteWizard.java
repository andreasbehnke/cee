package com.cee.news.client.content;

import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TextBox;

public class NewSiteWizard extends DialogBox implements NewSiteWizardView {

	private CellTable<FeedData> cellTableFeeds;
	private DeckPanel deckPanel;
	private TextBox textBoxSiteUrl;
	private TextBox textBoxSiteName;
	private Button buttonCancel;
	private Button buttonLocationInput;
	private Button buttonStoreSite;
	private InlineLabel labelErrorMessage;

	public NewSiteWizard() {
		setHTML("Add New Site");
		
		LayoutPanel layoutPanel = new LayoutPanel();
		setWidget(layoutPanel);
		layoutPanel.setSize("653px", "341px");
		
		deckPanel = new DeckPanel();
		layoutPanel.add(deckPanel);
		layoutPanel.setWidgetLeftRight(deckPanel, 0.0, Unit.PX, 0.0, Unit.PX);
		layoutPanel.setWidgetTopBottom(deckPanel, 0.0, Unit.PX, 79.0, Unit.PX);
		
		LayoutPanel panelLocationInput = new LayoutPanel();
		deckPanel.add(panelLocationInput);
		panelLocationInput.setSize("100%", "100%");
		
		InlineLabel nlnlblNewInlinelabel = new InlineLabel("Site URL:");
		panelLocationInput.add(nlnlblNewInlinelabel);
		panelLocationInput.setWidgetLeftWidth(nlnlblNewInlinelabel, 12.0, Unit.PX, 67.0, Unit.PX);
		panelLocationInput.setWidgetTopHeight(nlnlblNewInlinelabel, 100.0, Unit.PX, 28.0, Unit.PX);
		
		textBoxSiteUrl = new TextBox();
		panelLocationInput.add(textBoxSiteUrl);
		panelLocationInput.setWidgetLeftRight(textBoxSiteUrl, 78.0, Unit.PX, 12.0, Unit.PX);
		panelLocationInput.setWidgetTopHeight(textBoxSiteUrl, 100.0, Unit.PX, 28.0, Unit.PX);
		
		InlineLabel nlnlblEnterTheUrl = new InlineLabel("Enter the URL of the Site from which content should be extracted");
		panelLocationInput.add(nlnlblEnterTheUrl);
		panelLocationInput.setWidgetLeftRight(nlnlblEnterTheUrl, 12.0, Unit.PX, 12.0, Unit.PX);
		panelLocationInput.setWidgetTopHeight(nlnlblEnterTheUrl, 15.0, Unit.PX, 73.0, Unit.PX);
		
		LayoutPanel panelSelectFeeds = new LayoutPanel();
		deckPanel.add(panelSelectFeeds);
		panelSelectFeeds.setSize("100%", "100%");
		
		cellTableFeeds = new CellTable<FeedData>();
		panelSelectFeeds.add(cellTableFeeds);
		panelSelectFeeds.setWidgetLeftRight(cellTableFeeds, 12.0, Unit.PX, 12.0, Unit.PX);
		panelSelectFeeds.setWidgetTopHeight(cellTableFeeds, 42.0, Unit.PX, 144.0, Unit.PX);
		
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
		
		InlineLabel nlnlblSelectTheFeeds = new InlineLabel("Select the feeds which should be read for this site:");
		panelSelectFeeds.add(nlnlblSelectTheFeeds);
		panelSelectFeeds.setWidgetLeftRight(nlnlblSelectTheFeeds, 12.0, Unit.PX, 12.0, Unit.PX);
		panelSelectFeeds.setWidgetTopHeight(nlnlblSelectTheFeeds, 20.0, Unit.PX, 16.0, Unit.PX);
		
		InlineLabel nlnlblSiteName = new InlineLabel("Site Name:");
		panelSelectFeeds.add(nlnlblSiteName);
		panelSelectFeeds.setWidgetLeftWidth(nlnlblSiteName, 12.0, Unit.PX, 90.0, Unit.PX);
		panelSelectFeeds.setWidgetBottomHeight(nlnlblSiteName, 41.0, Unit.PX, 16.0, Unit.PX);
		
		textBoxSiteName = new TextBox();
		panelSelectFeeds.add(textBoxSiteName);
		panelSelectFeeds.setWidgetLeftRight(textBoxSiteName, 12.0, Unit.PX, 12.0, Unit.PX);
		panelSelectFeeds.setWidgetBottomHeight(textBoxSiteName, 9.0, Unit.PX, 28.0, Unit.PX);
		deckPanel.showWidget(0);
		
		labelErrorMessage = new InlineLabel("");
		layoutPanel.add(labelErrorMessage);
		layoutPanel.setWidgetLeftRight(labelErrorMessage, 10.0, Unit.PX, 12.0, Unit.PX);
		layoutPanel.setWidgetBottomHeight(labelErrorMessage, 38.0, Unit.PX, 35.0, Unit.PX);
		
		buttonCancel = new Button("Cancel");
		layoutPanel.add(buttonCancel);
		layoutPanel.setWidgetRightWidth(buttonCancel, 12.0, Unit.PX, 78.0, Unit.PX);
		layoutPanel.setWidgetBottomHeight(buttonCancel, 8.0, Unit.PX, 24.0, Unit.PX);
		
		buttonLocationInput = new Button("Next");
		layoutPanel.add(buttonLocationInput);
		layoutPanel.setWidgetRightWidth(buttonLocationInput, 96.0, Unit.PX, 78.0, Unit.PX);
		layoutPanel.setWidgetBottomHeight(buttonLocationInput, 8.0, Unit.PX, 24.0, Unit.PX);
		buttonLocationInput.setText("Next");
		
		buttonStoreSite = new Button("Finish");
		layoutPanel.setVisible(false);
		layoutPanel.add(buttonStoreSite);
		layoutPanel.setWidgetRightWidth(buttonStoreSite, 96.0, Unit.PX, 78.0, Unit.PX);
		layoutPanel.setWidgetBottomHeight(buttonStoreSite, 8.0, Unit.PX, 24.0, Unit.PX);
		
	}

	@Override
	public void showPageLocationInput() {
		deckPanel.showWidget(0);
		buttonLocationInput.setVisible(true);
		buttonStoreSite.setVisible(false);
	}

	@Override
	public void showPageFeedSelection() {
		deckPanel.showWidget(1);
		buttonLocationInput.setVisible(false);
		buttonStoreSite.setVisible(true);
	}

	@Override
	public HasClickHandlers getButtonLocationInput() {
		return buttonLocationInput;
	}

	@Override
	public HasClickHandlers getButtonCancel() {
		return buttonCancel;
	}

	@Override
	public HasClickHandlers getButtonStoreSite() {
		return buttonStoreSite;
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
