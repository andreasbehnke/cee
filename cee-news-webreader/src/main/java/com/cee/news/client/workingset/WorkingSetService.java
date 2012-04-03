package com.cee.news.client.workingset;

import java.util.List;

import com.cee.news.client.async.EntityUpdateResult;
import com.cee.news.model.EntityKey;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtWorkingSetService")
public interface WorkingSetService extends RemoteService {
	
	List<EntityKey> getWorkingSetsOrderedByName();
	
	WorkingSetData getWorkingSet(EntityKey workingSetKey);
	
	EntityUpdateResult update(WorkingSetData workingSet);
	
	WorkingSetData addSiteToWorkingSet(EntityKey workingSetKey, EntityKey siteKey);
}
