package org.cee.webreader.client.content;

import java.util.List;

import org.cee.news.model.EntityKey;
import org.cee.webreader.client.async.NotificationCallback;
import org.cee.webreader.client.list.DefaultListModel;

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