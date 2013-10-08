package org.cee.webreader.client.error;

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


import com.google.gwt.event.shared.GwtEvent;

public class ErrorEvent extends GwtEvent<ErrorHandler> {
    
    public final static Type<ErrorHandler> TYPE = new Type<ErrorHandler>();
    
    private final Throwable cause;
    
    private final String description;
    
    public ErrorEvent(Throwable cause, String description) {
        super();
        this.cause = cause;
        this.description = description;
    }

    public Throwable getCause() {
        return cause;
    }
    
    public String getDescription() {
        return description;
    }

    @Override
    public Type<ErrorHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ErrorHandler handler) {
        handler.onError(this);
    }
}