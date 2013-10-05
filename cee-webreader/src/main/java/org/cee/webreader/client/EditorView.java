package org.cee.webreader.client;


public interface EditorView<T> {
    
    void edit(T wsd);
    
    T getData();

    boolean hasValidationErrors();
    
    void showValidationErrors();
    
    
}
