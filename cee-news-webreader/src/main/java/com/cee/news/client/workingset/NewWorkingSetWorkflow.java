package com.cee.news.client.workingset;

import java.util.List;

import com.cee.news.client.error.ErrorEvent;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.error.ErrorSource;
import com.cee.news.client.list.AddRemoveListModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class NewWorkingSetWorkflow implements ErrorSource {
	
	private final WorkingSetListModel workingSetListModel;
	
	private final AddRemoveListModel sitesModel;
	
	private WorkingSetDataEditorDriver driver;
	
	private WorkingSetServiceAsync service = WorkingSetServiceAsync.Util.getInstance();
	
	private WorkingSetEditor editor;
	
	private EventBus handlerManager = new SimpleEventBus();
	
	public NewWorkingSetWorkflow(WorkingSetListModel workingSetListModel, AddRemoveListModel sitesModel) {
		this.sitesModel = sitesModel;
		this.workingSetListModel = workingSetListModel;
		driver = GWT.create(WorkingSetDataEditorDriver.class);
	}

	public void start() {
		
		WorkingSetData workingSetData = new WorkingSetData();
		workingSetData.setIsNew(true);
		
		editor = new WorkingSetEditor(sitesModel);
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
		driver.initialize(editor);
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
		}
		
		service.containsWorkingSet(workingSetData.getNewName(), new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					showValidationError("Working Set with name " + workingSetData.getNewName() + " exists!");
				} else {
					store(workingSetData);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could test working set existence");
			}
		});
		
	}
	
	protected void store(final WorkingSetData workingSetData) {
		service.update(workingSetData, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				editor.hide();
				workingSetListModel.update();
				workingSetListModel.setSelectedWorkingSet(workingSetData.getNewName());
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
	
	protected void fireErrorEvent(Throwable cause, String description) {
		handlerManager.fireEvent(new ErrorEvent(cause, description));
	}

	@Override
	public HandlerRegistration addErrorHandler(ErrorHandler handler) {
		return handlerManager.addHandler(ErrorEvent.TYPE, handler);
	}
}