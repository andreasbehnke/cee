package com.cee.news.parser.impl;

import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;

public class NormalizedLevenshteinDistance implements BoilerpipeFilter {
	
	private final String label;
	
	private final double maxDistance;
	
	private final String match;
	
	private final boolean stopAtFirstMatch;
	
	public static double calculate(String a, String b) {
		double result = 1.0;

        if (a != null && b != null) {
        	result = 0.0;
            int max = Math.max(a.length(), b.length());
            if (max > 0) {
                    a = a.toLowerCase();
                    b = b.toLowerCase();
                    int distance = Levenshtein.distance(a, b);
                    result = (double)distance / (double)max;
            }
        }
        return result;
	}
	
	public NormalizedLevenshteinDistance(String label, String match, double maxDistance, boolean stopAtFirstMatch) {
		this.label = label;
		this.match = match;
		this.maxDistance = maxDistance;
		this.stopAtFirstMatch = stopAtFirstMatch;
	}

	@Override
	public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
		boolean changes = false;
		for (TextBlock tb : doc.getTextBlocks()) {
			String text = tb.getText();
			if (calculate(text, match) < maxDistance) {
				tb.addLabel(label);
				changes = true;
				if (stopAtFirstMatch) {
					break;
				}
			}
		}
		return changes;
	}

}
