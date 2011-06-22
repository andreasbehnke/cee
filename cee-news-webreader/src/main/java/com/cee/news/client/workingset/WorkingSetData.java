package com.cee.news.client.workingset;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Bean holding all view data of a working set
 */
public class WorkingSetData implements IsSerializable {
    
	private Boolean isNew = true;

	private String newName;
    
    private String oldName;
    
    private List<String> sites = new ArrayList<String>();

    /**
     * @return If true, this bean is a new created working set, otherwise this working set should be updated
     */
    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * @return The new name of this working set
     */
    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    /**
     * @return The old name of the working set, used by the backend service for renaming
     */
    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    /**
     * @return Site URL's of this working set
     */
    public List<String> getSites() {
        return sites;
    }

    public void setSites(List<String> sites) {
        this.sites = sites;
    }
}
