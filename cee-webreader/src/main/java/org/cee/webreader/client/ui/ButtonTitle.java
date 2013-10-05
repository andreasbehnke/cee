package org.cee.webreader.client.ui;

import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Button;

public class ButtonTitle implements HasSafeHtml {
    
    private final Button button;
    
    public ButtonTitle(final Button button) {
        this.button = button;
    }

    @Override
    public void setHTML(SafeHtml html) {
        button.setTitle(html.asString());
    }

}
