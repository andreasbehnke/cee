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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HtmlBuilder {
	
	private final StringBuilder buffer;
	
	public HtmlBuilder(final StringBuilder buffer) {
	    this.buffer = buffer;
    }

	private String formatDate(Calendar calendar) {
        return SimpleDateFormat.getDateInstance().format(calendar.getTime());
    }

    public HtmlBuilder appendHtmlElement(String htmlElement, Object content) {
    	buffer.append('<').append(htmlElement).append('>')
			.append(content)
			.append("</").append(htmlElement).append('>');
    	return this;
    }
    
    public HtmlBuilder appendIfNotNull(String htmlElement, Object content) {
    	if (content != null) {
    		appendHtmlElement(htmlElement, content);
    	}
    	return this;
    }
    
    public HtmlBuilder appendIfNotNull(String htmlElement, Calendar calendar) {
    	if (calendar != null) {
    		appendHtmlElement(htmlElement, formatDate(calendar));
    	}
    	return this;
    }
    
    public HtmlBuilder appendIfIsNumber(String htmlElement, double number) {
    	if (number > 0) {
    		appendHtmlElement(htmlElement, number);
    	}
    	return this;
    }
    
    public HtmlBuilder appendLink(String href, String target, String content) {
    	buffer.append("<a href=\"").append(href).append("\" target=\"").append(target).append("\">")
    		.append(content).append("</a>");
    	return this;
    }
    
    public HtmlBuilder append(Object content)  {
    	buffer.append(content);
    	return this;
    }
    
    
}
