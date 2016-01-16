package com.ToxicBakery.app.tabsontallahassee.models;

@SuppressWarnings("unused")
public class Version {

    private String note;
    private String date;
    private Link[] links;

    public String getDate() {
        return date;
    }

    public Link[] getLinks() {
        return links;
    }

    public String getNote() {
        return note;
    }
}
