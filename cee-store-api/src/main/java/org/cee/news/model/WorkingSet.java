package org.cee.news.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of sites. The user can manage one site in different working sets for different purposes.
 */
public class WorkingSet {
    
    private String name;
    
    private List<EntityKey> sites = new ArrayList<EntityKey>();
    
    private String language;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EntityKey> getSites() {
        return sites;
    }

    public void setSites(List<EntityKey> sites) {
        this.sites = sites; 
    }

    /**
     * @return the ISO 639-1 Language Code of this working set
     */
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	@Override
	public String toString() {
		return "WorkingSet [name=" + name + ", language=" + language + "]";
	}
}
