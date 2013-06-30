package com.cee.news.language.impl;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

import org.junit.Test;

import com.cee.news.language.LanguageDetector;
import com.cee.news.model.Site;

public class TestDelegatingLanguageDetector {

	@Test
	public void testOne() {
		List<LanguageDetector> detectors = new ArrayList<LanguageDetector>();
		detectors.add( new  LanguageDetector() {
			
			@Override
			public String detect(Site site) {
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
			public String detect(Site site) {
				return "de";
			}
		});
		detectors.add( new  LanguageDetector() {
			
			@Override
			public String detect(Site site) {
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
			public String detect(Site site) {
				return null;
			}
		});
		detectors.add( new  LanguageDetector() {
			
			@Override
			public String detect(Site site) {
				return "en";
			}
		});
		DelegatingLanguageDetector detector = new DelegatingLanguageDetector(detectors);
		assertEquals("en", detector.detect(null));
	}
}
