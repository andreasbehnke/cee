package com.cee.news.client.content;

import java.util.List;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.cellview.client.CellTable;
import com.cee.news.model.Site;
import com.google.gwt.user.cellview.client.CellList;
import com.cee.news.model.Feed;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class NewSiteWizard extends DialogBox implements NewSiteWizardView {

	private CellTable<Feed> cellTableFeeds;
	private DeckPanel deckPanel;
	private TextBox textBoxSiteUrl;
	private TextBox textBoxSiteName;
	private Button buttonCancel;
	private Button buttonNext;
	private Button buttonFinish;

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
		
		cellTableFeeds = new CellTable<Feed>();
		panelSelectFeeds.add(cellTableFeeds);
		panelSelectFeeds.setWidgetLeftRight(cellTableFeeds, 12.0, Unit.PX, 12.0, Unit.PX);
		panelSelectFeeds.setWidgetTopHeight(cellTableFeeds, 42.0, Unit.PX, 144.0, Unit.PX);
		
		Column<Feed, Boolean> columnActive = new Column<Feed, Boolean>(new CheckboxCell()) {
			@Override
			public Boolean getValue(Feed feed) {
				return feed.isActive();
			}
		};
		columnActive.setFieldUpdater(new FieldUpdater<Feed, Boolean>() {
			
			@Override
			public void update(int index, Feed feed, Boolean active) {
				feed.setActive(active);
				cellTableFeeds.redraw();
			}
		});
		
		cellTableFeeds.addColumn(columnActive, "Active");
		
		TextColumn<Feed> columnTitle = new TextColumn<Feed>() {
			@Override
			public String getValue(Feed feed) {
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
		
		InlineLabel labelErrorMessage = new InlineLabel("");
		layoutPanel.add(labelErrorMessage);
		layoutPanel.setWidgetLeftRight(labelErrorMessage, 10.0, Unit.PX, 12.0, Unit.PX);
		layoutPanel.setWidgetBottomHeight(labelErrorMessage, 38.0, Unit.PX, 35.0, Unit.PX);
		
		buttonCancel = new Button("Cancel");
		layoutPanel.add(buttonCancel);
		layoutPanel.setWidgetRightWidth(buttonCancel, 12.0, Unit.PX, 78.0, Unit.PX);
		layoutPanel.setWidgetBottomHeight(buttonCancel, 8.0, Unit.PX, 24.0, Unit.PX);
		
		buttonNext = new Button("Next");
		layoutPanel.add(buttonNext);
		layoutPanel.setWidgetRightWidth(buttonNext, 96.0, Unit.PX, 78.0, Unit.PX);
		layoutPanel.setWidgetBottomHeight(buttonNext, 8.0, Unit.PX, 24.0, Unit.PX);
		buttonNext.setText("Next");
		
		buttonFinish = new Button("Finish");
		layoutPanel.setVisible(false);
		layoutPanel.add(buttonFinish);
		layoutPanel.setWidgetRightWidth(buttonFinish, 96.0, Unit.PX, 78.0, Unit.PX);
		layoutPanel.setWidgetBottomHeight(buttonFinish, 8.0, Unit.PX, 24.0, Unit.PX);
		
	}

	@Override
	public void showPageLocationInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPageFeedSelection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasClickHandlers getButtonNext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getButtonCancel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getButtonFinish() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocationInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSiteName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSiteName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFeeds(List<Feed> feeds) {
		// TODO Auto-generated method stub
		
	}
}
