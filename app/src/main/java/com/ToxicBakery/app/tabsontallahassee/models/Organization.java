package com.ToxicBakery.app.tabsontallahassee.models;

import com.ToxicBakery.app.tabsontallahassee.db.IdTagged;

@SuppressWarnings("unused")
public class Organization implements IdTagged {

    private String type;
    private String id;
    private String classification;
    private String name;

    @Override
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getClassification() {
        return classification;
    }

    public String getName() {
        return name;
    }

}
