package com.cee.news.store.jcr;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.model.EntityKey;

public final class NamedKeyUtil {

	private NamedKeyUtil() {}
	
	public static List<String> extractKeys(List<EntityKey> input) {
		List<String> keys = new ArrayList<String>(input.size());
		for (EntityKey key : input) {
			keys.add(key.getKey());
		}
		return keys;
	}
	
	public static List<String> extractNames(List<EntityKey> input) {
		List<String> keys = new ArrayList<String>(input.size());
		for (EntityKey key : input) {
			keys.add(key.getName());
		}
		return keys;
	}
}
