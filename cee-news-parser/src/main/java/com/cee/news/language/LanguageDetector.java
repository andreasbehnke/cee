package com.cee.news.language;

import com.cee.news.SiteExtraction;

public interface LanguageDetector {

	/**
	 * Detects language of site
	 * @return ISO 636 language code or null if language could not be determined 
	 */
	public String detect(SiteExtraction siteExtraction);
	
}
