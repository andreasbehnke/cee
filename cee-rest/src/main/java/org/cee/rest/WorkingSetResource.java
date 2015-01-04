package org.cee.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.cee.news.model.EntityKey;
import org.cee.news.store.StoreException;
import org.cee.news.store.WorkingSetStore;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

@Path("workingset")
@Component
public class WorkingSetResource {
	
	private WorkingSetStore workingSetStore;
	
	@Required
	public void setWorkingSetStore(WorkingSetStore workingSetStore) {
		this.workingSetStore = workingSetStore;
	}
	
	@GET
	@Path("sortedbyname")
	@Produces(MediaType.APPLICATION_JSON)
	public List<EntityKey> sortedByName() throws StoreException {
		return workingSetStore.getWorkingSetsOrderedByName();
	}
}
