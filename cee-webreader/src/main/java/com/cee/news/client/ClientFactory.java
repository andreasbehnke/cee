package com.cee.news.client;

import com.cee.news.client.content.NewSiteWizardView;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.workingset.WorkingSetView;

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
