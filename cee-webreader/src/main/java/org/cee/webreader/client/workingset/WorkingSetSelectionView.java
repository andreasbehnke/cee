package org.cee.webreader.client.workingset;

/*
 * #%L
 * News Reader
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


import java.util.List;

import org.cee.news.model.EntityKey;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public interface WorkingSetSelectionView {

    void addSelectionChangedHandler(ChangeHandler handler);
    
    HasClickHandlers getNewButton();
    
    HasClickHandlers getEditButton();
    
    HasClickHandlers getDeleteButton();
    
    void setEditButtonEnabled(boolean enabled);
    
    void setDeleteButtonEnabled(boolean enabled);
    
    int getSelectedWorkingSet();
    
    void setSelectedWorkingSet(int key);
    
    void setWorkingSets(List<EntityKey> names);
}
