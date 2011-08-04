package com.cee.news.client.util;

import com.google.gwt.safehtml.shared.SafeHtml;

public final class SafeHtmlUtil {

	private SafeHtmlUtil() {};
	
	//TODO implement HTML sanitizer which has understanding for attributes and different tags like span, div, p, img
	public static SafeHtml sanitize(final String html) {
		return ContentHtmlSanitizer.sanitizeHtml(html);
	}
}
 