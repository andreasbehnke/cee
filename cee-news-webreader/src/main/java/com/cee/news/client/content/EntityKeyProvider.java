package com.cee.news.client.content;

import com.google.gwt.view.client.ProvidesKey;

public class EntityKeyProvider<K> implements ProvidesKey<EntityContent<K>> {

	@Override
	public K getKey(EntityContent<K> item) {
		return item.getKey();
	}

}
