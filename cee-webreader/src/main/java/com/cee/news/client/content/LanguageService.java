package com.cee.news.client.content;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtLanguageService")
public interface LanguageService extends RemoteService {

	LanguageList getSupportedLanguages();
}
