package org.cee.webreader.client.util;

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

import com.google.gwt.safehtml.shared.SafeHtml;

public class SafeContentString implements SafeHtml {

	private static final long serialVersionUID = -5552382985009681588L;

	private String html;
	
	public SafeContentString() {}
	
	public SafeContentString(String html) {
		if (html == null) {
			throw new IllegalArgumentException("Parameter html must not be null!");
		}
		this.html = html;
	}	
	
	@Override
	public String asString() {
		return html;
	}

	@Override
	public boolean equals(Object obj) {
	    if (!(obj instanceof SafeHtml)) {
	        return false;
	      }
	      return html.equals(((SafeHtml) obj).asString());
	}
	
	@Override
	public int hashCode() {
		return html.hashCode();
	}
}
