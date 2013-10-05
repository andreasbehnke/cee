package org.cee.webreader.client.workingset;

import java.util.List;

import org.cee.news.model.EntityKey;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtWorkingSetService")
public interface WorkingSetService extends RemoteService {
	
	List<EntityKey> getWorkingSetsOrderedByName();
	
	WorkingSetData getWorkingSet(EntityKey workingSetKey);
	
	List<EntityKey> validateSiteLanguages(WorkingSetData workingSet);
	
	WorkingSetUpdateResult update(WorkingSetData workingSet);
	
	WorkingSetUpdateResult addSiteToWorkingSet(EntityKey workingSetKey, EntityKey siteKey);
	
	void deleteWorkingSet(EntityKey workingSetKey);
}
