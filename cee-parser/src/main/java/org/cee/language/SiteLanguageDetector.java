package org.cee.language;

import java.util.List;

import org.cee.SiteExtraction;
import org.cee.news.model.Feed;
import org.cee.news.model.Site;

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
		String text = siteExtraction.getContent().toString();
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
