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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

public abstract class ErrorSourceBase implements ErrorSource {

	protected final EventBus handlerManager = new SimpleEventBus();

	protected void fireErrorEvent(Throwable cause, String description) {
		handlerManager.fireEvent(new ErrorEvent(cause, description));
	}

	@Override
	public HandlerRegistration addErrorHandler(ErrorHandler handler) {
		return handlerManager.addHandler(ErrorEvent.TYPE, handler);
	}
}