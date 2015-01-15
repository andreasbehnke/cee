package org.cee.rest.workingset;

import java.util.ArrayList;
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
import org.cee.client.workingset.WorkingSetUpdateResult;
import org.cee.rest.exception.DuplicateKeyException;
import org.cee.rest.exception.MissingParameterException;
import org.cee.rest.exception.ValidationException;
import org.cee.rest.exception.ValidationIssue;
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
	@Path("{key}")
	public WorkingSetData get(@PathParam("key") String key) throws StoreException, EntityNotFoundException {
		return workingSetService.get(EntityKey.get(key));
	}
	
	private void checkNotNull(String fieldName, Object field, List<ValidationIssue> issues) {
		if (field == null) {
			issues.add(new ValidationIssue(fieldName, "Field " + fieldName + " must not be not empty"));
		}
	}
	
	private void validateInput(WorkingSetData workingSetData) throws ValidationException {
		List<ValidationIssue> issues = new ArrayList<>();
		checkNotNull("newName", workingSetData.getNewName(), issues);
		checkNotNull("language", workingSetData.getLanguage(), issues);
		if (!issues.isEmpty()) {
			throw new ValidationException(issues);
		}
		if (workingSetData.getSites() == null) {
			workingSetData.setSites(new ArrayList<EntityKey>());
		}
	}
	
	@POST
	@Path("validateSiteLanguage")
	public List<EntityKey> validateSiteLanguage(WorkingSetData workingSetData) throws StoreException, MissingParameterException, ValidationException {
		if (workingSetData == null) {
			throw new MissingParameterException("workingSetData");
		}
		validateInput(workingSetData);
		return workingSetService.validateSiteLanguages(workingSetData);
	}
	
	private WorkingSetUpdateResult createOrUpdate(WorkingSetData workingSetData) throws StoreException, DuplicateKeyException, ValidationException, MissingParameterException {
		if (workingSetData == null) {
			throw new MissingParameterException("workingSetData");
		}
		validateInput(workingSetData);
		WorkingSetUpdateResult result = workingSetService.update(workingSetData);
		switch (result.getState()) {
		case entityExists:
			throw new DuplicateKeyException(result);
		case siteLanguagesDiffer:
			throw new ValidationException(result);
		default:
			return result;
		}
	}
	
	@POST
	public WorkingSetUpdateResult create(WorkingSetData workingSetData) throws StoreException, DuplicateKeyException, ValidationException, MissingParameterException {
		workingSetData.setIsNew(true);
		return createOrUpdate(workingSetData);
	}
	
	@PUT
	public WorkingSetUpdateResult update(WorkingSetData workingSetData) throws StoreException, DuplicateKeyException, ValidationException, MissingParameterException {
		workingSetData.setIsNew(false);
		return createOrUpdate(workingSetData);
	}
	
	@DELETE
	@Path("{key}")
	public void delete(@PathParam("key") String key) throws StoreException, EntityNotFoundException {
		workingSetService.delete(EntityKey.get(key));
	}
}
