package com.cee.news.client.list;

import com.cee.news.model.EntityKey;
import com.google.gwt.view.client.ProvidesKey;

public class EntityKeyProvider implements ProvidesKey<EntityKey> {

	@Override
	public Object getKey(EntityKey item) {
		return item;
	}

}
