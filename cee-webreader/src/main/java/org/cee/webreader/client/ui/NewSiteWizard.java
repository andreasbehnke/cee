package org.cee.webreader.client.ui;

/*
 * #%L
 * News Reader
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.List;

import org.cee.news.model.EntityKey;
import org.cee.processing.site.FeedData;
import org.cee.webreader.client.content.EntityKeyProvider;
import org.cee.webreader.client.content.EntityKeyRenderer;
import org.cee.webreader.client.content.NewSiteWizardView;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
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
import com.google.gwt.user.client.ui.ValueListBox;
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
    TextBox textBoxFeedUrl;
    
    @UiField
    TextBox textBoxSiteName;

    @UiField(provided = true)
    CellTable<FeedData> cellTableFeeds;
    
    @UiField(provided = true)
    ValueListBox<EntityKey> listBoxLanguage;
    
    @UiField
    HTMLPanel panelLanguageSelection;
    
    @UiField
    InlineLabel labelErrorMessage;
    
    @UiField
    InlineLabel labelLoadingMessage;
    
    @UiField
    Button buttonLocationInput;
    
    @UiField
    Button buttonStoreSite;
    
    @UiField
    Button buttonCancel;
    
    private Element loadingGlass;
    
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
    
        TextColumn<FeedData> columnLanguage = new TextColumn<FeedData>() {
            @Override
            public String getValue(FeedData feed) {
            	EntityKey languageKey = feed.getLanguage();
            	if (languageKey == null) {
            		return "unknown";
            	} else {
            		return languageKey.getName();
            	}
            }
        };
        cellTableFeeds.addColumn(columnLanguage, "Language");
        
        listBoxLanguage = new ValueListBox<EntityKey>(new EntityKeyRenderer(), new EntityKeyProvider());
        
        setWidget(uiBinder.createAndBindUi(this));
        setGlassEnabled(true);
        setStyleName(resources.styles().popupPanel());
        
        labelLoadingMessage.setVisible(false);
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
    public HasValue<String> getSiteLocationInput() {
        return textBoxSiteUrl;
    }
    
    @Override
    public HasValue<String> getFeedLocationInput() {
        return textBoxFeedUrl;
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
    
    private void showLoadingGlassPane() {
    	if (loadingGlass == null) {
    		loadingGlass = Document.get().createDivElement();
    		loadingGlass.addClassName(resources.styles().loadingGlass());
    	}
    	getElement().appendChild(loadingGlass);
    }
    
    private void hideLoadingGlassPane() {
    	if (loadingGlass != null) {
    		getElement().removeChild(loadingGlass);
    	}
    }

    @Override
    public void showLoading(String message) {
    	showLoadingGlassPane();
        labelErrorMessage.setVisible(false);
        labelLoadingMessage.setText(message);
        labelLoadingMessage.setVisible(true);
    }
    
    @Override
    public void hideLoading() {
    	hideLoadingGlassPane();
    	labelErrorMessage.setVisible(true);
        labelLoadingMessage.setVisible(false);
    }

	@Override
    public void setAvailableLanguages(List<EntityKey> languages) {
		listBoxLanguage.setAcceptableValues(languages);
    }

	@Override
    public EntityKey getSelectedLanguage() {
	    return listBoxLanguage.getValue();
    }

	@Override
    public void showLanguageSelection(boolean visible) {
		panelLanguageSelection.setVisible(visible);
    }
}
