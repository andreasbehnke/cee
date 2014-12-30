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

import static junit.framework.Assert.*;

import java.io.IOException;

import org.cee.language.impl.LanguageDetectorImpl;
import org.junit.Test;

import com.cybozu.labs.langdetect.LangDetectException;

public class TestLanguageDetectorImpl {

	@Test
	public void testDetect() throws IOException, LangDetectException {
		LanguageDetectorImpl detector = new LanguageDetectorImpl();

		assertEquals("de", detector.detect("Männer die auf Ziegen starren"));
		assertEquals("en", detector.detect("This was a nice burger"));
		assertEquals("ja", detector.detect("ライブラリはすでに多数存在しますが、"));
		assertNull(detector.detect("23 237 32 234"));
	}

}