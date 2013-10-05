package org.cee.store.lucene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuceneAnalyzers {
	
	private final static Logger LOG = LoggerFactory.getLogger(LuceneAnalyzers.class);

	private final HashMap<String, Analyzer> analyzers;
	
	private final Analyzer defaultAnalyzer = new StandardAnalyzer(LuceneConstants.VERSION);
	
	public LuceneAnalyzers() {
		analyzers = new HashMap<String, Analyzer>();
		analyzers.put("ar", new org.apache.lucene.analysis.ar.ArabicAnalyzer(LuceneConstants.VERSION));
		analyzers.put("bg", new org.apache.lucene.analysis.bg.BulgarianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("br", new org.apache.lucene.analysis.br.BrazilianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("pt-br", new org.apache.lucene.analysis.br.BrazilianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("ca", new org.apache.lucene.analysis.ca.CatalanAnalyzer(LuceneConstants.VERSION));
		analyzers.put("cs", new org.apache.lucene.analysis.cz.CzechAnalyzer(LuceneConstants.VERSION));
		analyzers.put("cz", new org.apache.lucene.analysis.cz.CzechAnalyzer(LuceneConstants.VERSION));
		analyzers.put("da", new org.apache.lucene.analysis.da.DanishAnalyzer(LuceneConstants.VERSION));
		analyzers.put("de", new org.apache.lucene.analysis.de.GermanAnalyzer(LuceneConstants.VERSION));
		analyzers.put("el", new org.apache.lucene.analysis.el.GreekAnalyzer(LuceneConstants.VERSION));
		analyzers.put("en", new org.apache.lucene.analysis.en.EnglishAnalyzer(LuceneConstants.VERSION));
		analyzers.put("es", new org.apache.lucene.analysis.es.SpanishAnalyzer(LuceneConstants.VERSION));
		analyzers.put("eu", new org.apache.lucene.analysis.eu.BasqueAnalyzer(LuceneConstants.VERSION));
		analyzers.put("fa", new org.apache.lucene.analysis.fa.PersianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("fi", new org.apache.lucene.analysis.fi.FinnishAnalyzer(LuceneConstants.VERSION));
		analyzers.put("fr", new org.apache.lucene.analysis.fr.FrenchAnalyzer(LuceneConstants.VERSION));
		analyzers.put("ga", new org.apache.lucene.analysis.ga.IrishAnalyzer(LuceneConstants.VERSION));
		analyzers.put("gl", new org.apache.lucene.analysis.gl.GalicianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("hi", new org.apache.lucene.analysis.hi.HindiAnalyzer(LuceneConstants.VERSION));
		analyzers.put("hu", new org.apache.lucene.analysis.hu.HungarianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("hy", new org.apache.lucene.analysis.hy.ArmenianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("id", new org.apache.lucene.analysis.id.IndonesianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("it", new org.apache.lucene.analysis.it.ItalianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("lv", new org.apache.lucene.analysis.lv.LatvianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("nl", new org.apache.lucene.analysis.nl.DutchAnalyzer(LuceneConstants.VERSION));
		analyzers.put("no", new org.apache.lucene.analysis.no.NorwegianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("pt", new org.apache.lucene.analysis.pt.PortugueseAnalyzer(LuceneConstants.VERSION));
		analyzers.put("ro", new org.apache.lucene.analysis.ro.RomanianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("ru", new org.apache.lucene.analysis.ru.RussianAnalyzer(LuceneConstants.VERSION));
		analyzers.put("sv", new org.apache.lucene.analysis.sv.SwedishAnalyzer(LuceneConstants.VERSION));
		analyzers.put("th", new org.apache.lucene.analysis.th.ThaiAnalyzer(LuceneConstants.VERSION));
		analyzers.put("tr", new org.apache.lucene.analysis.tr.TurkishAnalyzer(LuceneConstants.VERSION));
		
		//Japanese
		analyzers.put("ja", new org.apache.lucene.analysis.cjk.CJKAnalyzer(LuceneConstants.VERSION));
		analyzers.put("jp", new org.apache.lucene.analysis.cjk.CJKAnalyzer(LuceneConstants.VERSION));
		
		//Chinese
		analyzers.put("zh", new org.apache.lucene.analysis.cjk.CJKAnalyzer(LuceneConstants.VERSION));
		analyzers.put("cn", new org.apache.lucene.analysis.cjk.CJKAnalyzer(LuceneConstants.VERSION));
		
		//Korean
		analyzers.put("ko", new org.apache.lucene.analysis.cjk.CJKAnalyzer(LuceneConstants.VERSION));
	}
	
	public List<String> getSupportedLanguages() {
		ArrayList<String> languages = new ArrayList<>(analyzers.keySet());
		Collections.sort(languages);
		return languages;
	}
	
	public Analyzer getAnalayserForLanguage(String language) {
		if (language == null) {
			throw new IllegalArgumentException("Parameter language is requried");
		}
		String lang = language.toLowerCase();
		Analyzer analyzer = analyzers.get(lang);
		if (analyzer == null && language.length() > 2) {
			analyzer = analyzers.get(language.substring(0, 2));
			if (analyzer == null) {
				LOG.warn("Found unsupported language {}, fall back to StandardAnalyzer.", language);
				return defaultAnalyzer;
			}
		}
		return analyzer;
	}
}
