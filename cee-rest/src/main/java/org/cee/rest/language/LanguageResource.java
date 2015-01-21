package org.cee.rest.language;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.cee.client.language.LanguageList;
import org.cee.service.EntityNotFoundException;
import org.cee.service.language.LanguageService;
import org.cee.store.StoreException;
import org.springframework.stereotype.Component;

@Path("language")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class LanguageResource {

	private LanguageService languageService;
	
	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}
	
	@GET
	public LanguageList supportedLanguages() throws StoreException, EntityNotFoundException {
		return languageService.getSupportedLanguages();
	}
}
