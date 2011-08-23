package com.cee.news.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of sites. The user can manage one site in different working sets for different purposes.
 */
public class WorkingSet {
    
    private String name;
    
    private List<EntityKey> sites = new ArrayList<EntityKey>();

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
}
