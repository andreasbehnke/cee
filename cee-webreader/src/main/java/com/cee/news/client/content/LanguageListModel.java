package com.cee.news.client.content;

import java.util.List;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.list.DefaultListModel;
import com.cee.news.model.EntityKey;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LanguageListModel extends DefaultListModel<EntityKey> {
    
    private final LanguageServiceAsync languageService = LanguageServiceAsync.Util.getInstance();
    
    private EntityKey defaultLanguage;
    
    public void findAllLanguages() {
    	this.findAllLanguages(null);
    }
    
    public void findAllLanguages(final NotificationCallback callback) {
    	
    	languageService.getSupportedLanguages(new AsyncCallback<LanguageList>() {

			@Override
			public void onFailure(Throwable caught) {
				fireErrorEvent(caught, "Could not retrieve list of languages");
			}

			@Override
			public void onSuccess(LanguageList result) {
				List<EntityKey> languages = result.getLanguages();
				defaultLanguage = languages.get(result.getDefaultLanguage());
				setValues(languages);
				if (callback != null) {
					callback.finished();
				}
			}
    		
		});
    }
    
    public EntityKey getDefaultLanguage() {
    	return defaultLanguage;
    }
}