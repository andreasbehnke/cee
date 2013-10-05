package org.cee.webreader.client.ui;

import org.cee.webreader.client.ClientFactory;
import org.cee.webreader.client.ConfirmView;
import org.cee.webreader.client.NewsView;
import org.cee.webreader.client.NotificationView;
import org.cee.webreader.client.StartView;
import org.cee.webreader.client.content.NewSiteWizardView;
import org.cee.webreader.client.error.ErrorDialog;
import org.cee.webreader.client.error.ErrorHandler;
import org.cee.webreader.client.ui.NewSiteWizard;
import org.cee.webreader.client.workingset.WorkingSetView;

public class BinderClientFactory implements ClientFactory {

    private final ErrorHandler globalErrorHandler;
    
    private final PageSwitch pageSwitchView;
    
    private final StartView startView;
    
    private final NewsView newsView;
    
    private final NewSiteWizardView newSiteWizardView;

    private final NewSiteWizardView addSiteWizardView;

    private final WorkingSetView workingSetView;
    
    public BinderClientFactory() {
        Resources.INSTANCE.styles().ensureInjected();
        
        globalErrorHandler = new ErrorDialog();
        
        startView = new Start();
        newsView = new News();
        newSiteWizardView = new NewSiteWizard();
        addSiteWizardView = new NewSiteWizard();
        workingSetView = new WorkingSet();

        pageSwitchView = new PageSwitch(startView, newsView);
        pageSwitchView.showStartPage();
    }

    @Override
    public ErrorHandler getGlobalErrorHandler() {
        return globalErrorHandler;
    }

    @Override
    public StartView getStartView() {
        return startView;
    }

    @Override
    public NewsView getNewsView() {
        return newsView;
    }
    
    @Override
    public PageSwitch getPageSwitchView() {
        return pageSwitchView;
    }
    
    @Override
    public NewSiteWizardView getNewSiteWizardView() {
        return newSiteWizardView;
    }

    @Override
    public NewSiteWizardView getAddSiteWizardView() {
        return addSiteWizardView;
    }

    @Override
    public WorkingSetView getWorkingSetView() {
        return workingSetView;
    }
    
    @Override
    public ConfirmView createConfirmView() {
    	return new Confirm();
    }
    
    @Override
    public NotificationView createNotificationView() {
    	return new Notification();
    }
}
