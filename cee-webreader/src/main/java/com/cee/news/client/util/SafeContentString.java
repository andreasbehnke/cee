package com.cee.news.client.util;

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
