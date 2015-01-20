package org.cee.webreader.client.workingset;

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

import org.cee.client.workingset.WorkingSetData;
import org.cee.store.EntityKey;
import org.cee.webreader.client.ConfirmView;
import org.cee.webreader.client.async.NotificationCallback;
import org.cee.webreader.client.content.AddSiteWorkflow;
import org.cee.webreader.client.content.LanguageListModel;
import org.cee.webreader.client.content.NewSiteWizardView;
import org.cee.webreader.client.content.SiteAddedEvent;
import org.cee.webreader.client.content.SiteAddedHandler;
import org.cee.webreader.client.content.SiteListContentModel;
import org.cee.webreader.client.error.ErrorHandler;
import org.cee.webreader.client.error.ErrorSourceBase;
import org.cee.webreader.client.list.ListChangedEvent;
import org.cee.webreader.client.list.ListChangedHandler;
import org.cee.webreader.client.list.MultiSelectListPresenter;
import org.cee.webreader.client.list.SelectionListChangedEvent;
import org.cee.webreader.client.list.SelectionListChangedHandler;
import org.cee.webreader.client.workingset.WorkingSetUpdateResult.State;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class WorkingSetWorkflow extends ErrorSourceBase {

	private final WorkingSetListModel workingSetListModel;

	private final GwtWorkingSetServiceAsync service = GwtWorkingSetServiceAsync.Util.getInstance();

	private final WorkingSetView workingSetView;
	
	private final ConfirmView confirmDeletionView;
	
	private final AddSiteWorkflow addSiteWorkflow;
	
	private final LanguageListModel languageListModel;
	
	private WorkingSetData currentWorkingSet = null;

	public WorkingSetWorkflow(final WorkingSetListModel workingSetListModel,
	        final SiteListContentModel siteListModel,
	        final LanguageListModel languageListModel,
	        final WorkingSetView workingSetView,
	        final NewSiteWizardView newSiteWizard,
	        final ConfirmView confirmDeletionView) {

	    this.workingSetListModel = workingSetListModel;
		this.workingSetView = workingSetView;
		this.confirmDeletionView = confirmDeletionView;
		this.languageListModel = languageListModel;
		workingSetView.getButtonCancel().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				workingSetView.hide();
			}
		});
		workingSetView.getButtonSave().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				validate();
			}
		});
		workingSetView.getButtonRemoveAllSites().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                siteListModel.clearSelection();
            }
        });
		workingSetView.addSiteSelectionListChangedHandler(new SelectionListChangedHandler<EntityKey>() {
            @Override
            public void onSelectionListChanged(SelectionListChangedEvent<EntityKey> event) {
                siteListModel.setSelections(event.getKeys());
            }
        });
		siteListModel.addSelectionListChangedHandler(new SelectionListChangedHandler<EntityKey>() {
            @Override
            public void onSelectionListChanged(SelectionListChangedEvent<EntityKey> event) {
                workingSetView.setSelectedSites(event.getKeys());
            }
        });
		languageListModel.addListChangedHandler(new ListChangedHandler<EntityKey>() {
			@Override
			public void onContentListChanged(ListChangedEvent<EntityKey> event) {
				workingSetView.setAvailableLanguages(event.getValues(), languageListModel.getDefaultLanguage());
				newSiteWizard.setAvailableLanguages(event.getValues());
			}
		});
		workingSetView.addLanguageChangedHandler(new ValueChangeHandler<EntityKey>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<EntityKey> event) {
				validateSiteLanguages();
			}
		});

		addSiteWorkflow = new AddSiteWorkflow(newSiteWizard);
		workingSetView.getButtonAddNewSite().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addSiteWorkflow.start();
			}
		});
		addSiteWorkflow.addSiteAddedHandler(new SiteAddedHandler() {
			@Override
			public void onSiteAdded(final SiteAddedEvent event) {
				siteListModel.findAllSites(new NotificationCallback() {
					
					@Override
					public void finished() {
					    siteListModel.addSelection(event.getEntityKey());
					}
				});
			}
		});
		
		new MultiSelectListPresenter<EntityKey>(siteListModel, siteListModel, workingSetView.getAvailableSitesList(), workingSetView.getSelectedSitesList());
		
		siteListModel.addSelectionListChangedHandler(new SelectionListChangedHandler<EntityKey>() {
			
			@Override
			public void onSelectionListChanged(SelectionListChangedEvent<EntityKey> event) {
				validateSiteLanguages();
			}
		});
		
		confirmDeletionView.getButtonNo().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				confirmDeletionView.hide();
			}
		});
		confirmDeletionView.getButtonYes().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				confirmDeletionView.hide();
				performDeletion();
			}
		});
	}
	
	private void validateSiteLanguages() {
		if (currentWorkingSet != null) {
			service.validateSiteLanguages(currentWorkingSet, new AsyncCallback<List<EntityKey>>() {
				
				@Override
				public void onSuccess(List<EntityKey> result) {
					displayNotificationsSiteLanguageDiffer(result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					fireErrorEvent(caught, "Could not validate working set");
				}
			});
		}
	}

	private void displayNotificationsSiteLanguageDiffer(List<EntityKey> sitesWithDifferentLanguage) {
		if (sitesWithDifferentLanguage.size() == 0) {
			workingSetView.getErrorText().setText("");
		} else {
			String differentSites = "";
			boolean first = true;
			for (EntityKey entityKey : sitesWithDifferentLanguage) {
				if (first) {
					first = false;
				} else {
					differentSites += ",";
				}
				differentSites += entityKey.getName();
			}
			showValidationError("The following sites have a different language than working set: " + differentSites + ". This may result in bad search and related documents behaviour!");
		}
	}
	
	public void newWorkingSet() {
		WorkingSetData workingSetData = new WorkingSetData();
		workingSetData.setIsNew(true);
		workingSetData.setLanguage(languageListModel.getDefaultLanguage());
		showEditor(workingSetData);
	}
	
	public void editWorkingSet() {
		final EntityKey workingSetKey = workingSetListModel.getSelectedKey();
		service.getWorkingSet(workingSetKey, new AsyncCallback<WorkingSetData>() {
			
			@Override
			public void onSuccess(WorkingSetData result) {
				showEditor(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not retrieve working set \"" + workingSetKey + "\"");
			}
		});
	}
	
	public void deleteWorkingSet() {
		final EntityKey workingSetKey = workingSetListModel.getSelectedKey();
		confirmDeletionView.getLabelTitle().setText("Delete Working Set");
		confirmDeletionView.getLabelQuestion().setText("Are you sure deleting working set " + workingSetKey.getName() + "?");
		confirmDeletionView.center();
	}
	
	private void performDeletion() {
		final EntityKey workingSetKey = workingSetListModel.getSelectedKey();
		service.deleteWorkingSet(workingSetKey, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				workingSetListModel.findAllWorkingSets(new NotificationCallback() {
					
					@Override
					public void finished() {
						if (workingSetListModel.getContentCount() > 0) {
		                    EntityKey firstWorkingSet = workingSetListModel.getKeys().get(0);
		                    workingSetListModel.setSelectedKey(firstWorkingSet);
		                } else {
		                	workingSetListModel.setSelectedKey(null);
		                }
					}
				});
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not delete working set \"" + workingSetKey + "\"");
			}
		});
	}
	
	private void showEditor(WorkingSetData wsd) {
		currentWorkingSet = wsd;
		workingSetView.getErrorText().setText("");
	    workingSetView.edit(wsd);
	    workingSetView.center();
	}

	private void validate() {
		currentWorkingSet = workingSetView.getData();
		if (workingSetView.hasValidationErrors()) {
		    workingSetView.showValidationErrors();
		    return;
		}
		String name = currentWorkingSet.getNewName();
		if (name == null || name.trim().isEmpty()) {
			showValidationError("Missing or invalid working set name!");
			return;
		}

		service.update(currentWorkingSet, new AsyncCallback<WorkingSetUpdateResult>() {

			@Override
			public void onSuccess(final WorkingSetUpdateResult result) {
				onWorkingSetUpdateSuccess(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not store working set");
			}
		});
	}
	
	private void onWorkingSetUpdateSuccess(final WorkingSetUpdateResult result) {
		if (result.getState() == State.entityExists) {
			showValidationError("Working Set with name " + currentWorkingSet.getNewName() + " exists!");
		} else {
			workingSetView.hide();
			workingSetListModel.findAllWorkingSets(new NotificationCallback() {
				
				@Override
				public void finished() {
					workingSetListModel.setSelectedKey(result.getKey());
				}
			});
		}
	}

	private void showValidationError(String message) {
	    workingSetView.getErrorText().setText(message);
	}
	
	@Override
	public HandlerRegistration addErrorHandler(ErrorHandler handler) {
		addSiteWorkflow.addErrorHandler(handler);
		return super.addErrorHandler(handler);
	}
}