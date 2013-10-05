package org.cee.webreader.client.workingset;

import java.util.List;

import org.cee.news.model.EntityKey;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WorkingSetUpdateResult implements IsSerializable {
	
	public enum State {
		ok,
		entityExists,
		siteLanguagesDiffer
	}
	
	private State state;
	
	private List<EntityKey> sitesWithDifferentLang;
	
	private WorkingSetData workingSetData;

	private EntityKey key;
	
	public WorkingSetUpdateResult() {}
	
	public WorkingSetUpdateResult(State state, List<EntityKey> sitesWithDifferentLang, WorkingSetData workingSetData, EntityKey workingSetKey) {
		this.state = state;
		this.sitesWithDifferentLang = sitesWithDifferentLang;
		this.workingSetData = workingSetData;
		this.key = workingSetKey;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public EntityKey getKey() {
		return key;
	}

	public void setKey(EntityKey key) {
		this.key = key;
	}

	public List<EntityKey> getSitesWithDifferentLang() {
		return sitesWithDifferentLang;
	}

	public void setSitesWithDifferentLang(List<EntityKey> sitesWithDifferentLang) {
		this.sitesWithDifferentLang = sitesWithDifferentLang;
	}

	public WorkingSetData getWorkingSetData() {
		return workingSetData;
	}

	public void setWorkingSetData(WorkingSetData workingSetData) {
		this.workingSetData = workingSetData;
	}

}
