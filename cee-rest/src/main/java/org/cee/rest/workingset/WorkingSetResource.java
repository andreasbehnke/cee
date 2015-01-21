package org.cee.rest.workingset;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.cee.client.workingset.WorkingSetData;
import org.cee.rest.exception.MissingParameterException;
import org.cee.service.DuplicateKeyException;
import org.cee.service.EntityNotFoundException;
import org.cee.service.workingset.WorkingSetService;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

@Path("workingset")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class WorkingSetResource {
	
	private WorkingSetService workingSetService;
	
	@Required
	public void setWorkingSetService(WorkingSetService workingSetService) {
		this.workingSetService = workingSetService;
	}
	
	@GET
	public List<EntityKey> orderedByName() throws StoreException {
		return workingSetService.orderedByName();
	}
	
	@GET
	@Path("get/{key}")
	public WorkingSetData get(@PathParam("key") String key) throws StoreException, EntityNotFoundException {
		return workingSetService.get(EntityKey.get(key));
	}
	
	@PUT
	@Path("validateSiteLanguage")
	public List<EntityKey> validateSiteLanguage(WorkingSetData workingSetData) throws StoreException, MissingParameterException {
		if (workingSetData == null) {
			throw new MissingParameterException("workingSetData");
		}
		return workingSetService.validateSiteLanguages(workingSetData);
	}
	
	@POST
	public WorkingSetData create(WorkingSetData workingSetData) throws StoreException, DuplicateKeyException, MissingParameterException {
		if (workingSetData == null) {
			throw new MissingParameterException("workingSetData");
		}
		workingSetData.setIsNew(true);
		return workingSetService.update(workingSetData);
	}
	
	@PUT
	public WorkingSetData update(WorkingSetData workingSetData) throws StoreException, DuplicateKeyException, MissingParameterException {
		if (workingSetData == null) {
			throw new MissingParameterException("workingSetData");
		}
		workingSetData.setIsNew(false);
		return workingSetService.update(workingSetData);
	}
	
	@DELETE
	@Path("{key}")
	public void delete(@PathParam("key") String key) throws StoreException, EntityNotFoundException {
		workingSetService.delete(EntityKey.get(key));
	}
}
