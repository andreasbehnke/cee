package org.cee.webreader.client.util;

import com.google.gwt.safehtml.shared.HtmlSanitizer;
import com.google.gwt.safehtml.shared.SafeHtml;

public final class SafeHtmlUtil {

	private SafeHtmlUtil() {};
	
	private static final HtmlSanitizer sanitizer = new ContentHtmlSanitizer(false);
	
	public static SafeHtml sanitize(final String html) {
		return sanitizer.sanitize(html);
	}
}
 