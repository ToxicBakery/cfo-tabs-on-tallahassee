package com.ToxicBakery.app.tabsontallahassee.models;

@SuppressWarnings("unused")
public class Action {

    private String[] classifications;
    private String description;
    private String date;
    private int order;
    private Organization organization;

    public String[] getClassifications() {
        return classifications;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getOrder() {
        return order;
    }

    public Organization getOrganization() {
        return organization;
    }

}
