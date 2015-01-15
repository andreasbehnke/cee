package org.cee.rest.site;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.cee.service.EntityNotFoundException;
import org.cee.service.site.SiteService;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

@Path("site")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SiteResource {

	private SiteService siteService;
	
	@Required
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
	
	@GET
	public List<EntityKey> orderedByName(@QueryParam("workingSet") String workingSetKey) throws StoreException, EntityNotFoundException {
		if (workingSetKey == null) {
			return siteService.orderedByName();
		} else {
			return siteService.sitesOfWorkingSet(EntityKey.get(workingSetKey));
		}
	}
}
