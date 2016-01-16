package com.ToxicBakery.app.tabsontallahassee.models;

import com.ToxicBakery.app.tabsontallahassee.db.IdTagged;

import java.util.Map;

@SuppressWarnings("unused")
public class Bill implements IdTagged {

    private String type;
    private String id;
    private Attributes attributes;
    private Map<String, Relationship> relationships;

    public Attributes getAttributes() {
        return attributes;
    }

    public String getId() {
        return id;
    }

    public Map<String, Relationship> getRelationships() {
        return relationships;
    }

    public String getType() {
        return type;
    }

}
