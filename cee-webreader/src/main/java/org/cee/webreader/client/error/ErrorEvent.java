package org.cee.webreader.client.error;

import com.google.gwt.event.shared.GwtEvent;

public class ErrorEvent extends GwtEvent<ErrorHandler> {
    
    public final static Type<ErrorHandler> TYPE = new Type<ErrorHandler>();
    
    private final Throwable cause;
    
    private final String description;
    
    public ErrorEvent(Throwable cause, String description) {
        super();
        this.cause = cause;
        this.description = description;
    }

    public Throwable getCause() {
        return cause;
    }
    
    public String getDescription() {
        return description;
    }

    @Override
    public Type<ErrorHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ErrorHandler handler) {
        handler.onError(this);
    }
}