package org.cee.webreader.client.ui;

/*
 * #%L
 * News Reader
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
