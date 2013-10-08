package org.cee.news.model;

/*
 * #%L
 * Content Extraction Engine - News Store API
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
