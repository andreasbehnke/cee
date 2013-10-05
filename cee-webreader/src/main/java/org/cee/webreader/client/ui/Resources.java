package org.cee.webreader.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

public interface Resources extends ClientBundle {

    public static final Resources INSTANCE = GWT.create(Resources.class);
    
    @Source("icons24.png")
    public DataResource icons();
    
    @Source("loading.gif")
    public DataResource loadingIcon();
    
    @Source("Styles.css")
    public Styles styles();
}
