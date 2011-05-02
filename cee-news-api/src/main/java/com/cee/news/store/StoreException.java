package com.cee.news.store;

public class StoreException extends Exception {

    private static final long serialVersionUID = 2467663447961830861L;
    
    private final Object entity;

    public StoreException(Object entity) {
        super();
        this.entity = entity;
    }

    public StoreException(String message) {
        super(message);
        entity = null;
    }

    public StoreException(Object entity, String message, Throwable cause) {
        super(message, cause);
        this.entity = entity;
    }

    public StoreException(Object entity, String message) {
        super(message);
        this.entity = entity;
    }

    public StoreException(Object entity, Throwable cause) {
        super(cause);
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }

}
