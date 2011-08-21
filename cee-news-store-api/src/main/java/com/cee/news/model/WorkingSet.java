package com.cee.news.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of sites. The user can manage one site in different working sets for different purposes.
 */
public class WorkingSet {
    
    private String name;
    
    private List<NamedKey> sites = new ArrayList<NamedKey>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NamedKey> getSites() {
        return sites;
    }

    public void setSites(List<NamedKey> sites) {
        this.sites = sites; 
    }
}
