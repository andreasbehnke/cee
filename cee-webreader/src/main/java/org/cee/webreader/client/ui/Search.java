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


import org.cee.webreader.client.search.SearchView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Search extends Composite implements SearchView {
    
    private static SearchUiBinder uiBinder = GWT.create(SearchUiBinder.class);
    
    @UiField
    TextBox textBoxSearch;
    
    @UiField
    Button buttonSearch;

    @UiField
    Button buttonClear;

    interface SearchUiBinder extends UiBinder<Widget, Search> {
    }

    public Search() {
        initWidget(uiBinder.createAndBindUi(this));
        textBoxSearch.addKeyUpHandler(new KeyUpHandler() {
			
        	@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					buttonSearch.click();
				} else if (textBoxSearch.getText() == null || textBoxSearch.getText().isEmpty()) {
					buttonClear.click();
				}
				
			}
		});
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
        buttonSearch.setVisible(enabled);
    }

    @Override
    public void setClearButtonEnabled(boolean enabled) {
        buttonClear.setVisible(enabled);
    }

}
