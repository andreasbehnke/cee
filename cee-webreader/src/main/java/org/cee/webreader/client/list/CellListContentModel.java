package org.cee.webreader.client.list;

import java.util.ArrayList;
import java.util.List;

import org.cee.webreader.client.content.EntityContent;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CellListContentModel<K> {
    
    /**
     * Get rendered content for given entity keys
     */
    void getContent(ArrayList<K> keys, AsyncCallback<List<EntityContent<K>>> callback);

}
