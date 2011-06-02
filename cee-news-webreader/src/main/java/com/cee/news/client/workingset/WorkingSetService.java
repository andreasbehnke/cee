package com.cee.news.client.workingset;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtWorkingSetService")
public interface WorkingSetService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static WorkingSetServiceAsync instance;
		public static WorkingSetServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(WorkingSetService.class);
			}
			return instance;
		}
	}
	
	List<String> getWorkingSetsOrderedByName();
	
	WorkingSetData getWorkingSet(String name);
	
	boolean containsWorkingSet(String name);
	
	void update(WorkingSetData workingSet);
}
