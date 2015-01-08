package org.cee.store;

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


public class StoreException extends Exception {

    private static final long serialVersionUID = 2467663447961830861L;
    
    private final Object entity;

    public StoreException(Object entity) {
        super();
        this.entity = entity;
    }

    public StoreException(String message) {
        super(message);
        entity = null;
    }

    public StoreException(Object entity, String message, Throwable cause) {
        super(message, cause);
        this.entity = entity;
    }

    public StoreException(Object entity, String message) {
        super(message);
        this.entity = entity;
    }

    public StoreException(Object entity, Throwable cause) {
        super(cause);
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }

}
