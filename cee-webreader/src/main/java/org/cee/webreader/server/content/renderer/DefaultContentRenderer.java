package org.cee.webreader.server.content.renderer;

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


import java.util.ArrayList;
import java.util.List;

import org.cee.webreader.client.content.EntityContent;

public abstract class DefaultContentRenderer<K, T> implements ContentRenderer<K, T> {

    @Override
    public List<EntityContent<K>> render(List<K> keys, List<T> objects, String templateName) {
        if (keys == null) {
            throw new IllegalArgumentException("Parameter keys must not be null");
        }
        if (objects == null) {
            throw new IllegalArgumentException("Parameter objects must not be null");
        }
        int size = keys.size();
        if (size != objects.size()) {
            throw new IllegalArgumentException("The parameters keys and objects must have same size");
        }
        List<EntityContent<K>> contents = new ArrayList<EntityContent<K>>(size);
        for (int i=0; i < size; i++) {
            contents.add(render(keys.get(i), objects.get(i), templateName));
        }
        return contents;
    }
}