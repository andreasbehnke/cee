package org.cee.language.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.cee.language.LanguageDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

/**
 * {@link LanguageDetector} implementation based on com.cybozu.labs langdetect.
 */
public class LanguageDetectorImpl implements LanguageDetector {
	
	private static final Logger LOG = LoggerFactory.getLogger(LanguageDetectorImpl.class);
	
	private static final String PROFILE_RESOURCE = "/profiles/";
	
	private static final String LANGUAGE_LIST_RESOURCE = "langlist";
	
	private static boolean initialized = false;
	
	public LanguageDetectorImpl() throws IOException, LangDetectException {
		initialize();
	}
	
	private String readProfileFromResource(String language) throws IOException {
		InputStream input = getClass().getResourceAsStream(PROFILE_RESOURCE + language);
		try {
			return IOUtils.toString(input);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
	
	/**
	 * Initialize language detection subsystem
	 */
	private synchronized void initialize() throws IOException, LangDetectException {
		if (initialized) {
			return;
		}
		InputStream input = getClass().getResourceAsStream(LANGUAGE_LIST_RESOURCE);
		try {
			List<String> languageList = IOUtils.readLines(input);
			List<String> languageProfiles = new ArrayList<String>();
			for (String language : languageList) {
	            languageProfiles.add(readProfileFromResource(language));
            }
			DetectorFactory.loadProfile(languageProfiles);
		} finally {
			IOUtils.closeQuietly(input);
		}
		
		initialized = true;
	}

	@Override
	public String detect(String text) {
		try {
			Detector detector = DetectorFactory.create();
			detector.append(text);
			String language = detector.detect();
			if (language != null && language.equalsIgnoreCase("unknown")) {
				return null;
			}
			return language;
        } catch (LangDetectException e) {
        	LOG.warn("Could not detect language of text \"{}\": {}", text, e.getLocalizedMessage());
	        return null;
        }
	}

}
