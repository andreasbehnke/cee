package org.cee.client.workingset;

import java.util.List;

import org.cee.news.model.EntityKey;

public interface WorkingSetClientService {
	List<EntityKey> getWorkingSetsOrderedByName();
	
	WorkingSetData getWorkingSet(EntityKey workingSetKey);
	
	List<EntityKey> validateSiteLanguages(WorkingSetData workingSet);
	
	WorkingSetUpdateResult update(WorkingSetData workingSet);
	
	WorkingSetUpdateResult addSiteToWorkingSet(EntityKey workingSetKey, EntityKey siteKey);
	
	void deleteWorkingSet(EntityKey workingSetKey);
}
