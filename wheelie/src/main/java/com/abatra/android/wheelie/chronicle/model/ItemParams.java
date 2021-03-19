package com.abatra.android.wheelie.chronicle.model;

public interface ItemParams<T extends ItemParams<T>> {

    String getId();

    T setId(String id);

    String getName();

    T setName(String name);
}
