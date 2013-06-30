package com.cee.news.language.impl;

import java.util.List;

import com.cee.news.language.LanguageDetector;
import com.cee.news.model.Site;

/**
 * Delegates language detection request to a list of {@link LanguageDetector}s.
 */
public class DelegatingLanguageDetector implements LanguageDetector {
	
	private final List<LanguageDetector> detectors;
	
	public DelegatingLanguageDetector(List<LanguageDetector> detectors) {
	    this.detectors = detectors;
    }

	@Override
	public String detect(Site site) {
		for (LanguageDetector detector : detectors) {
	        String language = detector.detect(site);
	        if (language != null) {
	        	return language;
	        }
        }
		return null;
	}

}
