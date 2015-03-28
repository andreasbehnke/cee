package org.cee.language;

/*
 * #%L
 * Content Extraction Engine - News Parser
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

import java.util.List;

import org.cee.SiteExtraction;
import org.cee.store.site.Feed;
import org.cee.store.site.Site;

/**
 * Tries to detect site's language by using site feeds. If this fails,
 * delegates language detection request to a list of {@link LanguageDetector}s.
 */
public class SiteLanguageDetector {
	
	private List<LanguageDetector> detectors;
	
	public SiteLanguageDetector() {}
	
	public SiteLanguageDetector(List<LanguageDetector> detectors) {
	    this.detectors = detectors;
    }
	
	public void setDetectors(List<LanguageDetector> detectors) {
	    this.detectors = detectors;
    }
	
	private String detectLanguageFromFeeds(SiteExtraction siteExtraction) {
		Site site = siteExtraction.getSite();
	    for (Feed feed : site.getFeeds()) {
	    	String language = feed.getLanguage(); 
	        if (language != null) {
	        	return language;
	        }
        }
	    return null;
	}

	public String detect(SiteExtraction siteExtraction) {
		String language = detectLanguageFromFeeds(siteExtraction);
		if (language != null) {
        	return language;
        }
		String text = siteExtraction.getSiteContent();
		if (detectors != null) {
			for (LanguageDetector detector : detectors) {
				language = detector.detect(text);
				if (language != null) {
					return language;
				}
			}
		}
		return null;
	}
}
