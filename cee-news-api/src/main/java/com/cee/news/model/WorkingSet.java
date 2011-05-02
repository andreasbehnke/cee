package com.cee.news.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of sites. The user can manage one site in different working sets for different purposes.
 */
public class WorkingSet {
    
    private String name;
    
    private List<String> sites = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSites() {
        return sites;
    }

    public void setSites(List<String> sites) {
        this.sites = sites; 
    }
}
