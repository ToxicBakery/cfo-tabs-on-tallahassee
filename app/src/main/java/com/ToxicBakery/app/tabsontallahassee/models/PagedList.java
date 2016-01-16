package com.ToxicBakery.app.tabsontallahassee.models;

public abstract class PagedList<T> {

    private PageLinks links;
    private T[] data;
    private Meta meta;

    public T[] getData() {
        return data;
    }

    public PageLinks getLinks() {
        return links;
    }

    public Meta getMeta() {
        return meta;
    }

}
