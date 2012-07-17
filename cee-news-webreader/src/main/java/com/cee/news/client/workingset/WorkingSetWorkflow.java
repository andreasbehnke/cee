package com.cee.news.client.workingset;

import com.cee.news.client.async.EntityUpdateResult;
import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.content.AddSiteWorkflow;
import com.cee.news.client.content.NewSiteWizardView;
import com.cee.news.client.content.SiteAddedEvent;
import com.cee.news.client.content.SiteAddedHandler;
import com.cee.news.client.content.SiteListContentModel;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.error.ErrorSourceBase;
import com.cee.news.model.EntityKey;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class WorkingSetWorkflow extends ErrorSourceBase {

	private final WorkingSetListModel workingSetListModel;

	private final WorkingSetServiceAsync service = WorkingSetServiceAsync.Util.getInstance();

	private final WorkingSetView workingSetView;
	
	private final AddSiteWorkflow addSiteWorkflow;

	// TODO: Enabled / Disable buttons on async service request!
	public WorkingSetWorkflow(final WorkingSetListModel workingSetListModel,
	        final SiteListContentModel siteListModel,
	        final WorkingSetView workingSetView,
	        final NewSiteWizardView newSiteWizard) {

	    this.workingSetListModel = workingSetListModel;
		this.workingSetView = workingSetView;
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
	}

	public void newWorkingSet() {
		WorkingSetData workingSetData = new WorkingSetData();
		workingSetData.setIsNew(true);
		showEditor(workingSetData);
	}
	
	public void editWorkingSet(final EntityKey workingSetKey) {
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
	
	private void showEditor(WorkingSetData wsd) {
	    workingSetView.edit(wsd);
	    workingSetView.center();
	}

	private void validate() {
		final WorkingSetData workingSetData = workingSetView.getData();
		if (workingSetView.hasValidationErrors()) {
		    workingSetView.showValidationErrors();
		    return;
		}
		String name = workingSetData.getNewName();
		if (name == null || name.trim().isEmpty()) {
			showValidationError("Missing or invalid working set name!");
			return;
		}

		service.update(workingSetData, new AsyncCallback<EntityUpdateResult>() {

			@Override
			public void onSuccess(final EntityUpdateResult result) {
				switch (result.getState()) {
				case entityExists:
					showValidationError("Working Set with name " + workingSetData.getNewName() + " exists!");
					break;
				case ok:
				    workingSetView.hide();
					workingSetListModel.findAllWorkingSets(new NotificationCallback() {
						
						@Override
						public void finished() {
							workingSetListModel.setSelectedKey(result.getKey());
						}
					});
					break;

				default:
					break;
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not store working set");
			}
		});
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