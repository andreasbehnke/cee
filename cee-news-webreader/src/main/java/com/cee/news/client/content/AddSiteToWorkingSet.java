package com.cee.news.client.content;

import com.cee.news.client.workingset.WorkingSetData;
import com.cee.news.client.workingset.WorkingSetListModel;
import com.cee.news.client.workingset.WorkingSetServiceAsync;
import com.cee.news.model.EntityKey;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddSiteToWorkingSet extends AddSiteWorkflow implements SiteAddedHandler {

    private final WorkingSetServiceAsync workingSetService = WorkingSetServiceAsync.Util.getInstance();
    
    private final WorkingSetListModel workingSetListModel;
    
    public AddSiteToWorkingSet(final NewSiteWizardView wizard, final WorkingSetListModel workingSetListModel) {
        super(wizard);
        this.workingSetListModel = workingSetListModel;
        addSiteAddedHandler(this);
    }
    
    private void afterWorkingSetUpdate(EntityKey workingSetKey) {
        //This will fire an update causing all views to be updated
        workingSetListModel.setSelectedKey(workingSetKey);
    }

    @Override
    public void onSiteAdded(SiteAddedEvent event) {
        //add site to current working set
        final EntityKey siteKey = event.getEntityKey();
        final EntityKey workingSetKey = workingSetListModel.getSelectedKey();
        workingSetService.addSiteToWorkingSet(workingSetKey.getKey(), siteKey, new AsyncCallback<WorkingSetData>() {
            
            @Override
            public void onSuccess(WorkingSetData result) {
                afterWorkingSetUpdate(workingSetKey);
            }
            
            @Override
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not add site to working set");
            }
        });
    }
}
