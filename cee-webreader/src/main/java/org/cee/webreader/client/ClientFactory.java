package org.cee.webreader.client;

import org.cee.webreader.client.content.NewSiteWizardView;
import org.cee.webreader.client.error.ErrorHandler;
import org.cee.webreader.client.workingset.WorkingSetView;

public interface ClientFactory {

    ErrorHandler getGlobalErrorHandler();
    
    ConfirmView createConfirmView();
    
    NotificationView createNotificationView();
    
    PageSwitchView getPageSwitchView();
    
    StartView getStartView();
    
    NewsView getNewsView();
    
    NewSiteWizardView getNewSiteWizardView();
    
    NewSiteWizardView getAddSiteWizardView();
    
    WorkingSetView getWorkingSetView();
}
