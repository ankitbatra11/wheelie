package com.abatra.android.wheelie.chronicle.model;

public class Item implements ItemParams<Item> {

    private String id;
    private String name;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Item setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Item setName(String name) {
        this.name = name;
        return this;
    }
}
