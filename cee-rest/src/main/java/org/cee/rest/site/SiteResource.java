package org.cee.rest.site;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.cee.client.site.SiteData;
import org.cee.rest.exception.MissingParameterException;
import org.cee.service.DuplicateKeyException;
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
	public List<EntityKey> orderedByName() throws StoreException, EntityNotFoundException {
		return siteService.orderedByName();
	}
	
	@GET
	@Path("ofWorkingSet/{key}")
	public List<EntityKey> ofWorkingSet(@PathParam("key") String workingSetKey) throws StoreException, EntityNotFoundException {
		return siteService.sitesOfWorkingSet(EntityKey.get(workingSetKey));
	}
	
	@GET
	@Path("get/{key}")
	public SiteData get(@PathParam("key") String key) throws StoreException, EntityNotFoundException {
		return siteService.get(EntityKey.get(key));
	}
	
	@GET
	@Path("getAll/{keyList}")
	public List<SiteData> getAll(@PathParam("keyList") String keyList) throws StoreException, EntityNotFoundException {
		List<String> keys =	Arrays.asList(StringUtils.split(keyList,','));
		return siteService.get(EntityKey.fromList(keys));
	}
	
	@GET
	@Path("guessUniqueSiteName/{proposal}")
	public String guessUniqueSiteName(@PathParam("proposal") String proposal) {
		return siteService.guessUniqueSiteName(proposal);
	}
	
	@POST
	public SiteData create(SiteData site) throws StoreException, MissingParameterException, DuplicateKeyException {
		if (site == null) {
			throw new MissingParameterException("site");
		}
		site.setIsNew(true);
		return siteService.update(site);
	}
	
	@PUT
	public SiteData update(SiteData site) throws StoreException, MissingParameterException, DuplicateKeyException {
		if (site == null) {
			throw new MissingParameterException("site");
		}
		site.setIsNew(true);
		return siteService.update(site);
	}
}
