package com.cee.news.client.list;

import com.google.gwt.view.client.ProvidesKey;

public interface KeyProvider<T, K> extends ProvidesKey<T> {

    K getKey(T value);
}
