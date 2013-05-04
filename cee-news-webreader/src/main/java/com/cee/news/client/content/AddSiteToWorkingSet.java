package com.cee.news.client.content;

import java.util.List;

import com.cee.news.client.NotificationView;
import com.cee.news.client.workingset.WorkingSetListModel;
import com.cee.news.client.workingset.WorkingSetServiceAsync;
import com.cee.news.client.workingset.WorkingSetUpdateResult;
import com.cee.news.client.workingset.WorkingSetUpdateResult.State;
import com.cee.news.model.EntityKey;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddSiteToWorkingSet extends AddSiteWorkflow implements SiteAddedHandler {

    private final WorkingSetServiceAsync workingSetService = WorkingSetServiceAsync.Util.getInstance();
    
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
