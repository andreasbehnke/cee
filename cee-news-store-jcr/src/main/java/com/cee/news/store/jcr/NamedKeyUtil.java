package com.cee.news.store.jcr;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.model.NamedKey;

public final class NamedKeyUtil {

	private NamedKeyUtil() {}
	
	public static List<String> extractKeys(List<NamedKey> input) {
		List<String> keys = new ArrayList<String>(input.size());
		for (NamedKey key : input) {
			keys.add(key.getKey());
		}
		return keys;
	}
	
	public static List<String> extractNames(List<NamedKey> input) {
		List<String> keys = new ArrayList<String>(input.size());
		for (NamedKey key : input) {
			keys.add(key.getName());
		}
		return keys;
	}
}
