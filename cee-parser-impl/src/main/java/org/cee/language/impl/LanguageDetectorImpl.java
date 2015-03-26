package org.cee.language.impl;

/*
 * #%L
 * Content Extraction Engine - News Parser Implementations
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
		try (InputStream input = getClass().getResourceAsStream(PROFILE_RESOURCE + language)) {
			return IOUtils.toString(input);
		}
	}
	
	/**
	 * Initialize language detection subsystem
	 */
	private synchronized void initialize() throws IOException, LangDetectException {
		if (initialized) {
			return;
		}
		try (InputStream input = getClass().getResourceAsStream(LANGUAGE_LIST_RESOURCE)) {
			List<String> languageList = IOUtils.readLines(input);
			List<String> languageProfiles = new ArrayList<String>();
			for (String language : languageList) {
	            languageProfiles.add(readProfileFromResource(language));
            }
			DetectorFactory.loadProfile(languageProfiles);
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
