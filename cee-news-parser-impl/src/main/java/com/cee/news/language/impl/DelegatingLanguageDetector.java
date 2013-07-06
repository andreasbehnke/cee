package com.cee.news.language.impl;

import java.util.List;

import com.cee.news.SiteExtraction;
import com.cee.news.language.LanguageDetector;

/**
 * Delegates language detection request to a list of {@link LanguageDetector}s.
 */
public class DelegatingLanguageDetector implements LanguageDetector {
	
	private List<LanguageDetector> detectors;
	
	public DelegatingLanguageDetector() {}
	
	public DelegatingLanguageDetector(List<LanguageDetector> detectors) {
	    this.detectors = detectors;
    }
	
	public void setDetectors(List<LanguageDetector> detectors) {
	    this.detectors = detectors;
    }

	@Override
	public String detect(SiteExtraction content) {
		for (LanguageDetector detector : detectors) {
	        String language = detector.detect(content);
	        if (language != null) {
	        	return language;
	        }
        }
		return null;
	}
}
