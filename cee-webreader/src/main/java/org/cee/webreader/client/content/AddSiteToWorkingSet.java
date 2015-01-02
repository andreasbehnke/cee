package org.cee.webreader.client.content;

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

import org.cee.client.workingset.WorkingSetUpdateResult;
import org.cee.client.workingset.WorkingSetUpdateResult.State;
import org.cee.news.model.EntityKey;
import org.cee.webreader.client.NotificationView;
import org.cee.webreader.client.workingset.WorkingSetListModel;
import org.cee.webreader.client.workingset.GwtWorkingSetServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddSiteToWorkingSet extends AddSiteWorkflow implements SiteAddedHandler {

    private final GwtWorkingSetServiceAsync workingSetService = GwtWorkingSetServiceAsync.Util.getInstance();
    
    private final WorkingSetListModel workingSetListModel;
    
    private final NotificationView notification;
    
    public AddSiteToWorkingSet(final NewSiteWizardView wizard, final WorkingSetListModel workingSetListModel, final NotificationView notification) {
        super(wizard);
        this.workingSetListModel = workingSetListModel;
        this.notification = notification;
        addSiteAddedHandler(this);
        notification.getLabelTitle().setText("Site and Working Set Language differ");
        notification.getButtonOk().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				notification.hide();
			}
		});
    }
    
    @Override
    public void onSiteAdded(SiteAddedEvent event) {
    	final EntityKey siteKey = event.getEntityKey();
    	final EntityKey workingSetKey = workingSetListModel.getSelectedKey();
        workingSetService.addSiteToWorkingSet(workingSetKey, siteKey, new AsyncCallback<WorkingSetUpdateResult>() {
			
			@Override
			public void onSuccess(WorkingSetUpdateResult result) {
				if (result.getState() == State.siteLanguagesDiffer) {
					List<EntityKey> sitesWithDifferentLanguage = result.getSitesWithDifferentLang();
					for (EntityKey entityKey : sitesWithDifferentLanguage) {
						if (entityKey.equals(siteKey)) {
							notification.getLabelMessage().setText("The added site " + siteKey.getName() + " is not using the same language than the working set! This may result in bad search results and bad related news.");
							notification.center();
							break;
						}
					}
				}
				workingSetListModel.setSelectedKey(workingSetKey);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not add site to working set");
			}
		});
    }
    
}
