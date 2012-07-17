package com.cee.news.client;


public interface EditorView<T> {
    
    void edit(T wsd);
    
    T getData();

    boolean hasValidationErrors();
    
    void showValidationErrors();
    
    
}
