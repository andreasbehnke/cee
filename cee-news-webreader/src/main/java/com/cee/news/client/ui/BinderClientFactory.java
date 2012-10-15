package com.cee.news.client.ui;

import com.cee.news.client.ClientFactory;
import com.cee.news.client.ConfirmView;
import com.cee.news.client.NewsView;
import com.cee.news.client.StartView;
import com.cee.news.client.ui.NewSiteWizard;
import com.cee.news.client.content.NewSiteWizardView;
import com.cee.news.client.error.ErrorDialog;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.workingset.WorkingSetView;

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
}
