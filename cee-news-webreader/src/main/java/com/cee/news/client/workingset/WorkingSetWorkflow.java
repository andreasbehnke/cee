package com.cee.news.client.workingset;

import java.util.List;

import com.cee.news.client.async.EntityUpdateResult;
import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.content.AddSiteWorkflow;
import com.cee.news.client.content.NewSiteWizardView;
import com.cee.news.client.content.SiteAddedEvent;
import com.cee.news.client.content.SiteAddedHandler;
import com.cee.news.client.content.SiteListContentModel;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.error.ErrorSourceBase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class WorkingSetWorkflow extends ErrorSourceBase {

	private final WorkingSetListModel workingSetListModel;

	private final WorkingSetDataEditorDriver driver;

	private final WorkingSetServiceAsync service = WorkingSetServiceAsync.Util.getInstance();

	private final WorkingSetEditor editor;
	
	final AddSiteWorkflow addSiteWorkflow;

	// TODO: Enabled / Disable buttons on async service request!
	public WorkingSetWorkflow(final WorkingSetListModel workingSetListModel, final SiteListContentModel siteListModel, final WorkingSetEditor editor, final NewSiteWizardView newSiteWizard) {
		this.workingSetListModel = workingSetListModel;
		driver = GWT.create(WorkingSetDataEditorDriver.class);
		this.editor = editor;
		editor.getButtonCancel().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editor.hide();
			}
		});
		editor.getButtonSave().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				validate();
			}
		});
		addSiteWorkflow = new AddSiteWorkflow(newSiteWizard);
		editor.getButtonAddNewSite().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addSiteWorkflow.start();
			}
		});
		addSiteWorkflow.addSiteAddedHandler(new SiteAddedHandler() {
			@Override
			public void onSiteAdded(final SiteAddedEvent event) {
				siteListModel.update(new NotificationCallback() {
					
					@Override
					public void finished() {
						siteListModel.addSelection(event.getSiteName());
					}
				});
			}
		});
		
		driver.initialize(editor);
	}

	public void newWorkingSet() {
		WorkingSetData workingSetData = new WorkingSetData();
		workingSetData.setIsNew(true);
		showEditor(workingSetData);
	}
	
	public void editWorkingSet(final String name) {
		service.getWorkingSet(name, new AsyncCallback<WorkingSetData>() {
			
			@Override
			public void onSuccess(WorkingSetData result) {
				showEditor(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not retrieve working set \"" + name + "\"");
			}
		});
	}
	
	protected void showEditor(WorkingSetData workingSetData) {
		driver.edit(workingSetData);
		editor.center();
	}

	protected void validate() {
		final WorkingSetData workingSetData = driver.flush();
		if (driver.hasErrors()) {
			List<EditorError> errors = driver.getErrors();
			String message = "";
			for (EditorError editorError : errors) {
				message += editorError.getMessage();
			}
			showValidationError(message);
			return;
		}
		String name = workingSetData.getNewName();
		if (name == null || name.trim().isEmpty()) {
			showValidationError("Missing or invalid working set name!");
			return;
		}

		service.update(workingSetData, new AsyncCallback<EntityUpdateResult>() {

			@Override
			public void onSuccess(EntityUpdateResult result) {
				switch (result) {
				case entityExists:
					showValidationError("Working Set with name " + workingSetData.getNewName() + " exists!");
					break;
				case ok:
					editor.hide();
					workingSetListModel.update(new NotificationCallback() {
						
						@Override
						public void finished() {
							workingSetListModel.setSelectedKey(workingSetData.getNewName());
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

	protected void showValidationError(String message) {
		editor.getErrorText().setText(message);
	}
	
	@Override
	public HandlerRegistration addErrorHandler(ErrorHandler handler) {
		addSiteWorkflow.addErrorHandler(handler);
		return super.addErrorHandler(handler);
	}
}