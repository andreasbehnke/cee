package com.cee.news.language.impl;

import static junit.framework.Assert.*;

import java.io.IOException;

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
