package com.cee.news.client;

import com.cee.news.client.content.NewSiteWizard;
import com.cee.news.client.content.NewSiteWizardView;
import com.cee.news.client.error.ErrorDialog;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.workingset.WorkingSetEditor;
import com.cee.news.client.workingset.WorkingSetView;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class StandardClientFactory implements ClientFactory {

    private final ErrorHandler globalErrorHandler;
    
    private final PageSwitchView pageSwitchView;
    
    private final StartView startView;
    
    private final NewsView newsView;
    
    private final NewSiteWizardView newSiteWizardView;
    
    private final NewSiteWizardView addSiteWizardView;
    
    private final WorkingSetView workingSetView;
    
    public StandardClientFactory() {
        
        globalErrorHandler = new ErrorDialog();
        
        LayoutPanel layoutPanel = new LayoutPanel();
        layoutPanel.setSize("100%", "100%");
        
        startView = new StartPanel();
        newsView = new NewsPanel();
        pageSwitchView = new PageSwitchPanel(startView, newsView);
        
        layoutPanel.add(pageSwitchView);
        Widget pageSwitchWidget = pageSwitchView.asWidget();
        pageSwitchWidget.setSize("100%", "100%");
        layoutPanel.setWidgetLeftRight(pageSwitchWidget, 0.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(pageSwitchWidget, 0.0, Unit.PX, 0.0, Unit.PX);
        
        RootPanel.get().add(layoutPanel, 0, 0);
        
        newSiteWizardView = new NewSiteWizard();
        addSiteWizardView = new NewSiteWizard();
        workingSetView = new WorkingSetEditor();
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
    public PageSwitchView getPageSwitchView() {
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
}
