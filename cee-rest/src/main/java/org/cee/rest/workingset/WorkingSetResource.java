package org.cee.rest.workingset;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.cee.client.workingset.WorkingSetData;
import org.cee.service.EntityNotFoundException;
import org.cee.service.workingset.WorkingSetService;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

@Path("workingset")
@Component
public class WorkingSetResource {
	
	private WorkingSetService workingSetService;
	
	@Required
	public void setWorkingSetService(WorkingSetService workingSetService) {
		this.workingSetService = workingSetService;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<EntityKey> orderedByName() throws StoreException {
		return workingSetService.orderedByName();
	}
	
	@GET
	@Path("{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public WorkingSetData get(@PathParam("key") String key) throws StoreException, EntityNotFoundException {
		return workingSetService.get(EntityKey.get(key));
	}
}
