package org.cee.language;


public interface LanguageDetector {

	/**
	 * Detects language of text
	 * @return ISO 636 language code or null if language could not be determined 
	 */
	public String detect(String text);
	
}
