package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.model.EntityKey;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EntityContentModel {

	/**
	 * Get rendered content for given entity keys
	 */
	void getContent(ArrayList<EntityKey> keys, AsyncCallback<List<EntityContent>> callback);
	
}
