package com.cee.news.language.impl;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.cee.news.SiteExtraction;
import com.cee.news.language.LanguageDetector;

public class TestDelegatingLanguageDetector {

	@Test
	public void testOne() {
		List<LanguageDetector> detectors = new ArrayList<LanguageDetector>();
		detectors.add( new  LanguageDetector() {
			
			@Override
			public String detect(SiteExtraction siteExtraction) {
				return "de";
			}
		});
		DelegatingLanguageDetector detector = new DelegatingLanguageDetector(detectors);
		assertEquals("de", detector.detect(null));
	}

	@Test
	public void testFirstMatch() {
		List<LanguageDetector> detectors = new ArrayList<LanguageDetector>();
		detectors.add( new  LanguageDetector() {
			
			@Override
			public String detect(SiteExtraction siteExtraction) {
				return "de";
			}
		});
		detectors.add( new  LanguageDetector() {
			
			@Override
			public String detect(SiteExtraction siteExtraction) {
				return "en";
			}
		});
		DelegatingLanguageDetector detector = new DelegatingLanguageDetector(detectors);
		assertEquals("de", detector.detect(null));
	}
	
	@Test
	public void testSecondMatch() {
		List<LanguageDetector> detectors = new ArrayList<LanguageDetector>();
		detectors.add( new  LanguageDetector() {
			
			@Override
			public String detect(SiteExtraction siteExtraction) {
				return null;
			}
		});
		detectors.add( new  LanguageDetector() {
			
			@Override
			public String detect(SiteExtraction siteExtraction) {
				return "en";
			}
		});
		DelegatingLanguageDetector detector = new DelegatingLanguageDetector(detectors);
		assertEquals("en", detector.detect(null));
	}
}
